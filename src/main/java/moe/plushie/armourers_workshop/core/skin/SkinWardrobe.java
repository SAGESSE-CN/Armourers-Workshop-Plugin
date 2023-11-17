package moe.plushie.armourers_workshop.core.skin;

import moe.plushie.armourers_workshop.api.ITagRepresentable;
import moe.plushie.armourers_workshop.api.skin.ISkinType;
import moe.plushie.armourers_workshop.core.network.NetworkManager;
import moe.plushie.armourers_workshop.core.network.UpdateWardrobePacket;
import moe.plushie.armourers_workshop.init.ModConfig;
import moe.plushie.armourers_workshop.init.ModEntityProfiles;
import moe.plushie.armourers_workshop.init.ModMenuTypes;
import moe.plushie.armourers_workshop.init.ModPermissions;
import moe.plushie.armourers_workshop.utils.CacheKeys;
import net.cocoonmc.Cocoon;
import net.cocoonmc.core.inventory.Container;
import net.cocoonmc.core.inventory.SimpleContainer;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.utils.PersistentDataHelper;
import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.core.world.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.BitSet;
import java.util.HashMap;
import java.util.function.Consumer;

public class SkinWardrobe implements ITagRepresentable<CompoundTag> {

    private static final int[] ES_TO_FLAGS = {0, 5, 1, 2, 3, 4};

    private final BitSet flags = new BitSet(6);

    private final HashMap<SkinSlotType, Integer> skinSlots = new HashMap<>();

    private final SimpleContainer inventory = new SimpleContainer(SkinSlotType.getTotalSize());

    private final int id;
    private final EntityProfile profile;
    private final WeakReference<Entity> entity;

    public SkinWardrobe(Entity entity, EntityProfile profile) {
        this.id = entity.getId();
        this.entity = new WeakReference<>(entity);
        this.profile = profile;
        this.inventory.addListener(it -> save());
    }

    @Nullable
    public static SkinWardrobe of(@Nullable Entity entity) {
        if (entity == null) {
            return null;
        }
        EntityProfile profile = ModEntityProfiles.getProfile(entity);
        if (profile == null) {
            return null;
        }
        return Cocoon.API.CACHE.computeIfAbsent(entity, CacheKeys.SKIN_WARDROBE_KEY, it -> {
            SkinWardrobe wardrobe = new SkinWardrobe(entity, profile);
            CompoundTag tag = entity.getPersistentData(SkinWardrobeStorage.SKIN_WARDROBE_KEY, PersistentDataHelper.COMPOUND_TAG);
            if (tag != null && tag.size() != 0) {
                wardrobe.deserializeNBT(tag);
            }
            return wardrobe;
        });
    }

    public void dropAll(@Nullable Consumer<ItemStack> consumer) {
        int containerSize = inventory.getContainerSize();
        int ignoredStart = SkinSlotType.DYE.getIndex() + 8;
        int ignoredEnd = SkinSlotType.DYE.getIndex() + SkinSlotType.DYE.getMaxSize();
        for (int i = 0; i < containerSize; ++i) {
            if (i >= ignoredStart && i < ignoredEnd) {
                continue;
            }
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack.isEmpty()) {
                continue;
            }
            if (consumer != null) {
                consumer.accept(itemStack);
            }
            inventory.setItem(i, ItemStack.EMPTY);
        }
    }

    public void save() {
        Entity entity = getEntity();
        if (entity != null) {
            entity.setPersistentData(SkinWardrobeStorage.SKIN_WARDROBE_KEY, PersistentDataHelper.COMPOUND_TAG, serializeNBT());
        }
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
            if (inventory.getItem(slotType.getIndex() + i).isEmpty()) {
                return i;
            }
        }
        return unlockedSize - 1;
    }

    public ItemStack getItem(SkinSlotType slotType, int slot) {
        if (slot >= getUnlockedSize(slotType)) {
            return ItemStack.EMPTY;
        }
        return inventory.getItem(slotType.getIndex() + slot);
    }

    public void setItem(SkinSlotType slotType, int slot, ItemStack itemStack) {
        if (slot >= getUnlockedSize(slotType)) {
            return;
        }
        inventory.setItem(slotType.getIndex() + slot, itemStack);
    }

    public ItemStack removeItem(SkinSlotType slotType, int slot, int size) {
        ItemStack itemStack = getItem(slotType, slot);
        if (itemStack != ItemStack.EMPTY) {
            return itemStack.split(size);
        }
        return ItemStack.EMPTY;
    }

    public void setUnlockedSize(SkinSlotType slotType, int size) {
        if (slotType != SkinSlotType.DYE) {
            skinSlots.put(slotType, size);
        }
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
            return Math.min(slotType.getMaxSize(), profile.getMaxCount(type));
        }
        return slotType.getMaxSize();
    }

    public Container getInventory() {
        return inventory;
    }

    public Entity getEntity() {
        return entity.get();
    }

    public EntityProfile getProfile() {
        return profile;
    }

    public int getId() {
        return id;
    }

    public boolean isEditable(Player player) {
        if (!ModPermissions.OPEN.accept(ModMenuTypes.WARDROBE, getEntity(), player)) {
            return false;
        }
        // can't edit another player's wardrobe
        Entity entity = getEntity();
        if (entity instanceof Player && entity.getId() != player.getId()) {
            return false;
        }
        if (!ModConfig.Common.canOpenWardrobe(entity, player)) {
            return false;
        }
        return !getProfile().isLocked();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = CompoundTag.newInstance();
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

