package minecrafters.minecraftersplugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GenericListener implements Listener {

    public GenericListener() {}

    @EventHandler
    public void onEvent(Event event) {
        Bukkit.getServer().broadcastMessage(event.getEventName());
    }
}