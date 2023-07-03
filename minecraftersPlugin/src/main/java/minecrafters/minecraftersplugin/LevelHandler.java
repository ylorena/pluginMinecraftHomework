package minecrafters.minecraftersplugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.plugin.Plugin;

public class LevelHandler {
    static private BossBar bossBar = null;
    static private UUID playerId = null;
    static private Plugin plugin = null;
    static private LevelHandler levelHandler = null;

    static public LevelHandler getLevelHandler(Plugin _plugin) {
        if (levelHandler == null) {
            bossBar = _plugin.getServer().createBossBar("", org.bukkit.boss.BarColor.GREEN,
                    org.bukkit.boss.BarStyle.SOLID);
            levelHandler = new LevelHandler();
            plugin = _plugin;
        }
        return levelHandler;
    }

    public void handleUserLevel(Event event) {
        if (loadPlayerData() == null) {
            createPlayerData();
        }
        if (event instanceof PlayerEvent) {
            playerId = ((PlayerEvent)event).getPlayer().getUniqueId();
        }
        ActivityEnum eventName = ActivityEnum.findByEventName(event.getEventName());

        increaseXp(eventName);
        if (getEventExperience(eventName) >= getExperienceRequiredForLevelUp(getEventLevel(eventName))) {
            levelUp(eventName);
        }

        savePlayerData(eventName, getEventLevel(eventName), getEventExperience(eventName));
        updateBossBar(eventName);
    }

    public void increaseXp(ActivityEnum eventName) {
        savePlayerData(eventName, getEventLevel(eventName), getEventExperience(eventName) + 1);
        
            if (getEventLevel(eventName) >= 20) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                controlFire(player);
                controlWater(player);
                controlEarth(player);
                controlAir(player);
            }
        }

        if (eventName == ActivityEnum.MINING && getEventLevel(eventName) >= 15) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1));
            }
        }

        if (eventName == ActivityEnum.FISHING && getEventLevel(eventName) >= 10) {
            plugin.getServer().getPluginManager().registerEvents(new FishingListener(), plugin);
        }

        savePlayerData(eventName, getEventLevel(eventName), getEventExperience(eventName) + 1);
    }

    private void controlFire(Player player) {
        player.launchProjectile(Fireball.class);
    }

    private void controlEarth(Player player) {
        Location playerLocation = player.getLocation();
        World world = player.getWorld();
        Block block = playerLocation.getBlock();
        block.setType(Material.STONE);
    }

    private void controlAir(Player player) {
        Location playerLocation = player.getLocation();
        World world = player.getWorld();
        player.setVelocity(player.getVelocity().add(new Vector(0, 1, 0)));
        world.spawnParticle(Particle.CLOUD, playerLocation, 100);
    }

    private void controlWater(Player player) {
        Location playerLocation = player.getLocation();
        World world = player.getWorld();
        world.spawnParticle(Particle.WATER_SPLASH, playerLocation, 100);
        world.playSound(playerLocation, Sound.ENTITY_PLAYER_SPLASH, 1.0f, 1.0f);

    }



    public double getExperienceRequiredForLevelUp(int level) {
        return (int) Math.pow(level, 2) + 100;
    }

    private void checkLevel50() {
            if (getEventLevel(ActivityEnum.MINING) == 50) {
            World world = Bukkit.getWorld("world"); //Substitua world pelo nome do seu mundo
            if (world != null) {
                world.setTime(18000);

                Player player = Bukkit.getPlayer(playerId);
                if (player != null) {
                    player.getInventory().addItem(new ItemStack(Material.TORCH, 1));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
                }
            }
        }
    }

    public void levelUp(ActivityEnum eventName) {
        int currentLevel = getEventLevel(eventName);
        int nextLevel = currentLevel + 1;

        if (nextLevel == 3) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                World world = player.getWorld();
                Location chestLocation = player.getLocation().add(0, 1, 0); // Posição acima do jogador
                
                Block chestBlock = world.getBlockAt(chestLocation);
                chestBlock.setType(Material.CHEST);
                
                Chest chest = (Chest) chestBlock.getState();
                Inventory chestInventory = chest.getInventory();
                chestInventory.addItem(new ItemStack(Material.DIAMOND, 1));
                chestInventory.addItem(new ItemStack(Material.GOLD_INGOT, 3));
                
                player.sendMessage("Você encontrou um baú escondido!");

                world.playSound(chestLocation, Sound.BLOCK_END_PORTAL_FRAME_FILL, 1.0f, 1.0f);
                world.spawnParticle(Particle.EXPLOSION_NORMAL, chestLocation, 10);
            }
        }

        savePlayerData(eventName, nextLevel, 0);

    }

    public void updateBossBar(ActivityEnum eventName) {
        Player player = plugin.getServer().getPlayer(playerId);
        bossBar.removePlayer(player);
        double progress = (double) getEventExperience(eventName)
                / getExperienceRequiredForLevelUp(getEventLevel(eventName));
        bossBar.setProgress(progress);
        bossBar.setTitle(eventName.getDisplayName() + " - Level " + getEventLevel(eventName));

        bossBar.addPlayer(player);
        bossBar.setVisible(true);
    }

    private int getEventLevel(ActivityEnum eventName) {
        Map<String, String> playerData = loadPlayerData();
        return Integer.parseInt(playerData.get(eventName.getEventName()).split("\\+")[0]);
    }

    private int getEventExperience(ActivityEnum eventName) {
        Map<String, String> playerData = loadPlayerData();
        return Integer.parseInt(playerData.get(eventName.getEventName()).split("\\+")[1]);
    }

    private Map<String, String> loadPlayerData() {
        File dataFile = new File("plugins/minecraftersPlugin/playerdata/" + playerId + ".dat");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            return (Map<String, String>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    static private void createPlayerData() {
        File dataFolder = new File("plugins/minecraftersPlugin/playerdata");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        if (!Files.exists(Paths.get(dataFolder + "/" + playerId + ".dat"))) {
            File dataFile = new File(dataFolder, playerId + ".dat");
            Map<String, String> data = new HashMap<String, String>();

            for (ActivityEnum eventName : ActivityEnum.values()) {
                data.put(eventName.getEventName().toString(), "0+0");
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
                oos.writeObject(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void savePlayerData(ActivityEnum eventName, int level, int experience) {
        File dataFolder = new File(plugin.getDataFolder(), "playerdata");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File dataFile = new File(dataFolder, playerId + ".dat");
        Map<String, String> data = loadPlayerData();

        data.put(eventName.getEventName(), String.format("%d+%d", level, experience));
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            oos.writeObject(data);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}