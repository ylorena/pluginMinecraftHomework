package minecrafters.minecraftersplugin;

import minecrafters.minecraftersplugin.listeners.GenericListener;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class MinecraftersPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
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
                System.out.println(event.getEventName());
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