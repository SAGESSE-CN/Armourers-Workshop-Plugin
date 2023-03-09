package moe.plushie.armourers_workshop.plugin.core.skin;

import moe.plushie.armourers_workshop.plugin.api.skin.ISkinType;
import moe.plushie.armourers_workshop.plugin.api.ITagRepresentable;
import moe.plushie.armourers_workshop.plugin.api.ItemStack;
import moe.plushie.armourers_workshop.plugin.api.NonNullList;
import moe.plushie.armourers_workshop.plugin.network.NetworkManager;
import moe.plushie.armourers_workshop.plugin.network.UpdateWardrobePacket;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.ShortTag;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

import java.lang.ref.WeakReference;
import java.util.BitSet;
import java.util.HashMap;

public class SkinWardrobe implements ITagRepresentable<CompoundTag> {

    private static final int[] ES_TO_FLAGS = {0, 5, 1, 2, 3, 4};

    private final BitSet flags = new BitSet(6);

    private final HashMap<SkinSlotType, Integer> skinSlots = new HashMap<>();

    private final NonNullList<ItemStack> inventory = NonNullList.withSize(SkinSlotType.getTotalSize(), ItemStack.EMPTY);


    private final int id;
    private final WeakReference<Entity> entity;

    public SkinWardrobe(Entity entity) {
        this.id = entity.getEntityId();
        this.entity = new WeakReference<>(entity);
    }


    public void broadcast() {
        NetworkManager.sendToTracking(UpdateWardrobePacket.sync(this), getEntity());
    }

    public void broadcast(Player player) {
        NetworkManager.sendTo(UpdateWardrobePacket.sync(this), player);
    }


    public boolean shouldRenderEquipment(EquipmentSlot slotType) {
        return !flags.get(ES_TO_FLAGS[slotType.ordinal()]);
    }

    public void setRenderEquipment(EquipmentSlot slotType, boolean enable) {
        if (enable) {
            flags.clear(ES_TO_FLAGS[slotType.ordinal()]);
        } else {
            flags.set(ES_TO_FLAGS[slotType.ordinal()]);
        }
    }

    public boolean shouldRenderExtra() {
        return !flags.get(6);
    }

    public void setRenderExtra(boolean value) {
        if (value) {
            flags.clear(6);
        } else {
            flags.set(6);
        }
    }

    public int getFreeSlot(SkinSlotType slotType) {
        int unlockedSize = getUnlockedSize(slotType);
        for (int i = 0; i < unlockedSize; ++i) {
            if (inventory.get(slotType.getIndex() + i).isEmpty()) {
                return i;
            }
        }
        return unlockedSize - 1;
    }

    public ItemStack getItem(SkinSlotType slotType, int slot) {
        if (slot >= getUnlockedSize(slotType)) {
            return ItemStack.EMPTY;
        }
        return inventory.get(slotType.getIndex() + slot);
    }

    public void setItem(SkinSlotType slotType, int slot, ItemStack itemStack) {
        if (slot >= getUnlockedSize(slotType)) {
            return;
        }
        inventory.set(slotType.getIndex() + slot, itemStack);
    }

    public int getUnlockedSize(SkinSlotType slotType) {
        if (slotType == SkinSlotType.DYE) {
            return 8;
        }
        Integer modifiedSize = skinSlots.get(slotType);
        if (modifiedSize != null) {
            return Math.min(slotType.getMaxSize(), modifiedSize);
        }
        ISkinType type = slotType.getSkinType();
        if (type != null) {
            //return Math.min(slotType.getMaxSize(), profile.getMaxCount(type));
            return Math.min(slotType.getMaxSize(), 10);
        }
        return slotType.getMaxSize();
    }

    public Entity getEntity() {
        return entity.get();
    }

    public int getId() {
        return id;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        Storage.saveSkinSlots(skinSlots, nbt);
        Storage.saveFlags(flags, nbt);
        Storage.saveInventoryItems(inventory, nbt);
        Storage.saveDataFixer(this, nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        Storage.loadSkinSlots(skinSlots, nbt);
        Storage.loadFlags(flags, nbt);
        Storage.loadInventoryItems(inventory, nbt);
        Storage.loadDataFixer(this, nbt);
    }

    public static class Storage {

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
            if (value.size() == 0) {
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
            for (int i = 0; i < listTag.size(); ++i) {
                CompoundTag compoundTag2 = (CompoundTag) listTag.get(i);
                int j = compoundTag2.getByte("Slot") & 255;
                if (j < inventory.size()) {
                    inventory.set(j, new ItemStack(compoundTag2));
                }
            }
        }
    }
}
