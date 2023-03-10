package moe.plushie.armourers_workshop.plugin.core.skin;

import moe.plushie.armourers_workshop.plugin.ArmourersWorkshop;
import moe.plushie.armourers_workshop.plugin.api.ItemStack;
import moe.plushie.armourers_workshop.plugin.api.NonNullList;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.ShortTag;
import org.bukkit.NamespacedKey;

import java.util.BitSet;
import java.util.HashMap;

public class SkinWardrobeStorage {

    private static NamespacedKey SKIN_WARDROBE_KEY;

    public static NamespacedKey getKey() {
        if (SKIN_WARDROBE_KEY == null) {
            SKIN_WARDROBE_KEY = new NamespacedKey(ArmourersWorkshop.INSTANCE, "wardrobe");
        }
        return SKIN_WARDROBE_KEY;
    }

    public static void saveDataFixer(SkinWardrobe wardrobe, CompoundTag nbt) {
        nbt.putByte("DataVersion", (byte) 1);
    }

    public static void loadDataFixer(SkinWardrobe wardrobe, CompoundTag nbt) {
    }

    public static void saveFlags(BitSet flags, CompoundTag nbt) {
        int value = 0;
        for (int i = 0; i < 32; ++i) {
            if (flags.get(i)) {
                value |= 1 << i;
            }
        }
        if (value != 0) {
            nbt.putInt("Visibility", value);
        }
    }

    public static void loadFlags(BitSet flags, CompoundTag nbt) {
        int value = nbt.getInt("Visibility");
        flags.clear();
        for (int i = 0; i < 32; ++i) {
            int mask = 1 << i;
            if ((value & mask) != 0) {
                flags.set(i);
            }
        }
    }

    public static void saveSkinSlots(HashMap<SkinSlotType, Integer> slots, CompoundTag nbt) {
        if (slots.isEmpty()) {
            return;
        }
        ListTag<ShortTag> value = new ListTag<>(ShortTag.class);
        slots.forEach((slotType, count) -> {
            int index = slotType.getId() & 0xff;
            int encoded = index << 8 | count & 0xff;
            value.add(new ShortTag((short) encoded));
        });
        if (value.size() != 0) {
            nbt.put("Slots", value);
        }
    }

    public static void loadSkinSlots(HashMap<SkinSlotType, Integer> slots, CompoundTag nbt) {
        ListTag<?> value = nbt.getListTag("Slots");
        if (value == null || value.size() == 0) {
            return;
        }
        for (int i = 0; i < value.size(); ++i) {
            short encoded = ((ShortTag) value.get(i)).asShort();
            SkinSlotType slotType = SkinSlotType.by((encoded >> 8) & 0xff);
            if (slotType != null) {
                slots.put(slotType, encoded & 0xff);
            }
        }
    }

    public static void saveInventoryItems(NonNullList<ItemStack> inventory, CompoundTag nbt) {
        ListTag<CompoundTag> listTag = new ListTag<>(CompoundTag.class);
        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack itemStack = inventory.get(i);
            if (!itemStack.isEmpty()) {
                CompoundTag compoundTag2 = new CompoundTag();
                compoundTag2.putByte("Slot", (byte) i);
                itemStack.save(compoundTag2);
                listTag.add(compoundTag2);
            }
        }
        if (listTag.size() != 0) {
            nbt.put("Items", listTag);
        }
    }

    public static void loadInventoryItems(NonNullList<ItemStack> inventory, CompoundTag nbt) {
        ListTag<?> listTag = nbt.getListTag("Items");
        if (listTag == null || listTag.size() == 0) {
            return;
        }
        for (int i = 0; i < listTag.size(); ++i) {
            CompoundTag compoundTag2 = (CompoundTag) listTag.get(i);
            int j = compoundTag2.getByte("Slot") & 255;
            if (j < inventory.size()) {
                inventory.set(j, new ItemStack(compoundTag2));
            }
        }
    }
}
