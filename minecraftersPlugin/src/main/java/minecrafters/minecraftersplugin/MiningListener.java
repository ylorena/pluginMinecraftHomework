package minecrafters.minecraftersplugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;

public class MiningListener implements Listener {
    private Plugin plugin;

    PlayerLevelManager playerLevelManager = null;

    public MiningListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        PlayerLevelManager playerLevelManager = getPlayerLevelManager(player);
        playerLevelManager.addExperience(1);
    }

    private PlayerLevelManager getPlayerLevelManager(Player player) {
        if (playerLevelManager == null) {
            playerLevelManager = new PlayerLevelManager(player, plugin);
        }
        return playerLevelManager;
    }
}