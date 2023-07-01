package minecrafters.minecraftersplugin;

public class EventToFunction {
    private IEventToFunction[] eventToFunctions = new IEventToFunction[] {
            new IEventToFunction() {
                public void call() {
                    LevelController.getLevelController().updateBossBar();
                }
            },
    };

    public void eventToFunction(LevelNamesEnum levelName) {
        eventToFunctions[0].call();
    }
}