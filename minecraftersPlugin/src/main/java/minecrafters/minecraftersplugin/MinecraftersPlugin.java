package minecrafters.minecraftersplugin;

import minecrafters.minecraftersplugin.listeners.GenericListener;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class MinecraftersPlugin extends JavaPlugin {

    private Plugin plugin = null;

    @Override
    public void onEnable() {
        this.plugin = this;
        registerGenericEventHandler(PlayerJoinEvent.class, this, new GenericListener());
        registerGenericEventHandler(BlockBreakEvent.class, this, new GenericListener());
        registerGenericEventHandler(EntityDeathEvent.class, this, new GenericListener());
    }

    @Override
    public void onDisable() {
    }

    public <T extends Event> void registerGenericEventHandler(Class<T> eventClass, Plugin plugin, Listener listener) {
        EventExecutor eventExecutor = new EventExecutor() {
            @Override
            public void execute(Listener listener, Event event) throws EventException {
                LevelHandler.getLevelHandler(plugin).handleUserLevel(event);
            }
        };

        plugin.getServer().getPluginManager().registerEvent(
                eventClass,
                listener,
                EventPriority.NORMAL,
                eventExecutor,
                plugin);
    }
}