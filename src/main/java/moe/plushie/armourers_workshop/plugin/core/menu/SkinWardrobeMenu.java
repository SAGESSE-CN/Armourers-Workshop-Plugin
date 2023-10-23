package moe.plushie.armourers_workshop.plugin.core.menu;

import moe.plushie.armourers_workshop.customapi.CustomSlot;
import moe.plushie.armourers_workshop.plugin.api.FriendlyByteBuf;
import moe.plushie.armourers_workshop.plugin.api.MenuType;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinSlotType;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinWardrobe;
import moe.plushie.armourers_workshop.plugin.init.ModLog;
import moe.plushie.armourers_workshop.plugin.utils.BukkitUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public class SkinWardrobeMenu extends ContainerMenu {

    private final Inventory inventory;
    private final SkinWardrobe wardrobe;
    private final ArrayList<ItemStack> lastSyncSlot = new ArrayList<>();

    public SkinWardrobeMenu(MenuType<?> menuType, Player player, SkinWardrobe wardrobe) {
        super(menuType, player, SkinSlotType.getTotalSize());
        this.wardrobe = wardrobe;
        this.inventory = getInventory();

        addPlayerSlots(player.getInventory());

        addSkinSlots(SkinSlotType.HEAD);
        addSkinSlots(SkinSlotType.CHEST);
        addSkinSlots(SkinSlotType.LEGS);
        addSkinSlots(SkinSlotType.FEET);
        addSkinSlots(SkinSlotType.WINGS);

        addEquipmentSlots();

        addSkinSlots(SkinSlotType.OUTFIT);
        addSkinSlots(SkinSlotType.DYE);

//        addMannequinSlots(Group.SKINS, 0, 5);
    }

    private void addPlayerSlots(Inventory inventory) {
        for (int i = 0; i < 36; ++i) {
            addSlot(new CustomSlot(inventory, i, 0, 0));
        }
    }

    protected void addEquipmentSlots() {
        SkinSlotType[] slotTypes = {SkinSlotType.SWORD, SkinSlotType.SHIELD, SkinSlotType.BOW, SkinSlotType.TRIDENT, null, SkinSlotType.PICKAXE, SkinSlotType.AXE, SkinSlotType.SHOVEL, SkinSlotType.HOE};
        for (SkinSlotType slotType : slotTypes) {
            if (slotType != null) {
                int count = wardrobe.getUnlockedSize(slotType);
                if (count > 0) {
                    addSkinSlots(slotType);
                }
            }
        }
    }

    protected void addMannequinSlots() {
//        if (wardrobe.getEntity() instanceof MannequinEntity) {
//            Container inventory = ((MannequinEntity) wardrobe.getEntity()).getInventory();
//            int size = inventory.getContainerSize();
//            for (int i = 0; i < inventory.getContainerSize(); ++i) {
//                int x = slotsX + (column + (size - i - 1)) * 19; // reverse: slot 1 => left, slot 2 => right
//                int y = slotsY + row * 19;
//                SkinSlot slot = addGroupSlot(inventory, i, x, y, group);
//                customSlots.add(slot);
//            }
//        }
    }

    private void addSkinSlots(SkinSlotType slotType) {
        int size = wardrobe.getUnlockedSize(slotType);
        for (int i = 0; i < size; ++i) {
            addSlot(new SkinSlot(inventory, wardrobe, slotType, i));
        }
    }

    private int getFreeSlot(SkinSlotType slotType) {
        for (CustomSlot slot : slots) {
            if (slot instanceof SkinSlot && !slot.hasItem()) {
                SkinSlot slot1 = (SkinSlot) slot;
                if (slot1.getSlotTypes().contains(slotType) || slot1.getSlotTypes().isEmpty()) {
                    return slot1.index;
                }
            }
        }
        return 0;
    }

    @Override
    public boolean stillValid(Player player) {
        Entity entity = getEntity();
        if (entity == null || !entity.isValid() || !wardrobe.isEditable(player)) {
            return false;
        }
        return entity.getLocation().distance(player.getLocation()) <= 64.0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        CustomSlot slot = slots.get(index);
        if (slot == null || !slot.hasItem()) {
            return BukkitUtils.EMPTY_STACK;
        }
        ItemStack itemStack = slot.getItem();
        if (slot instanceof SkinSlot) {
            if (!(moveItemStackTo(itemStack, 9, 36, false) || moveItemStackTo(itemStack, 0, 9, false))) {
                return BukkitUtils.EMPTY_STACK;
            }
            slot.set(BukkitUtils.EMPTY_STACK);
            return new ItemStack(itemStack);
        }
        SkinSlotType slotType = SkinSlotType.of(BukkitUtils.wrap(itemStack));
        if (slotType != null) {
            int startIndex = getFreeSlot(slotType);
            if (!moveItemStackTo(itemStack, startIndex, startIndex + 1, false)) {
                return BukkitUtils.EMPTY_STACK;
            }
            slot.set(BukkitUtils.EMPTY_STACK);
            return new ItemStack(itemStack);
        }
        return BukkitUtils.EMPTY_STACK;
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        // in normal, the size is inconsistent this will only happen when the container is first loaded.
        if (lastSyncSlot.size() != slots.size()) {
            lastSyncSlot.ensureCapacity(slots.size());
            slots.forEach(s -> lastSyncSlot.add(s.getItem()));
            return;
        }
        // if slots is ready, we check all slots and fast synchronize changes to all players if changes.
        int changes = 0;
        for (int index = 0; index < slots.size(); ++index) {
            // the first 36 slots we defined as player slots, no synchronize is required.
            if (index < 36) {
                continue;
            }
            ItemStack newItemStack = slots.get(index).getItem();
            if (!Objects.equals(lastSyncSlot.get(index), newItemStack)) {
                lastSyncSlot.set(index, newItemStack);
                changes += 1;
            }
        }
        if (changes != 0) {
            ModLog.debug("observer slots has {} changes, sync to players", changes);
            wardrobe.broadcast();
        }
    }

    @Override
    public void serialize(FriendlyByteBuf buffer) {
        buffer.writeInt(wardrobe.getId());
        buffer.writeUtf(wardrobe.getProfile().getRegistryName().toString());
    }


    public SkinWardrobe getWardrobe() {
        return wardrobe;
    }

    @Nullable
    public Entity getEntity() {
        return wardrobe.getEntity();
    }
}
