package minecrafters.minecraftersplugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class MinecraftersPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new MiningListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityListener(this), this);
    }
    @Override
    public void onDisable() {
    }
}