package moe.plushie.armourers_workshop.plugin.core.skin;

import moe.plushie.armourers_workshop.plugin.api.InventorySlot;
import moe.plushie.armourers_workshop.plugin.api.Menu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SkinWardrobeMenu extends Menu {

    private final SkinWardrobe wardrobe;

    public SkinWardrobeMenu(SkinWardrobe wardrobe, Player player) {
        super("armourers_workshop:wardrobe", player);
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
            addSlot(new SkinWardrobeSlot(wardrobe, slotType, i));
        }
    }

    private int getFreeSlot(SkinSlotType slotType) {
//        for (Slot slot : slots) {
//            if (slot instanceof SkinWardrobeSlot && !slot.hasItem()) {
//                SkinWardrobeSlot slot1 = (SkinWardrobeSlot) slot;
//                if (slot1.getSlotTypes().contains(slotType) || slot1.getSlotTypes().isEmpty()) {
//                    return slot1.index;
//                }
//            }
//        }
        return 0;
    }
}
