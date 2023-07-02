package minecrafters.minecraftersplugin;

import minecrafters.minecraftersplugin.listeners.GenericListener;

import java.beans.EventHandler;
import java.util.logging.Handler;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class MinecraftersPlugin extends JavaPlugin {

<<<<<<< HEAD
    private LevelController levelController;

    @Override
    public void onEnable() {
        levelController = LevelController.getLevelController();
=======
    private _EventHandler eventHandler = null;
    private Plugin plugin = null;

    @Override
    public void onEnable() {
        this.plugin = this;
        eventHandler = new _EventHandler();
>>>>>>> 038fada99ca50f5070b6a2ead1d7fa260ad29be3
        registerGenericEventHandler(BlockBreakEvent.class, this, new GenericListener());
        registerGenericEventHandler(PlayerJoinEvent.class, this, new GenericListener());
    }

    @Override
    public void onDisable() {
    }

    public <T extends Event> void registerGenericEventHandler(Class<T> eventClass, Plugin plugin, Listener listener) {
        EventExecutor eventExecutor = new EventExecutor() {
            @Override
            public void execute(Listener listener, Event event) throws EventException {
<<<<<<< HEAD
            if (event instanceof BlockBreakEvent) {
                BlockBreakEvent blockBreakEvent = (BlockBreakEvent) event;
                updateBossBar(blockBreakEvent.getPlayer().getUniqueId());
=======
                LevelHandler.getLevelHandler(plugin).handleUserLevel(event);
                eventHandler.handleEvent(event);
>>>>>>> 038fada99ca50f5070b6a2ead1d7fa260ad29be3
            }
        }
        };

        plugin.getServer().getPluginManager().registerEvent(
                eventClass,
                listener,
                EventPriority.NORMAL,
                eventExecutor,
                plugin);
    }

    private void updateBossBar(UUID playerId) {
        
        LevelController.getLevelController().updateBossBar();
    }
}