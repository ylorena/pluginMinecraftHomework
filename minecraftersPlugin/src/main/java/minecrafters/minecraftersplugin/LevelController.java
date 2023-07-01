package minecrafters.minecraftersplugin;

import org.bukkit.boss.BossBar;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LevelController {
    private int level = 1;
    private int experience = 0;
    private BossBar bossBar = null;
    private String tipo;
    static private final UUID playerId = null;
    static private final Plugin plugin = null;
    static private LevelController levelController = null;

    static public LevelController getLevelController() {
        if (levelController == null) {
            levelController = new LevelController();
        }
        return levelController;
    }

    private void levelUp() {
        level++;
        savePlayerData();
        updateBossBar();
    }

    public void addExperience(int amount) {
        experience += amount;

        while (experience >= getExperienceRequiredForLevelUp(level)) {
            levelUp();
            experience -= getExperienceRequiredForLevelUp(level - 1);
            savePlayerData();
        }

        updateBossBar();
        savePlayerData();
    }

    private int getExperienceRequiredForLevelUp(int currentLevel) {
        return (int) Math.round(100 * Math.pow(1.1, currentLevel));
    }

    public void updateBossBar() {
        double progress = (double) experience / getExperienceRequiredForLevelUp(level);
        bossBar.setProgress(progress);
        bossBar.setTitle(this.tipo + " - Level " + level);
    }

    private void loadPlayerData() {
        File dataFile = new File("plugins/MinecraftersPlugin/playerdata/" + playerId + ".dat");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            Map<String, String> loadedData = (Map<String, String>) ois.readObject();

            this.level = Integer.parseInt(loadedData.get("tipo").split("+")[0]);
            this.experience = Integer.parseInt(loadedData.get("tipo").split("+")[1]);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void savePlayerData() {
        // Save player's data to file
        File dataFolder = new File(plugin.getDataFolder(), "playerdata");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File dataFile = new File(dataFolder, playerId + ".dat");
        Map<String, String> data = new HashMap<>();

        data.put(tipo, "{0}+{1}".format(Integer.toString(this.level), Integer.toString(this.experience)));

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            oos.writeObject(data);
            plugin.getServer().getConsoleSender().sendMessage("[MinecraftersPlugin] Player data saved for " + playerId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}