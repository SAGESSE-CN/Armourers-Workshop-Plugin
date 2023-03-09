package moe.plushie.armourers_workshop.plugin.api;

import org.jetbrains.annotations.Nullable;

public enum ItemOverrideType {

    SWORD("sword"),
    SHIELD("shield"),
    BOW("bow"),
    TRIDENT("trident"),

    PICKAXE("pickaxe"),
    AXE("axe"),
    SHOVEL("shovel"),
    HOE("hoe"),

    ITEM("item");

    private final String name;

    ItemOverrideType(String name) {
        this.name = name;
    }

    @Nullable
    public static ItemOverrideType of(String name) {
        for (ItemOverrideType overrideType : ItemOverrideType.values()) {
            if (overrideType.getName().equals(name)) {
                return overrideType;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }
}
