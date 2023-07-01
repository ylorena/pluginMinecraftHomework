package minecrafters.minecraftersplugin;

import java.util.HashMap;
import java.util.Map;

public enum EventNamesEnum {
    MINING("BlockBreakEvent"),
    FLYING("PlayerToggleFlightEvent"),
    CRAFTING("CraftItemEvent"),
    FISHING("PlayerFishEvent"),
    HUNTING("EntityDeathEvent"),
    FARMING("BlockFertilizeEvent"),
    BUILDING("BlockPlaceEvent"),
    SMELTING("FurnaceSmeltEvent"),
    ENCHANTING("EnchantItemEvent"),
    BREWING("BrewEvent"),
    FIGHTING("EntityDamageByEntityEvent"),
    TRADING("TradeSelectEvent"),
    EXPLORING("PlayerMoveEvent"),
    MAGIC("PlayerInteractEvent"), // This is a placeholder, as there isn't a direct equivalent
    TINKERING("InventoryClickEvent"), // This is a placeholder, as there isn't a direct equivalent
    ARMORING("PlayerArmorStandManipulateEvent"),
    WEAPONRY("PlayerInteractEvent"), // This is a placeholder, as there isn't a direct equivalent
    COOKING("FurnaceSmeltEvent"),
    BAKING("FurnaceSmeltEvent"),
    BREEDING("EntityBreedEvent"),
    RANCHING("PlayerInteractEntityEvent"),
    SMITHING("PrepareAnvilEvent");

    private final String eventName;
    private static final Map<String, EventNamesEnum> lookup = new HashMap<>();

    static {
        for (EventNamesEnum event : EventNamesEnum.values()) {
            lookup.put(event.getEventName(), event);
        }
    }

    public String getEventName() {
        return eventName;
    }

    public static EventNamesEnum findByEventName(String eventName) {
        return lookup.get(eventName);
    }

    EventNamesEnum(final String eventName) {
        this.eventName = eventName;
    }

    @Override
    public String toString() {
        return eventName;
    }
}