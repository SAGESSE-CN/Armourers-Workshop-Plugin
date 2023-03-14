package moe.plushie.armourers_workshop.plugin.core.menu;

import moe.plushie.armourers_workshop.plugin.api.InventorySlot;
import moe.plushie.armourers_workshop.plugin.api.ItemStack;
import moe.plushie.armourers_workshop.plugin.api.Slot;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinSlotType;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinWardrobe;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SkinWardrobeMenu extends ContainerMenu {

    private final SkinWardrobe wardrobe;

    public SkinWardrobeMenu(SkinWardrobe wardrobe, Player player) {
        super("armourers_workshop:wardrobe");
        this.wardrobe = wardrobe;

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
            addSlot(new InventorySlot(inventory, i));
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
            addSlot(new SkinSlot(wardrobe, slotType, i));
        }
    }

    private int getFreeSlot(SkinSlotType slotType) {
        for (Slot slot : slots) {
            if (slot instanceof SkinSlot && !slot.hasItem()) {
                SkinSlot slot1 = (SkinSlot) slot;
                if (slot1.getSlotTypes().contains(slotType) || slot1.getSlotTypes().isEmpty()) {
                    return slot1.getIndex();
                }
            }
        }
        return 0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = safeGetSlot(index);
        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack itemStack = slot.getItem();
        if (slot instanceof SkinSlot) {
            if (!(moveItemStackTo(itemStack, 9, 36, false) || moveItemStackTo(itemStack, 0, 9, false))) {
                return ItemStack.EMPTY;
            }
            slot.set(ItemStack.EMPTY);
            return itemStack.copy();
        }
        SkinSlotType slotType = SkinSlotType.of(itemStack);
        if (slotType != null) {
            int startIndex = getFreeSlot(slotType);
            if (!moveItemStackTo(itemStack, startIndex, startIndex + 1, false)) {
                return ItemStack.EMPTY;
            }
            slot.set(ItemStack.EMPTY);
            return itemStack.copy();
        }
        return ItemStack.EMPTY;
    }
}
