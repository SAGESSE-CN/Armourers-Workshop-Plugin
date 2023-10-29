package moe.plushie.armourers_workshop.plugin.core.skin;

import moe.plushie.armourers_workshop.plugin.ArmourersWorkshop;
import net.cocoonmc.core.inventory.Container;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.nbt.ListTag;
import net.cocoonmc.core.nbt.ShortTag;
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
        ListTag value = ListTag.newInstance();
        slots.forEach((slotType, count) -> {
            int index = slotType.getId() & 0xff;
            int encoded = index << 8 | count & 0xff;
            value.add(ShortTag.valueOf((short) encoded));
        });
        if (value.size() != 0) {
            nbt.put("Slots", value);
        }
    }

    public static void loadSkinSlots(HashMap<SkinSlotType, Integer> slots, CompoundTag nbt) {
        ListTag value = nbt.getList("Slots", 2);
        if (value == null || value.size() == 0) {
            return;
        }
        for (int i = 0; i < value.size(); ++i) {
            short encoded = ((ShortTag) value.get(i)).getAsShort();
            SkinSlotType slotType = SkinSlotType.by((encoded >> 8) & 0xff);
            if (slotType != null) {
                slots.put(slotType, encoded & 0xff);
            }
        }
    }

    public static void saveInventoryItems(Container inventory, CompoundTag nbt) {
        ListTag listTag = ListTag.newInstance();
        for (int i = 0; i < inventory.getContainerSize(); ++i) {
            ItemStack itemStack = inventory.getItem(i);
            if (!itemStack.isEmpty()) {
                CompoundTag compoundTag2 = CompoundTag.newInstance();
                compoundTag2.putByte("Slot", (byte) i);
                itemStack.save(compoundTag2);
                listTag.add(compoundTag2);
            }
        }
        if (listTag.size() != 0) {
            nbt.put("Items", listTag);
        }
    }

    public static void loadInventoryItems(Container inventory, CompoundTag nbt) {
        ListTag listTag = nbt.getList("Items", 10);
        if (listTag == null || listTag.size() == 0) {
            return;
        }
        for (int i = 0; i < listTag.size(); ++i) {
            CompoundTag compoundTag2 = (CompoundTag) listTag.get(i);
            int j = compoundTag2.getByte("Slot") & 255;
            if (j < inventory.getContainerSize()) {
                inventory.setItem(j, new ItemStack(compoundTag2));
            }
        }
    }
}
