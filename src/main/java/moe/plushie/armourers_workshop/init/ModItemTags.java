package moe.plushie.armourers_workshop.init;

import moe.plushie.armourers_workshop.api.IItemTag;
import moe.plushie.armourers_workshop.api.registry.IRegistryKey;
import net.cocoonmc.core.item.Item;
import net.cocoonmc.core.item.Items;
import net.cocoonmc.core.resources.ResourceLocation;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * different the mod side, we can't add and check the item tags.
 * but plugin side there won't be new items, so we only mark fixed items.
 */
public class ModItemTags {

    public static final IRegistryKey<IItemTag> SWORDS = skinnable().of(Items.WOODEN_SWORD, Items.STONE_SWORD, Items.IRON_SWORD, Items.GOLDEN_SWORD, Items.DIAMOND_SWORD, Items.NETHERITE_SWORD).build("swords");
    public static final IRegistryKey<IItemTag> SHIELDS = skinnable().of(Items.SHIELD).build("shields");
    public static final IRegistryKey<IItemTag> BOWS = skinnable().of(Items.BOW, Items.CROSSBOW).build("bows");
    public static final IRegistryKey<IItemTag> TRIDENTS = skinnable().of(Items.TRIDENT).build("tridents");

    public static final IRegistryKey<IItemTag> PICKAXES = skinnable().of(Items.WOODEN_PICKAXE, Items.STONE_PICKAXE, Items.IRON_PICKAXE, Items.GOLDEN_PICKAXE, Items.DIAMOND_PICKAXE, Items.NETHERITE_PICKAXE).build("pickaxes");
    public static final IRegistryKey<IItemTag> AXES = skinnable().of(Items.WOODEN_AXE, Items.STONE_AXE, Items.IRON_AXE, Items.GOLDEN_AXE, Items.DIAMOND_AXE, Items.NETHERITE_AXE).build("axes");
    public static final IRegistryKey<IItemTag> SHOVELS = skinnable().of(Items.WOODEN_SHOVEL, Items.STONE_SHOVEL, Items.IRON_SHOVEL, Items.GOLDEN_SHOVEL, Items.DIAMOND_SHOVEL, Items.NETHERITE_SHOVEL).build("shovels");
    public static final IRegistryKey<IItemTag> HOES = skinnable().of(Items.WOODEN_HOE, Items.STONE_HOE, Items.IRON_HOE, Items.GOLDEN_HOE, Items.DIAMOND_HOE, Items.NETHERITE_HOE).build("hoes");

    private static ItemTagBuilder skinnable() {
        return new ItemTagBuilder();
    }

    public static void init() {
    }

    private static class ItemTagBuilder {

        private Collection<Item> items = Collections.emptyList();

        public ItemTagBuilder of(Item... items) {
            this.items = Arrays.asList(items);
            return this;
        }

        public IRegistryKey<IItemTag> build(String name) {
            Collection<Item> includes = items;
            ResourceLocation registryName = ModConstants.key("skinnable/" + name);
            ModLog.debug("Registering Item Tag '{}'", registryName);
            IItemTag tag = it -> includes.contains(it.getItem());
            return new IRegistryKey<IItemTag>() {
                @Override
                public ResourceLocation getRegistryName() {
                    return registryName;
                }

                @Override
                public IItemTag get() {
                    return tag;
                }
            };
        }
    }
}
