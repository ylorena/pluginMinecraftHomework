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
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.plugin.Plugin;

public class LevelHandler {
    private int level = 1;
    private int experienceToLevelUp = 0;
    static private BossBar bossBar = null;
    static private UUID playerId = null;
    static private Plugin plugin = null;
    static private LevelHandler levelHandler = null;

    static public LevelHandler getLevelHandler(Plugin _plugin) {
        if (levelHandler == null) {
            levelHandler = new LevelHandler();
            plugin = _plugin;
        }
        bossBar = _plugin.getServer().createBossBar("", org.bukkit.boss.BarColor.GREEN,
                org.bukkit.boss.BarStyle.SOLID);
        return levelHandler;
    }

    public void handleUserLevel(Event event) {
        createPlayerData();
        if (event instanceof PlayerEvent) {
            playerId = ((PlayerEvent) event).getPlayer().getUniqueId();
        }
        EventNamesEnum eventName = EventNamesEnum.findByEventName(event.getEventName());

        increaseXp(eventName);
        if (getEventExperience(eventName) >= getExperienceRequiredForLevelUp(getEventLevel(eventName))) {
            levelUp(eventName);
        }

        savePlayerData(eventName, level, experienceToLevelUp);
        updateBossBar(eventName);
    }

    public void increaseXp(EventNamesEnum eventName) {
        savePlayerData(eventName, getEventLevel(eventName), getEventExperience(eventName) + 1);
    }

    public double getExperienceRequiredForLevelUp(int level) {
        return (int) Math.pow(level, 2) + 100;
    }

    public void levelUp(EventNamesEnum eventName) {
        level += 1;
        experienceToLevelUp = (int) getExperienceRequiredForLevelUp(level);
        updateBossBar(eventName);
    }

    public void updateBossBar(EventNamesEnum eventName) {
        double progress = (double) experienceToLevelUp / getExperienceRequiredForLevelUp(level);
        bossBar.setProgress(progress);
        bossBar.setTitle(eventName.toString() + " - Level " + level);
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
        File dataFile = new File("plugins/MinecraftersPlugin/playerdata/" + playerId + ".dat");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            return (Map<String, String>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    static private void createPlayerData() {
        File dataFolder = new File("plugins/MinecraftersPlugin/playerdata");
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

        data.put(eventName.toString(), "{0}+{1}".format(Integer.toString(level), Integer.toString(experience)));

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            oos.writeObject(data);
            plugin.getServer().getConsoleSender().sendMessage("[MinecraftersPlugin] Player data saved for " + playerId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}