package minecrafters.minecraftersplugin;

import org.bukkit.Bukkit;

public class EventToFunction {
    public void test() {
        Bukkit.getServer().broadcastMessage("Test");
    }

    private IEventToFunction[] eventToFunctions = new IEventToFunction[] {
            new IEventToFunction() {
                public void call() {
                    test();
                }
            },
    };

    public void eventToFunction(LevelNamesEnum levelName) {
        eventToFunctions[0].call();
    }
}