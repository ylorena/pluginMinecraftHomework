package minecrafters.minecraftersplugin;

public enum LevelNamesEnum {
    MINING("Mineração"),
    FARMING("Farming"),
    FISHING("Fishing"),
    HUNTING("Hunting"),
    WOODCUTTING("Woodcutting"),
    DIGGING("Digging"),
    BUILDING("Building"),
    CRAFTING("Crafting"),
    SMELTING("Smelting"),
    ENCHANTING("Enchanting"),
    BREWING("Brewing"),
    FIGHTING("Fighting"),
    TRADING("Trading"),
    EXPLORING("Exploring"),
    FLYING("Flying"),
    ARMORING("Armoring"),
    WEAPONRY("Weaponry"),
    COOKING("Cooking"),
    BAKING("Baking"),
    BREEDING("Breeding"),
    RANCHING("Ranching"),
    SMITHING("Smithing");

    private final String text;

    LevelNamesEnum(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}