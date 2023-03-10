package moe.plushie.armourers_workshop.plugin.core.skin;

import moe.plushie.armourers_workshop.plugin.api.skin.ISkinType;
import moe.plushie.armourers_workshop.plugin.api.ITagRepresentable;
import moe.plushie.armourers_workshop.plugin.api.ItemStack;
import moe.plushie.armourers_workshop.plugin.api.NonNullList;
import moe.plushie.armourers_workshop.plugin.network.NetworkManager;
import moe.plushie.armourers_workshop.plugin.network.UpdateWardrobePacket;
import net.querz.nbt.tag.CompoundTag;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Nullable;

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

    @Nullable
    public static SkinWardrobe of(@Nullable Entity entity) {
        if (entity != null) {
            return SkinWardrobeStorage.of(entity);
        }
        return null;
    }

    public void save() {
        SkinWardrobeStorage.saveToContainer(this);
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

    public Inventory getInventory() {
        // TODO:IMP
//        return inventory;
        return null;
    }

    public Entity getEntity() {
        return entity.get();
    }

    public int getId() {
        return id;
    }

    public boolean isEditable(Player player) {
//        if (!ModPermissions.OPEN.accept(ModMenuTypes.WARDROBE, getEntity(), player)) {
//            return false;
//        }
//        // can't edit another player's wardrobe
//        Entity entity = getEntity();
//        if (entity instanceof Player && entity.getId() != player.getId()) {
//            return false;
//        }
//        if (!ModConfig.Common.canOpenWardrobe(entity, player)) {
//            return false;
//        }
//        return !getProfile().isLocked();
        return true;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        SkinWardrobeStorage.saveSkinSlots(skinSlots, nbt);
        SkinWardrobeStorage.saveFlags(flags, nbt);
        SkinWardrobeStorage.saveInventoryItems(inventory, nbt);
        SkinWardrobeStorage.saveDataFixer(this, nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        SkinWardrobeStorage.loadSkinSlots(skinSlots, nbt);
        SkinWardrobeStorage.loadFlags(flags, nbt);
        SkinWardrobeStorage.loadInventoryItems(inventory, nbt);
        SkinWardrobeStorage.loadDataFixer(this, nbt);
    }
}

