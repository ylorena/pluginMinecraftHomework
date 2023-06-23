package minecrafters.minecraftersplugin;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class PlayerLevelManager {
    private int level;
    private int experience;
    private BossBar bossBar;

    public PlayerLevelManager(Player player, Plugin plugin) {
        this.level = 1;
        this.experience = 0;
        this.bossBar = plugin.getServer().createBossBar("Level: " + level, BarColor.GREEN, BarStyle.SOLID);
        bossBar.addPlayer(player);
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public void addExperience(int amount) {
        experience += amount;
        bossBar.setProgress((double) experience / getExperienceRequiredForLevelUp(level));

        // Check if the player has enough experience to level up
        if (experience >= getExperienceRequiredForLevelUp(level)) {
            levelUp();
        }
    }

    private void levelUp() {
        level++;
        bossBar.setTitle("Level: " + level);
        // You can perform additional actions when the player levels up
    }

    private int getExperienceRequiredForLevelUp(int currentLevel) {
        // Define a formula to calculate the experience required for level up
        // This can be a linear or exponential formula based on your design
        return currentLevel * 10; // Example formula: 10 experience points per level
    }
}
