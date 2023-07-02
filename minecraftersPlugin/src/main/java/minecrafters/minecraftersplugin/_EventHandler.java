package minecrafters.minecraftersplugin;

import org.bukkit.event.Event;

public class _EventHandler {

    public _EventHandler() {
    }

    public void handleEvent(Event event) {
        // UpdateLevelEvent();
        EventToFunction _eventToFunction = new EventToFunction();
        _eventToFunction.eventToFunction(EventNamesEnum.findByEventName(event.getEventName()));
    }
}