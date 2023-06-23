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

    public void setExperience(int xp){
        this.experience = xp;
    }

    public void addExperience(int amount) {
        experience += amount;
        
        if (experience >= getExperienceRequiredForLevelUp(level)) {
            levelUp();
            setExperience(0);
        }
        bossBar.setProgress((double) experience / getExperienceRequiredForLevelUp(level));
    }

    private void levelUp() {
        level++;
        bossBar.setTitle("Level: " + level);
    }

    private int getExperienceRequiredForLevelUp(int currentLevel) {
        return currentLevel * 10; // Example formula: 10 experience points per level
    }
}