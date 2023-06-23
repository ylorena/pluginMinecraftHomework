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
    private String tipo;

    public PlayerLevelManager(Player player, Plugin plugin, String tipo) {
        this.level = 1;
        this.experience = 0;
        this.tipo = tipo;
        this.bossBar = plugin.getServer().createBossBar("Level de: " +tipo+" : "+ level, BarColor.YELLOW, BarStyle.SEGMENTED_6);
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

    bossBar.setVisible(false);
    }

    private int getExperienceRequiredForLevelUp(int currentLevel) {
        return currentLevel * 10; // Example formula: 10 experience points per level
    }

}