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
        EventNamesEnum eventName = EventNamesEnum.findByEventName(event.getEventName());

        increaseXp(eventName);
        if (getEventExperience(eventName) >= getExperienceRequiredForLevelUp(getEventLevel(eventName))) {
            levelUp(eventName);
        }

        savePlayerData(eventName, getEventLevel(eventName), getEventExperience(eventName));
        updateBossBar(eventName);
    }

    public void increaseXp(EventNamesEnum eventName) {
        savePlayerData(eventName, getEventLevel(eventName), getEventExperience(eventName) + 1);
        System.out.print(getEventExperience(eventName) + 1);
    }

    public double getExperienceRequiredForLevelUp(int level) {
        return (int) Math.pow(level, 2) + 100;
    }

    public void levelUp(EventNamesEnum eventName) {
        savePlayerData(eventName, getEventLevel(eventName) + 1, 0);
    }

    public void updateBossBar(EventNamesEnum eventName) {
        Player player = plugin.getServer().getPlayer(playerId);
        bossBar.removePlayer(player);
        double progress = (double) getEventExperience(eventName)
                / getExperienceRequiredForLevelUp(getEventLevel(eventName));
        bossBar.setProgress(progress);
        bossBar.setTitle(eventName.toString() + " - Level " + getEventLevel(eventName));

        bossBar.addPlayer(player);
        bossBar.setVisible(true);
    }

    private int getEventLevel(EventNamesEnum eventName) {
        Map<String, String> playerData = loadPlayerData();
        return Integer.parseInt(playerData.get(eventName.toString()).split("\\+")[0]);
    }

    private int getEventExperience(EventNamesEnum eventName) {
        Map<String, String> playerData = loadPlayerData();
        return Integer.parseInt(playerData.get(eventName.toString()).split("\\+")[1]);
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

            for (EventNamesEnum eventName : EventNamesEnum.values()) {
                data.put(eventName.toString(), "0+0");
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
                oos.writeObject(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void savePlayerData(EventNamesEnum eventName, int level, int experience) {
        File dataFolder = new File(plugin.getDataFolder(), "playerdata");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File dataFile = new File(dataFolder, playerId + ".dat");
        Map<String, String> data = loadPlayerData();

        data.put(eventName.toString(), String.format("%d+%d", level, experience));
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            oos.writeObject(data);
            oos.flush();
            plugin.getServer().getConsoleSender().sendMessage("[MinecraftersPlugin] Player data saved for " + playerId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}