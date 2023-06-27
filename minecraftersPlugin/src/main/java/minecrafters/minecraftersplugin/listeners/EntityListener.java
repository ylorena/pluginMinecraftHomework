package minecrafters.minecraftersplugin.listeners;

import minecrafters.minecraftersplugin.PlayerLevelManager;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityListener implements Listener {
    private final Plugin plugin;
    private final Map<UUID, PlayerLevelManager> playerLevelManagerMap;

    public EntityListener(Plugin plugin) {
        this.plugin = plugin;
        this.playerLevelManagerMap = new HashMap<>();
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Monster) {
            Monster monsterEnt = (Monster) event.getEntity();
            Player player = monsterEnt.getKiller();
            if (player == null) {
                return;
            }

            getPlayerLevelManager(player, "kills").addExperience(5);
        }
    }

    private PlayerLevelManager getPlayerLevelManager(Player player, String tipo) {
        UUID playerId = player.getUniqueId();
        PlayerLevelManager playerLevelManager = playerLevelManagerMap.get(playerId);
        if (playerLevelManager == null) {
            playerLevelManager = new PlayerLevelManager(player, plugin, tipo);
            playerLevelManagerMap.put(playerId, playerLevelManager);
        } else if (!playerLevelManager.getTipo().equals(tipo)) {
            playerLevelManager = new PlayerLevelManager(player, plugin, tipo);
            playerLevelManagerMap.put(playerId, playerLevelManager);
        }

        return playerLevelManager;
    }
}
