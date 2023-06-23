package minecrafters.minecraftersplugin;

import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

public class EntityListener implements Listener {

    private Plugin plugin;
    PlayerLevelManager playerLevelManager = null;

    public EntityListener(Plugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        if(event.getEntity() instanceof Monster){
            Monster monsterEnt = (Monster) event.getEntity();
            Player player = monsterEnt.getKiller();
            if(player == null){
                return;
            }
            PlayerLevelManager level = getPlayerLevelManager(player, "kills");
            level.addExperience(5);
        }
    }

    private PlayerLevelManager getPlayerLevelManager(Player player, String tipo) {
        if (this.playerLevelManager == null) {
            this.playerLevelManager = new PlayerLevelManager(player, plugin, tipo);
        }
        return this.playerLevelManager;
    }


}
