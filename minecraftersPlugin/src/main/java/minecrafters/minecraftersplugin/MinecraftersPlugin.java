package minecrafters.minecraftersplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MinecraftersPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.banIP("localhost");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
