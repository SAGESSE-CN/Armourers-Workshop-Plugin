package moe.plushie.armourers_workshop.plugin.core.skin;

import moe.plushie.armourers_workshop.plugin.api.skin.ISkinType;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Predicate;

public enum SkinSlotType {

    HEAD(0, 0, 10, "head", SkinTypes.ARMOR_HEAD),
    CHEST(1, 10, 10, "chest", SkinTypes.ARMOR_CHEST),
    LEGS(2, 20, 10, "legs", SkinTypes.ARMOR_LEGS),
    FEET(3, 30, 10, "feet", SkinTypes.ARMOR_FEET),
    WINGS(4, 40, 10, "wings", SkinTypes.ARMOR_WINGS),

    SWORD(5, 50, 1, "sword", SkinTypes.ITEM_SWORD),
    SHIELD(6, 51, 1, "shield", SkinTypes.ITEM_SHIELD),
    BOW(7, 52, 1, "bow", SkinTypes.ITEM_BOW),
    TRIDENT(14, 57, 1, "trident", SkinTypes.ITEM_TRIDENT),

    PICKAXE(8, 53, 1, "pickaxe", SkinTypes.TOOL_PICKAXE),
    AXE(9, 54, 1, "axe", SkinTypes.TOOL_AXE),
    SHOVEL(10, 55, 1, "shovel", SkinTypes.TOOL_SHOVEL),
    HOE(11, 56, 1, "hoe", SkinTypes.TOOL_HOE),

    OUTFIT(12, 70, 10, "outfit", SkinTypes.OUTFIT),
    DYE(13, 80, 16, "dye", null);

    private final String name;
    private final int id;
    private final int index;
    private final int size;
    private final ISkinType skinType;

    SkinSlotType(int id, int index, int size, String name, ISkinType skinType) {
        this.id = id;
        this.name = name;
        this.index = index;
        this.size = size;
        this.skinType = skinType;
        Helper.totalSize = Math.max(Helper.totalSize, index + size);
    }

    @Nullable
    public static SkinSlotType by(int id) {
        return find(type -> type.id == id);
    }

    @Nullable
    public static SkinSlotType of(String name) {
        return find(type -> Objects.equals(type.name, name));
    }

    @Nullable
    public static SkinSlotType of(ISkinType skinType) {
        return find(type -> Objects.equals(type.skinType, skinType));
    }

//    @Nullable
//    public static SkinSlotType of(ItemStack itemStack) {
//        if (itemStack.isEmpty()) {
//            return null;
//        }
//        if (itemStack.getItem() instanceof BottleItem) {
//            return DYE;
//        }
//        SkinDescriptor descriptor = SkinDescriptor.of(itemStack);
//        if (!descriptor.isEmpty()) {
//            return of(descriptor.getType());
//        }
//        return null;
//    }

    public static int getMaxSlotSize() {
        return 10;
    }

    public static int getTotalSize() {
        return Helper.totalSize;
    }

//    public static ISkinPaintType[] getSupportedPaintTypes() {
//        return Helper.SLOT_TO_TYPES;
//    }
//
//    public static int getDyeSlotIndex(ISkinPaintType paintType) {
//        int i = 0;
//        for (; i < Helper.SLOT_TO_TYPES.length; ++i) {
//            if (Helper.SLOT_TO_TYPES[i] == paintType) {
//                break;
//            }
//        }
//        return DYE.getIndex() + i;
//    }

    private static SkinSlotType find(Predicate<SkinSlotType> predicate) {
        for (SkinSlotType slotType : SkinSlotType.values()) {
            if (predicate.test(slotType)) {
                return slotType;
            }
        }
        return null;
    }

//    public ResourceLocation getIconSprite() {
//        return ModConstants.key("item/slot/" + name);
//    }

    public boolean isResizable() {
        return this != DYE && size > 1;
    }

//    public boolean isArmor() {
//        return skinType instanceof ISkinArmorType;
//    }

    public int getId() {
        return id;
    }

    public int getIndex() {
        return index;
    }

    public int getMaxSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public ISkinType getSkinType() {
        return skinType;
    }

    private static class Helper {
//        static final ISkinPaintType[] SLOT_TO_TYPES = {
//                SkinPaintTypes.DYE_1,
//                SkinPaintTypes.DYE_2,
//                SkinPaintTypes.DYE_3,
//                SkinPaintTypes.DYE_4,
//                SkinPaintTypes.DYE_5,
//                SkinPaintTypes.DYE_6,
//                SkinPaintTypes.DYE_7,
//                SkinPaintTypes.DYE_8,
//                SkinPaintTypes.SKIN,
//                SkinPaintTypes.HAIR,
//                SkinPaintTypes.EYES,
//                SkinPaintTypes.MISC_1,
//                SkinPaintTypes.MISC_2,
//                SkinPaintTypes.MISC_3,
//                SkinPaintTypes.MISC_4
//        };
        static int totalSize = 0;
    }
}