package minecrafters.minecraftersplugin;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class PlayerLevelManager {
    private int level;
    private int experience;
    private BossBar bossBar;
    private String tipo;
    private final Map<String, Integer> data = new HashMap<>();
    private final UUID playerId; // Player's UUID
    private final Plugin plugin; // Plugin instance

    public PlayerLevelManager(Player player, Plugin plugin, String tipo) {
        this.level = 1;
        this.experience = 0;
        this.tipo = tipo;
        this.plugin = plugin;
        this.playerId = player.getUniqueId();

        // Check if the player already has a boss bar attached
        for (Iterator<KeyedBossBar> it = plugin.getServer().getBossBars(); it.hasNext(); ) {
            BossBar bar = it.next();
            if (bar.getPlayers().contains(player)) {
                this.bossBar = bar;
                break;
            }
        }

        // If the player does not have a boss bar attached, create a new one
        if (bossBar == null) {
            this.bossBar = plugin.getServer().createBossBar("Level de " + this.tipo + ": " + level, BarColor.YELLOW, BarStyle.SEGMENTED_6);
            bossBar.addPlayer(player);
        }

        loadPlayerData(); // Load player's data from file

        if (data.containsKey(tipo)) {
            this.level = data.get(tipo);
            this.experience = getExperienceRequiredForLevelUp(level);
            updateBossBar();
        }
    }


    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int xp) {
        this.experience = xp;
    }

    private void levelUp() {
        level++;
        bossBar.setTitle("Level de " + this.tipo + ": " + level);
        plugin.getServer().getConsoleSender().sendMessage("[MinecraftersPlugin] Player " + playerId + " leveled up to level " + level);
        savePlayerData(); // Save player's data after leveling up
    }

    public void addExperience(int amount) {
        experience += amount;

        while (experience >= getExperienceRequiredForLevelUp(level)) {
            levelUp();
            experience -= getExperienceRequiredForLevelUp(level - 1); // Deduct the required experience from the previous level
            savePlayerData();
        }

        updateBossBar(); // Update the boss bar when experience changes
        savePlayerData();
        printPlayerData(); // Print the player's data to the console
    }


    private int getExperienceRequiredForLevelUp(int currentLevel) {
        return currentLevel * 10; // Example formula: 10 experience points per level
    }

    private void updateBossBar() {
        double progress = (double) experience / getExperienceRequiredForLevelUp(level);
        bossBar.setProgress(progress);

        // Update the boss bar title with the correct level
        bossBar.setTitle("Level de " + this.tipo + ": " + level);
    }


    private void loadPlayerData() {
        // Load player's data from file or create a new map if the file doesn't exist
        File dataFile = new File("plugins/MinecraftersPlugin/playerdata/" + playerId + ".dat");
        if (dataFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
                Map<String, Integer> loadedData = (Map<String, Integer>) ois.readObject();
                plugin.getServer().getConsoleSender().sendMessage("[MinecraftersPlugin] Player data loaded for " + playerId);
                plugin.getServer().getConsoleSender().sendMessage(String.valueOf(loadedData));

                // Check if the loaded data contains the level and experience values
                if (loadedData.containsKey(tipo)) {
                    this.level = loadedData.get(tipo);
                }
                if (loadedData.containsKey("experience")) {
                    this.experience = loadedData.get("experience");
                }

                updateBossBar();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            plugin.getServer().getConsoleSender().sendMessage("[MinecraftersPlugin] Player data file does not exist for " + playerId);
        }
    }

    private void savePlayerData() {
        // Save player's data to file
        File dataFolder = new File(plugin.getDataFolder(), "playerdata");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File dataFile = new File(dataFolder, playerId + ".dat");
        Map<String, Integer> data = new HashMap<>();
        data.put(tipo, level);
        data.put("experience", experience); // Include the experience value in the data map
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            oos.writeObject(data);
            plugin.getServer().getConsoleSender().sendMessage("[MinecraftersPlugin] Player data saved for " + playerId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void printPlayerData() {
        StringBuilder sb = new StringBuilder();
        sb.append("[MinecraftersPlugin] Player data for ").append(playerId).append(":");
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            sb.append(" ").append(entry.getKey()).append(": ").append(entry.getValue());
        }
        plugin.getServer().getConsoleSender().sendMessage(sb.toString());
    }

    public String getTipo() {
        return this.tipo;
    }
}

