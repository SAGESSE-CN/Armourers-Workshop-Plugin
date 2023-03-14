package moe.plushie.armourers_workshop.plugin.core.menu;

import moe.plushie.armourers_workshop.plugin.api.ClickAction;
import moe.plushie.armourers_workshop.plugin.api.ClickType;
import moe.plushie.armourers_workshop.plugin.api.HandleResult;
import moe.plushie.armourers_workshop.plugin.api.ItemStack;
import moe.plushie.armourers_workshop.plugin.api.Menu;
import moe.plushie.armourers_workshop.plugin.api.Slot;
import moe.plushie.armourers_workshop.plugin.init.ModLog;
import moe.plushie.armourers_workshop.plugin.utils.BukkitStackUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public abstract class ContainerMenu extends Menu {

    private Player player;

    private int quickcraftType;
    private int quickcraftStatus = -1;
    private final HashSet<Slot> quickcraftSlots = new HashSet<>();

    public ContainerMenu(String registryName) {
        super(registryName);
    }

    @Override
    public HandleResult handSlotClick(int slotIndex, int button, ClickType clickType, Player player) {
        ModLog.info("slot: {}, button: {}, click: {}", slotIndex, button, clickType);
        this.player = player;
        switch (clickType) {
            case QUICK_CRAFT: {
                int k = quickcraftStatus;
                quickcraftStatus = getQuickcraftHeader(button);
                if ((k != 1 || quickcraftStatus != 2) && k != quickcraftStatus) {
                    resetQuickCraft();
                } else if (getCarried().isEmpty()) {
                    resetQuickCraft();
                } else if (quickcraftStatus == 0) {
                    quickcraftType = getQuickcraftType(button);
                    if (isValidQuickcraftType(quickcraftType, player)) {
                        quickcraftStatus = 1;
                        quickcraftSlots.clear();
                    } else {
                        resetQuickCraft();
                    }
                } else if (quickcraftStatus == 1) {
                    Slot slot = slots.get(slotIndex);
                    ItemStack itemStack = getCarried();
                    if (canItemQuickReplace(slot, itemStack, true) && slot.mayPlace(itemStack) && (quickcraftType == 2 || itemStack.getCount() > quickcraftSlots.size()) && canDragTo(slot)) {
                        quickcraftSlots.add(slot);
                    }
                } else if (quickcraftStatus == 2) {
                    if (!quickcraftSlots.isEmpty()) {
                        if (quickcraftSlots.size() == 1) {
                            int l = quickcraftSlots.iterator().next().getIndex();
                            resetQuickCraft();
                            return handSlotClick(l, quickcraftType, ClickType.PICKUP, player);
                        }

                        ItemStack itemStack2 = getCarried().copy();
                        int m = getCarried().getCount();
                        Iterator var9 = quickcraftSlots.iterator();

                        label305:
                        while (true) {
                            Slot slot2;
                            ItemStack itemStack3;
                            do {
                                do {
                                    do {
                                        do {
                                            if (!var9.hasNext()) {
                                                itemStack2.setCount(m);
                                                setCarried(itemStack2);
                                                break label305;
                                            }

                                            slot2 = (Slot) var9.next();
                                            itemStack3 = getCarried();
                                        } while (slot2 == null);
                                    } while (!canItemQuickReplace(slot2, itemStack3, true));
                                } while (!slot2.mayPlace(itemStack3));
                            } while (quickcraftType != 2 && itemStack3.getCount() < quickcraftSlots.size());

                            if (canDragTo(slot2)) {
                                ItemStack itemStack4 = itemStack2.copy();
                                int n = slot2.hasItem() ? slot2.getItem().getCount() : 0;
                                getQuickCraftSlotCount(quickcraftSlots, quickcraftType, itemStack4, n);
                                int o = Math.min(itemStack4.getMaxStackSize(), slot2.getMaxStackSize(itemStack4));
                                if (itemStack4.getCount() > o) {
                                    itemStack4.setCount(o);
                                }

                                m -= itemStack4.getCount() - n;
                                slot2.setItem(itemStack4);
                            }
                        }
                    }

                    resetQuickCraft();
                } else {
                    resetQuickCraft();
                }
                break;
            }
            case QUICK_MOVE:
            case PICKUP: {
                // (clickType == ClickType.PICKUP || clickType == ClickType.QUICK_MOVE) && (j == 0 || j == 1);
                if (button != 0 && button != 1) {
                    break;
                }
                ClickAction clickAction = button == 0 ? ClickAction.PRIMARY : ClickAction.SECONDARY;
                if (slotIndex < 0) {
                    return HandleResult.PASS;
                }
                if (clickType == ClickType.QUICK_MOVE) {
                    Slot slot = slots.get(slotIndex);
                    if (!slot.mayPickup(player)) {
                        break;
                    }
                    int i = slotIndex;
                    ItemStack itemStack;
                    for (itemStack = quickMoveStack(player, i); !itemStack.isEmpty() && ItemStack.isSame(slot.getItem(), itemStack); itemStack = quickMoveStack(player, i)) {
                    }
                    break;
                }
                Slot slot = slots.get(slotIndex);
                ItemStack itemStack = slot.getItem();
                ItemStack itemStack5 = getCarried();
//                player.updateTutorialInventoryAction(itemStack5, slot.getItem(), clickAction);
//                if (!itemStack5.overrideStackedOnOther(slot, clickAction, player) && !itemStack.overrideOtherStackedOnMe(itemStack5, slot, clickAction, player, this.createCarriedSlotAccess())) {
                if (itemStack.isEmpty()) {
                    if (!itemStack5.isEmpty()) {
                        int p = clickAction == ClickAction.PRIMARY ? itemStack5.getCount() : 1;
                        setCarried(slot.safeInsert(itemStack5, p));
                    }
                } else if (slot.mayPickup(player)) {
                    if (itemStack5.isEmpty()) {
                        int p = clickAction == ClickAction.PRIMARY ? itemStack.getCount() : (itemStack.getCount() + 1) / 2;
                        Optional<ItemStack> optional = slot.tryRemove(p, Integer.MAX_VALUE, player);
                        optional.ifPresent(itemStackx -> {
                            setCarried(itemStackx);
                            slot.onTake(player, itemStackx);
                        });
                    } else if (slot.mayPlace(itemStack5)) {
                        if (ItemStack.isSameItemSameTags(itemStack, itemStack5)) {
                            int p = clickAction == ClickAction.PRIMARY ? itemStack5.getCount() : 1;
                            setCarried(slot.safeInsert(itemStack5, p));
                        } else if (itemStack5.getCount() <= slot.getMaxStackSize(itemStack5)) {
                            setCarried(itemStack);
                            slot.setItem(itemStack5);
                        }
                    } else if (ItemStack.isSameItemSameTags(itemStack, itemStack5)) {
                        Optional<ItemStack> optional2 = slot.tryRemove(itemStack.getCount(), itemStack5.getMaxStackSize() - itemStack5.getCount(), player);
                        optional2.ifPresent(itemStack2x -> {
                            itemStack5.grow(itemStack2x.getCount());
                            slot.onTake(player, itemStack2x);
                        });
                    }
                }
//                }
                slot.setChanged();
                break;
            }
            case PICKUP_ALL: {
                if (slotIndex < 0) {
                    return HandleResult.PASS;
                }
                Slot slot3 = slots.get(slotIndex);
                ItemStack itemStack2 = getCarried();
                if (!itemStack2.isEmpty() && (!slot3.hasItem() || !slot3.mayPickup(player))) {
                    int m = button == 0 ? 0 : slots.size() - 1;
                    int q = button == 0 ? 1 : -1;

                    for (int p = 0; p < 2; ++p) {
                        for (int r = m; r >= 0 && r < slots.size() && itemStack2.getCount() < itemStack2.getMaxStackSize(); r += q) {
                            Slot slot4 = slots.get(r);
                            if (slot4.hasItem() && canItemQuickReplace(slot4, itemStack2, true) && slot4.mayPickup(player) && canTakeItemForPickAll(itemStack2, slot4)) {
                                ItemStack itemStack6 = slot4.getItem();
                                if (p != 0 || itemStack6.getCount() != itemStack6.getMaxStackSize()) {
                                    ItemStack itemStack7 = slot4.safeTake(itemStack6.getCount(), itemStack2.getMaxStackSize() - itemStack2.getCount(), player);
                                    itemStack2.grow(itemStack7.getCount());
                                }
                            }
                        }
                    }
                }
                break;
            }
            case SWAP: {
                if (slotIndex < 0) {
                    return HandleResult.PASS;
                }
                Slot slot3 = slots.get(slotIndex);
                Slot slot1 = slots.get(button);
                ItemStack itemStack2 = slot1.getItem();
                ItemStack itemStack = slot3.getItem();
                if (!itemStack2.isEmpty() || !itemStack.isEmpty()) {
                    if (itemStack2.isEmpty()) {
                        if (slot3.mayPickup(player)) {
                            slot1.setItem(itemStack);
                            slot3.onSwapCraft(itemStack.getCount());
                            slot3.setItem(ItemStack.EMPTY);
                            slot3.onTake(player, itemStack);
                        }
                    } else if (itemStack.isEmpty()) {
                        if (slot3.mayPlace(itemStack2)) {
                            int q = slot3.getMaxStackSize(itemStack2);
                            if (itemStack2.getCount() > q) {
                                slot3.setItem(itemStack2.split(q));
                            } else {
                                slot1.setItem(ItemStack.EMPTY);
                                slot3.setItem(itemStack2);
                            }
                        }
                    } else if (slot3.mayPickup(player) && slot3.mayPlace(itemStack2)) {
                        int q = slot3.getMaxStackSize(itemStack2);
                        if (itemStack2.getCount() > q) {
                            slot3.setItem(itemStack2.split(q));
                            slot3.onTake(player, itemStack);
                            BukkitStackUtils.giveItemTo(itemStack, player);
                        } else {
                            slot1.setItem(itemStack);
                            slot3.setItem(itemStack2);
                            slot3.onTake(player, itemStack);
                        }
                    }
                }
                break;
            }
            case CLONE: {
                // clickType == ClickType.CLONE && player.getAbilities().instabuild && this.getCarried().isEmpty() && i >= 0
                if (slotIndex < 0) {
                    return HandleResult.PASS;
                }
                ItemStack itemStack = getCarried();
                if (player.getGameMode() != GameMode.CREATIVE || !itemStack.isEmpty()) {
                    return HandleResult.PASS;
                }
                Slot slot3 = slots.get(slotIndex);
                if (slot3.hasItem()) {
                    ItemStack itemStack2 = slot3.getItem().copy();
                    itemStack2.setCount(itemStack2.getMaxStackSize());
                    setCarried(itemStack2);
                }
                break;
            }
            case THROW: {
                // clickType == ClickType.THROW && this.getCarried().isEmpty() && i >= 0
                if (slotIndex < 0) {
                    return HandleResult.PASS;
                }
                ItemStack itemStack = getCarried();
                if (!itemStack.isEmpty()) {
                    return HandleResult.PASS;
                }
                Slot slot3 = slots.get(slotIndex);
                int l = button == 0 ? 1 : slot3.getItem().getCount();
                itemStack = slot3.safeTake(l, Integer.MAX_VALUE, player);
                BukkitStackUtils.drop(itemStack, player, true);
                break;
            }
        }
        return HandleResult.SUCCESS;
    }

    public abstract ItemStack quickMoveStack(Player player, int i);

    public ItemStack quickMoveStack(Player player, int index, int slotSize) {
        Slot slot = safeGetSlot(index);
        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack itemStack = slot.getItem();
        if (index >= 36) {
            if (!(moveItemStackTo(itemStack, 9, 36, false) || moveItemStackTo(itemStack, 0, 9, false))) {
                return ItemStack.EMPTY;
            }
            slot.setItem(ItemStack.EMPTY);
            return itemStack.copy();
        }
        if (!moveItemStackTo(itemStack, 36, slotSize, false)) {
            return ItemStack.EMPTY;
        }
        slot.setChanged();
        return ItemStack.EMPTY;
    }

    protected boolean moveItemStackTo(ItemStack itemStack, int i, int j, boolean bl) {
        boolean bl2 = false;
        int k = i;
        if (bl) {
            k = j - 1;
        }

        Slot slot;
        ItemStack itemStack2;
        if (itemStack.isStackable()) {
            while (!itemStack.isEmpty()) {
                if (bl) {
                    if (k < i) {
                        break;
                    }
                } else if (k >= j) {
                    break;
                }

                slot = slots.get(k);
                itemStack2 = slot.getItem();
                if (!itemStack2.isEmpty() && ItemStack.isSameItemSameTags(itemStack, itemStack2)) {
                    int l = itemStack2.getCount() + itemStack.getCount();
                    if (l <= itemStack.getMaxStackSize()) {
                        itemStack.setCount(0);
                        itemStack2.setCount(l);
                        slot.setChanged();
                        bl2 = true;
                    } else if (itemStack2.getCount() < itemStack.getMaxStackSize()) {
                        itemStack.shrink(itemStack.getMaxStackSize() - itemStack2.getCount());
                        itemStack2.setCount(itemStack.getMaxStackSize());
                        slot.setChanged();
                        bl2 = true;
                    }
                }

                if (bl) {
                    --k;
                } else {
                    ++k;
                }
            }
        }

        if (!itemStack.isEmpty()) {
            if (bl) {
                k = j - 1;
            } else {
                k = i;
            }

            while (true) {
                if (bl) {
                    if (k < i) {
                        break;
                    }
                } else if (k >= j) {
                    break;
                }

                slot = slots.get(k);
                itemStack2 = slot.getItem();
                if (itemStack2.isEmpty() && slot.mayPlace(itemStack)) {
                    if (itemStack.getCount() > slot.getMaxStackSize()) {
                        slot.setItem(itemStack.split(slot.getMaxStackSize()));
                    } else {
                        slot.setItem(itemStack.split(itemStack.getCount()));
                    }

                    slot.setChanged();
                    bl2 = true;
                    break;
                }

                if (bl) {
                    --k;
                } else {
                    ++k;
                }
            }
        }

        return bl2;
    }

    public static int getQuickcraftType(int i) {
        return i >> 2 & 3;
    }

    public static int getQuickcraftHeader(int i) {
        return i & 3;
    }

    public static int getQuickcraftMask(int i, int j) {
        return i & 3 | (j & 3) << 2;
    }

    public static boolean isValidQuickcraftType(int i, Player player) {
        if (i == 0) {
            return true;
        } else if (i == 1) {
            return true;
        } else {
            return i == 2 && player.getGameMode() == GameMode.CREATIVE; //player.getAbilities().instabuild;
        }
    }

    protected void resetQuickCraft() {
        this.quickcraftStatus = 0;
        this.quickcraftSlots.clear();
        ModLog.info("quick craft: reset");
    }

    //    public void broadcastChanges() {
//    }
//
//
    public void setCarried(ItemStack itemStack) {
        if (player != null) {
            player.setItemOnCursor(BukkitStackUtils.unwrap(itemStack));
        }
    }

    public ItemStack getCarried() {
        if (player != null) {
            return BukkitStackUtils.wrap(player.getItemOnCursor());
        }
        return ItemStack.EMPTY;
    }

    public static boolean canItemQuickReplace(@Nullable Slot slot, ItemStack itemStack, boolean bl) {
        boolean bl2 = slot == null || !slot.hasItem();
        if (!bl2 && ItemStack.isSameItemSameTags(itemStack, slot.getItem())) {
            return slot.getItem().getCount() + (bl ? 0 : itemStack.getCount()) <= itemStack.getMaxStackSize();
        } else {
            return bl2;
        }
    }


    public static int floor(float f) {
        int i = (int) f;
        return f < (float) i ? i - 1 : i;
    }

    public static void getQuickCraftSlotCount(Set<Slot> set, int i, ItemStack itemStack, int j) {
        switch (i) {
            case 0:
                itemStack.setCount(floor((float) itemStack.getCount() / (float) set.size()));
                break;
            case 1:
                itemStack.setCount(1);
                break;
            case 2:
                itemStack.setCount(itemStack.getMaxStackSize());
        }
        itemStack.grow(j);
    }

    public boolean canDragTo(Slot slot) {
        return true;
    }

    public boolean canTakeItemForPickAll(ItemStack itemStack, Slot slot) {
        return true;
    }
//
//    public boolean canRollbackHandle(Integer... slotIndexs) {
//        // We try rollback to handle vanilla as much as possible to avoid bugs.
//        for (int slotIndex : slotIndexs) {
//            if (slotIndex < 0) {
//                continue;
//            }
//            if (slots.get(slotIndex) instanceof InventorySlot) {
//                continue;
//            }
//            return false;
//        }
//        return true;  // rollback to vanilla
//    }
}
