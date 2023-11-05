package moe.plushie.armourers_workshop.init;

import moe.plushie.armourers_workshop.core.item.BottleItem;
import moe.plushie.armourers_workshop.core.item.SkinItem;
import moe.plushie.armourers_workshop.core.item.SkinUnlockItem;
import moe.plushie.armourers_workshop.core.item.WandOfStyleItem;
import moe.plushie.armourers_workshop.core.skin.SkinSlotType;
import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.item.BlockItem;
import net.cocoonmc.core.item.Item;
import net.cocoonmc.core.resources.ResourceLocation;

import java.util.function.Function;

@SuppressWarnings("unused")
public class ModItems {

    public static final Item SKIN = skin(ModBlocks.SKINNABLE).build("skin");

    public static final Item SKIN_LIBRARY = block(ModBlocks.SKIN_LIBRARY).build("skin-library");
    public static final Item SKIN_LIBRARY_CREATIVE = block(ModBlocks.SKIN_LIBRARY_CREATIVE).build("skin-library-creative");
    public static final Item SKIN_LIBRARY_GLOBAL = block(ModBlocks.SKIN_LIBRARY_GLOBAL).build("skin-library-global");

    public static final Item SKINNING_TABLE = block(ModBlocks.SKINNING_TABLE).build("skinning-table");
    public static final Item DYE_TABLE = block(ModBlocks.DYE_TABLE).build("dye-table");
    //public static final Item OUTFIT_MAKER = block(ModBlocks.OUTFIT_MAKER).build("outfit-maker");
    public static final Item HOLOGRAM_PROJECTOR = block(ModBlocks.HOLOGRAM_PROJECTOR).build("hologram-projector");

    public static final Item BOTTLE = normal(BottleItem::new).build("dye-bottle");
    //public static final Item MANNEQUIN_TOOL = normal(MannequinToolItem::new).build("mannequin-tool");
    //public static final Item ARMOURERS_HAMMER = normal(ArmourersHammerItem::new).build("armourers-hammer");
    public static final Item WAND_OF_STYLE = normal(WandOfStyleItem::new).build("wand-of-style");

    public static final Item SKIN_UNLOCK_HEAD = unlock(SkinSlotType.HEAD).build("skin-unlock-head");
    public static final Item SKIN_UNLOCK_CHEST = unlock(SkinSlotType.CHEST).build("skin-unlock-chest");
    public static final Item SKIN_UNLOCK_FEET = unlock(SkinSlotType.FEET).build("skin-unlock-feet");
    public static final Item SKIN_UNLOCK_LEGS = unlock(SkinSlotType.LEGS).build("skin-unlock-legs");
    public static final Item SKIN_UNLOCK_WINGS = unlock(SkinSlotType.WINGS).build("skin-unlock-wings");
    public static final Item SKIN_UNLOCK_OUTFIT = unlock(SkinSlotType.OUTFIT).build("skin-unlock-outfit");

    public static final Item LINKING_TOOL = normal(Item::new).build("linking-tool");
    public static final Item SKIN_TEMPLATE = normal(Item::new).stacksTo(64).build("skin-template");
    public static final Item SOAP = normal(Item::new).stacksTo(64).build("soap");
    public static final Item GIFT_SACK = normal(Item::new).stacksTo(64).build("gift-sack");

    //public static final Item ARMOURER = block(ModBlocks.ARMOURER).rarity(Rarity.EPIC).build("armourer");
    public static final Item COLOR_MIXER = block(ModBlocks.COLOR_MIXER).build("colour-mixer");
    //public static final Item ADVANCED_SKIN_BUILDER = block(ModBlocks.ADVANCED_SKIN_BUILDER).build("advanced-skin-builder");

    //public static final Item SKIN_CUBE = cube(ModBlocks.SKIN_CUBE).build("skin-cube");
    //public static final Item SKIN_CUBE_GLOWING = cube(ModBlocks.SKIN_CUBE_GLOWING).build("skin-cube-glowing");
    //public static final Item SKIN_CUBE_GLASS = cube(ModBlocks.SKIN_CUBE_GLASS).build("skin-cube-glass");
    //public static final Item SKIN_CUBE_GLASS_GLOWING = cube(ModBlocks.SKIN_CUBE_GLASS_GLOWING).build("skin-cube-glass-glowing");

    //public static final Item PAINT_BRUSH = normal(PaintbrushItem::new).build("paintbrush");
    //public static final Item PAINT_ROLLER = normal(PaintRollerItem::new).build("paint-roller");
    //public static final Item BURN_TOOL = normal(BurnToolItem::new).build("burn-tool");
    //public static final Item DODGE_TOOL = normal(DodgeToolItem::new).build("dodge-tool");
    //public static final Item SHADE_NOISE_TOOL = normal(ShadeNoiseToolItem::new).build("shade-noise-tool");
    //public static final Item COLOR_NOISE_TOOL = normal(ColorNoiseToolItem::new).build("colour-noise-tool");
    //public static final Item BLENDING_TOOL = normal(BlendingToolItem::new).build("blending-tool");
    //public static final Item HUE_TOOL = normal(HueToolItem::new).build("hue-tool");
    //public static final Item COLOR_PICKER = normal(ColorPickerItem::new).build("colour-picker");
    //public static final Item BLOCK_MARKER = normal(BlockMarkerItem::new).build("block-marker");

    public static void init() {
    }

    private static Builder normal(Function<Item.Properties, Item> factory) {
        return new Builder(factory).stacksTo(1);
    }

    private static Builder block(Block block) {
        return normal(properties -> new BlockItem(block, properties)).stacksTo(64);
    }

    private static Builder skin(Block block) {
        return normal(properties -> new SkinItem(block, properties));
    }

    private static Builder unlock(SkinSlotType slotType) {
        return normal(properties -> new SkinUnlockItem(slotType, properties)).stacksTo(16);
    }

    private static class Builder {

        final Item.Properties properties;
        final Function<Item.Properties, Item> factory;

        public Builder(Function<Item.Properties, Item> factory) {
            this.factory = factory;
            this.properties = new Item.Properties();
        }

        public Builder stacksTo(int i) {
            properties.stacksTo(i);
            return this;
        }

        public Item build(String name) {
            ResourceLocation key = new ResourceLocation("armourers_workshop", name);
            Item item = factory.apply(properties);
            ModLog.debug("Registering Item '{}'", key);
            return Item.register(key, item);
        }
    }
}
