package minecrafters.minecraftersplugin;

import java.util.HashMap;
import java.util.Map;

public enum ActivityEnum {
    MINING("Mineração", "BlockBreakEvent"),
    FARMING("Farming", "BlockFertilizeEvent"),
    FISHING("Fishing", "PlayerFishEvent"),
    HUNTING("Hunting", "EntityDeathEvent"),
    WOODCUTTING("Woodcutting", null),
    DIGGING("Digging", null),
    BUILDING("Building", "BlockPlaceEvent"),
    CRAFTING("Crafting", "CraftItemEvent"),
    SMELTING("Smelting", "FurnaceSmeltEvent"),
    ENCHANTING("Enchanting", "EnchantItemEvent"),
    BREWING("Brewing", "BrewEvent"),
    FIGHTING("Fighting", "EntityDamageByEntityEvent"),
    TRADING("Trading", "TradeSelectEvent"),
    EXPLORING("Exploring", "PlayerMoveEvent"),
    FLYING("Flying", "PlayerToggleFlightEvent"),
    ARMORING("Armoring", "PlayerArmorStandManipulateEvent"),
    WEAPONRY("Weaponry", "PlayerInteractEvent"),
    COOKING("Cooking", "FurnaceSmeltEvent"),
    BAKING("Baking", "FurnaceSmeltEvent"),
    BREEDING("Breeding", "EntityBreedEvent"),
    RANCHING("Ranching", "PlayerInteractEntityEvent"),
    SMITHING("Smithing", "PrepareAnvilEvent");

    private final String displayName;
    private final String eventName;
    private static final Map<String, ActivityEnum> lookup = new HashMap<>();

    static {
        for (ActivityEnum activity : ActivityEnum.values()) {
            lookup.put(activity.getDisplayName(), activity);
        }
    }

    ActivityEnum(final String displayName, final String eventName) {
        this.displayName = displayName;
        this.eventName = eventName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEventName() {
        return eventName;
    }

    public static ActivityEnum findByDisplayName(String displayName) {
        return lookup.get(displayName);
    }

    @Override
    public String toString() {
        return displayName;
    }
}