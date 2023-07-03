import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

public class FishingListener implements Listener {

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            ItemStack caughtFish = event.getCaught();
            if (caughtFish != null && caughtFish.getType() == Material.RAW_FISH) {
                player.getInventory().addItem(new ItemStack(Material.SALMON, 1));
                player.getInventory().addItem(new ItemStack(Material.PUFFERFISH, 1));
            }
        }
    }
}