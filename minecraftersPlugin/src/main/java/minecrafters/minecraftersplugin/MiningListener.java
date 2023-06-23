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
        PlayerLevelManager playerLevelManager = getPlayerLevelManager(player, "Mineração");
        playerLevelManager.addExperience(1);
    }

    private PlayerLevelManager getPlayerLevelManager(Player player, String tipo) {
        if (this.playerLevelManager == null) {
            this.playerLevelManager = new PlayerLevelManager(player, plugin, tipo);
        }

        return this.playerLevelManager;
    }
}