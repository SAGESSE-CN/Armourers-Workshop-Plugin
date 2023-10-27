package moe.plushie.armourers_workshop.plugin.api;

import com.google.common.collect.Lists;
import net.cocoonmc.core.item.ItemStack;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public enum ItemOverrideType {

    SWORD("sword", Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD),
    SHIELD("shield", Material.SHIELD),
    BOW("bow", Material.BOW, Material.CROSSBOW),
    TRIDENT("trident", Material.TRIDENT),

    PICKAXE("pickaxe", Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE),
    AXE("axe", Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE),
    SHOVEL("shovel", Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.DIAMOND_SHOVEL, Material.NETHERITE_SHOVEL),
    HOE("hoe", Material.WOODEN_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.DIAMOND_HOE, Material.NETHERITE_HOE),

    ITEM("item");

    private final String name;
    private final Collection<Material> materials;

    ItemOverrideType(String name, Material... materials) {
        this.name = name;
        this.materials = Lists.newArrayList(materials);
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
        // test by vanilla's item.
        return materials.contains(itemStack.getItem().asMaterial());
    }

    public String getName() {
        return name;
    }
}
