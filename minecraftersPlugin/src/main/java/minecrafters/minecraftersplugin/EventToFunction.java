package minecrafters.minecraftersplugin;

import org.bukkit.Bukkit;

public class EventToFunction {

    private Runnable[] eventToFunctions = new Runnable[] {
        this::test,
        this::test,
        this::test,
        this::test,
        this::test,
        this::test
    };

    public void test() {
        Bukkit.getServer().broadcastMessage("Test");
    }

    public void eventToFunction(EventNamesEnum levelName) {
        eventToFunctions[levelName.ordinal()].run();
    }
}