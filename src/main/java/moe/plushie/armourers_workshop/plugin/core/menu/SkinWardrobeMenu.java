package moe.plushie.armourers_workshop.plugin.core.menu;

import moe.plushie.armourers_workshop.plugin.api.ClickType;
import moe.plushie.armourers_workshop.plugin.api.InventorySlot;
import moe.plushie.armourers_workshop.plugin.api.ItemStack;
import moe.plushie.armourers_workshop.plugin.api.Menu;
import moe.plushie.armourers_workshop.plugin.api.Slot;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinSlotType;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinWardrobe;
import moe.plushie.armourers_workshop.plugin.init.ModLog;
import moe.plushie.armourers_workshop.plugin.utils.BukkitStackUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SkinWardrobeMenu extends Menu {

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

    @Override
    public void handSlotClick(int slotIndex, int button, ClickType clickType, Player player) {
        Slot slot = findSlot(slotIndex);
        ModLog.info("你点击了容器, 槽位ID: {}, 类型: {}, 槽: {}", slotIndex, clickType, slot);
        switch (clickType) {
            case QUICK_CRAFT:
            case QUICK_MOVE:
            case PICKUP:
            case PICKUP_ALL: {
                if (slot != null && slot.getItem() != null) {
                    ItemStack itemStack = BukkitStackUtils.wrap(player.getItemOnCursor());
                    player.setItemOnCursor(BukkitStackUtils.unwrap(slot.getItem()));
                    slot.set(itemStack);
                }
                break;
            }

        }

        // TODO: IMPL
        //    PICKUP,
        //    QUICK_MOVE,
        //    SWAP,
        //    CLONE,
        //    THROW,
        //    QUICK_CRAFT,
        //    PICKUP_ALL;
    }

//    private ItemStack doClick(int i, int j, ClickType clickType, Player player) {
//        ItemStack itemStack = ItemStack.EMPTY;
//        Inventory inventory = player.inventory;
//        if (clickType == ClickType.QUICK_CRAFT) {
//            int k = this.quickcraftStatus;
//            this.quickcraftStatus = AbstractContainerMenu.getQuickcraftHeader(j);
//            if ((k != 1 || this.quickcraftStatus != 2) && k != this.quickcraftStatus) {
//                this.resetQuickCraft();
//            } else if (inventory.getCarried().isEmpty()) {
//                this.resetQuickCraft();
//            } else if (this.quickcraftStatus == 0) {
//                this.quickcraftType = AbstractContainerMenu.getQuickcraftType(j);
//                if (AbstractContainerMenu.isValidQuickcraftType(this.quickcraftType, player)) {
//                    this.quickcraftStatus = 1;
//                    this.quickcraftSlots.clear();
//                } else {
//                    this.resetQuickCraft();
//                }
//            } else if (this.quickcraftStatus == 1) {
//                Slot slot = this.slots.get(i);
//                ItemStack itemStack2 = inventory.getCarried();
//                if (slot != null && AbstractContainerMenu.canItemQuickReplace(slot, itemStack2, true) && slot.mayPlace(itemStack2) && (this.quickcraftType == 2 || itemStack2.getCount() > this.quickcraftSlots.size()) && this.canDragTo(slot)) {
//                    this.quickcraftSlots.add(slot);
//                }
//            } else if (this.quickcraftStatus == 2) {
//                if (!this.quickcraftSlots.isEmpty()) {
//                    ItemStack itemStack3 = inventory.getCarried().copy();
//                    int l = inventory.getCarried().getCount();
//                    for (Slot slot2 : this.quickcraftSlots) {
//                        ItemStack itemStack4 = inventory.getCarried();
//                        if (slot2 == null || !AbstractContainerMenu.canItemQuickReplace(slot2, itemStack4, true) || !slot2.mayPlace(itemStack4) || this.quickcraftType != 2 && itemStack4.getCount() < this.quickcraftSlots.size() || !this.canDragTo(slot2)) continue;
//                        ItemStack itemStack5 = itemStack3.copy();
//                        int m = slot2.hasItem() ? slot2.getItem().getCount() : 0;
//                        AbstractContainerMenu.getQuickCraftSlotCount(this.quickcraftSlots, this.quickcraftType, itemStack5, m);
//                        int n = Math.min(itemStack5.getMaxStackSize(), slot2.getMaxStackSize(itemStack5));
//                        if (itemStack5.getCount() > n) {
//                            itemStack5.setCount(n);
//                        }
//                        l -= itemStack5.getCount() - m;
//                        slot2.set(itemStack5);
//                    }
//                    itemStack3.setCount(l);
//                    inventory.setCarried(itemStack3);
//                }
//                this.resetQuickCraft();
//            } else {
//                this.resetQuickCraft();
//            }
//        } else if (this.quickcraftStatus != 0) {
//            this.resetQuickCraft();
//        } else if (!(clickType != ClickType.PICKUP && clickType != ClickType.QUICK_MOVE || j != 0 && j != 1)) {
//            if (i == -999) {
//                if (!inventory.getCarried().isEmpty()) {
//                    if (j == 0) {
//                        player.drop(inventory.getCarried(), true);
//                        inventory.setCarried(ItemStack.EMPTY);
//                    }
//                    if (j == 1) {
//                        player.drop(inventory.getCarried().split(1), true);
//                    }
//                }
//            } else if (clickType == ClickType.QUICK_MOVE) {
//                if (i < 0) {
//                    return ItemStack.EMPTY;
//                }
//                Slot slot3 = this.slots.get(i);
//                if (slot3 == null || !slot3.mayPickup(player)) {
//                    return ItemStack.EMPTY;
//                }
//                ItemStack itemStack3 = this.quickMoveStack(player, i);
//                while (!itemStack3.isEmpty() && ItemStack.isSame(slot3.getItem(), itemStack3)) {
//                    itemStack = itemStack3.copy();
//                    itemStack3 = this.quickMoveStack(player, i);
//                }
//            } else {
//                if (i < 0) {
//                    return ItemStack.EMPTY;
//                }
//                Slot slot3 = this.slots.get(i);
//                if (slot3 != null) {
//                    ItemStack itemStack3 = slot3.getItem();
//                    ItemStack itemStack2 = inventory.getCarried();
//                    if (!itemStack3.isEmpty()) {
//                        itemStack = itemStack3.copy();
//                    }
//                    if (itemStack3.isEmpty()) {
//                        if (!itemStack2.isEmpty() && slot3.mayPlace(itemStack2)) {
//                            int o;
//                            int n = o = j == 0 ? itemStack2.getCount() : 1;
//                            if (o > slot3.getMaxStackSize(itemStack2)) {
//                                o = slot3.getMaxStackSize(itemStack2);
//                            }
//                            slot3.set(itemStack2.split(o));
//                        }
//                    } else if (slot3.mayPickup(player)) {
//                        int o;
//                        if (itemStack2.isEmpty()) {
//                            if (itemStack3.isEmpty()) {
//                                slot3.set(ItemStack.EMPTY);
//                                inventory.setCarried(ItemStack.EMPTY);
//                            } else {
//                                int o2 = j == 0 ? itemStack3.getCount() : (itemStack3.getCount() + 1) / 2;
//                                inventory.setCarried(slot3.remove(o2));
//                                if (itemStack3.isEmpty()) {
//                                    slot3.set(ItemStack.EMPTY);
//                                }
//                                slot3.onTake(player, inventory.getCarried());
//                            }
//                        } else if (slot3.mayPlace(itemStack2)) {
//                            if (AbstractContainerMenu.consideredTheSameItem(itemStack3, itemStack2)) {
//                                int o3;
//                                int n = o3 = j == 0 ? itemStack2.getCount() : 1;
//                                if (o3 > slot3.getMaxStackSize(itemStack2) - itemStack3.getCount()) {
//                                    o3 = slot3.getMaxStackSize(itemStack2) - itemStack3.getCount();
//                                }
//                                if (o3 > itemStack2.getMaxStackSize() - itemStack3.getCount()) {
//                                    o3 = itemStack2.getMaxStackSize() - itemStack3.getCount();
//                                }
//                                itemStack2.shrink(o3);
//                                itemStack3.grow(o3);
//                            } else if (itemStack2.getCount() <= slot3.getMaxStackSize(itemStack2)) {
//                                slot3.set(itemStack2);
//                                inventory.setCarried(itemStack3);
//                            }
//                        } else if (itemStack2.getMaxStackSize() > 1 && AbstractContainerMenu.consideredTheSameItem(itemStack3, itemStack2) && !itemStack3.isEmpty() && (o = itemStack3.getCount()) + itemStack2.getCount() <= itemStack2.getMaxStackSize()) {
//                            itemStack2.grow(o);
//                            itemStack3 = slot3.remove(o);
//                            if (itemStack3.isEmpty()) {
//                                slot3.set(ItemStack.EMPTY);
//                            }
//                            slot3.onTake(player, inventory.getCarried());
//                        }
//                    }
//                    slot3.setChanged();
//                }
//            }
//        } else if (clickType == ClickType.SWAP) {
//            Slot slot3 = this.slots.get(i);
//            ItemStack itemStack3 = inventory.getItem(j);
//            ItemStack itemStack2 = slot3.getItem();
//            if (!itemStack3.isEmpty() || !itemStack2.isEmpty()) {
//                if (itemStack3.isEmpty()) {
//                    if (slot3.mayPickup(player)) {
//                        inventory.setItem(j, itemStack2);
//                        slot3.onSwapCraft(itemStack2.getCount());
//                        slot3.set(ItemStack.EMPTY);
//                        slot3.onTake(player, itemStack2);
//                    }
//                } else if (itemStack2.isEmpty()) {
//                    if (slot3.mayPlace(itemStack3)) {
//                        int o = slot3.getMaxStackSize(itemStack3);
//                        if (itemStack3.getCount() > o) {
//                            slot3.set(itemStack3.split(o));
//                        } else {
//                            slot3.set(itemStack3);
//                            inventory.setItem(j, ItemStack.EMPTY);
//                        }
//                    }
//                } else if (slot3.mayPickup(player) && slot3.mayPlace(itemStack3)) {
//                    int o = slot3.getMaxStackSize(itemStack3);
//                    if (itemStack3.getCount() > o) {
//                        slot3.set(itemStack3.split(o));
//                        slot3.onTake(player, itemStack2);
//                        if (!inventory.add(itemStack2)) {
//                            player.drop(itemStack2, true);
//                        }
//                    } else {
//                        slot3.set(itemStack3);
//                        inventory.setItem(j, itemStack2);
//                        slot3.onTake(player, itemStack2);
//                    }
//                }
//            }
//        } else if (clickType == ClickType.CLONE && player.abilities.instabuild && inventory.getCarried().isEmpty() && i >= 0) {
//            Slot slot3 = this.slots.get(i);
//            if (slot3 != null && slot3.hasItem()) {
//                ItemStack itemStack3 = slot3.getItem().copy();
//                itemStack3.setCount(itemStack3.getMaxStackSize());
//                inventory.setCarried(itemStack3);
//            }
//        } else if (clickType == ClickType.THROW && inventory.getCarried().isEmpty() && i >= 0) {
//            Slot slot3 = this.slots.get(i);
//            if (slot3 != null && slot3.hasItem() && slot3.mayPickup(player)) {
//                ItemStack itemStack3 = slot3.remove(j == 0 ? 1 : slot3.getItem().getCount());
//                slot3.onTake(player, itemStack3);
//                player.drop(itemStack3, true);
//            }
//        } else if (clickType == ClickType.PICKUP_ALL && i >= 0) {
//            Slot slot3 = this.slots.get(i);
//            ItemStack itemStack3 = inventory.getCarried();
//            if (!(itemStack3.isEmpty() || slot3 != null && slot3.hasItem() && slot3.mayPickup(player))) {
//                int l = j == 0 ? 0 : this.slots.size() - 1;
//                int o = j == 0 ? 1 : -1;
//                for (int p = 0; p < 2; ++p) {
//                    for (int q = l; q >= 0 && q < this.slots.size() && itemStack3.getCount() < itemStack3.getMaxStackSize(); q += o) {
//                        Slot slot4 = this.slots.get(q);
//                        if (!slot4.hasItem() || !AbstractContainerMenu.canItemQuickReplace(slot4, itemStack3, true) || !slot4.mayPickup(player) || !this.canTakeItemForPickAll(itemStack3, slot4)) continue;
//                        ItemStack itemStack6 = slot4.getItem();
//                        if (p == 0 && itemStack6.getCount() == itemStack6.getMaxStackSize()) continue;
//                        int n = Math.min(itemStack3.getMaxStackSize() - itemStack3.getCount(), itemStack6.getCount());
//                        ItemStack itemStack7 = slot4.remove(n);
//                        itemStack3.grow(n);
//                        if (itemStack7.isEmpty()) {
//                            slot4.set(ItemStack.EMPTY);
//                        }
//                        slot4.onTake(player, itemStack7);
//                    }
//                }
//            }
//            this.broadcastChanges();
//        }
//        return itemStack;
//    }

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
