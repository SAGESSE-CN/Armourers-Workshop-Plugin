package moe.plushie.armourers_workshop.api;

import moe.plushie.armourers_workshop.api.registry.IRegistryKey;
import moe.plushie.armourers_workshop.init.ModConfig;
import moe.plushie.armourers_workshop.init.ModItemMatchers;
import moe.plushie.armourers_workshop.init.ModItemTags;
import moe.plushie.armourers_workshop.utils.ItemMatcher;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public enum ItemOverrideType {

    SWORD("sword", ModItemTags.SWORDS, ModItemMatchers.SWORDS),
    SHIELD("shield", ModItemTags.SHIELDS, ModItemMatchers.SHIELDS),
    BOW("bow", ModItemTags.BOWS, ModItemMatchers.BOWS),
    TRIDENT("trident", ModItemTags.TRIDENTS, ModItemMatchers.TRIDENTS),

    PICKAXE("pickaxe", ModItemTags.PICKAXES, ModItemMatchers.PICKAXES),
    AXE("axe", ModItemTags.AXES, ModItemMatchers.AXES),
    SHOVEL("shovel", ModItemTags.SHOVELS, ModItemMatchers.SHOVELS),
    HOE("hoe", ModItemTags.HOES, ModItemMatchers.HOES),

    ITEM("item", null, null);

    private final IRegistryKey<IItemTag> tag;
    private final String name;
    private final ItemMatcher matcher;

    ItemOverrideType(String name, IRegistryKey<IItemTag> tag, ItemMatcher matcher) {
        this.name = name;
        this.tag = tag;
        this.matcher = matcher;
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

    public boolean isOverrideItem(ItemStack itemStack) {
        // yep, the item skin override all item stack.
        if (this == ITEM) {
            return true;
        }
        // test by overrides of the config system.
        ResourceLocation registryName = itemStack.getItem().getRegistryName();
        if (ModConfig.Common.overrides.contains(name + ":" + registryName)) {
            return true;
        }
        // test by vanilla's tag system.
        if (tag != null && tag.get().contains(itemStack)) {
            return true;
        }
        // test by item id matching system.
        return matcher.test(registryName, itemStack);
    }

    public String getName() {
        return name;
    }
}
