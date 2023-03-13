package moe.plushie.armourers_workshop.plugin.core.menu;

import com.google.common.collect.Sets;
import moe.plushie.armourers_workshop.plugin.api.ClickType;
import moe.plushie.armourers_workshop.plugin.api.ItemStack;
import moe.plushie.armourers_workshop.plugin.api.Menu;
import moe.plushie.armourers_workshop.plugin.api.NonNullList;
import moe.plushie.armourers_workshop.plugin.api.Slot;
import moe.plushie.armourers_workshop.plugin.init.ModLog;
import moe.plushie.armourers_workshop.plugin.utils.BukkitStackUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public abstract class ContainerMenu extends Menu {


    private ItemStack carried;

    private final NonNullList<ItemStack> remoteSlots;
//    private final IntList remoteDataSlots;

    private final NonNullList<ItemStack> lastSlots = NonNullList.create();

    private int quickcraftType;
    private int quickcraftStatus;
    private final Set<Slot> quickcraftSlots;
//    private final List<ContainerListener> containerListeners;

//    protected AbstractContainerMenu(@Nullable MenuType<?> menuType, int i) {
//        this.carried = ItemStack.EMPTY;
//        this.remoteCarried = ItemStack.EMPTY;
//        this.quickcraftType = -1;
//        this.quickcraftSlots = Sets.newHashSet();
//        this.containerListeners = Lists.newArrayList();
//        this.menuType = menuType;
//        this.containerId = i;
//    }

    public ContainerMenu(String registryName) {
        super(registryName);
        this.carried = ItemStack.EMPTY;
        this.remoteSlots = NonNullList.create();
//        this.remoteDataSlots = new IntArrayList();
        this.quickcraftType = -1;
        this.quickcraftSlots = Sets.newHashSet();
    }

    @Override
    public boolean handSlotClick(int slotIndex, int button, ClickType clickType, Player player) {
        ModLog.info("slot: {}, button: {}, click: {}", slotIndex, button, clickType);
        switch (clickType) {
            case QUICK_CRAFT: {
                // FIXME: some bug ?
                Slot slot = slots.get(slotIndex);
                ItemStack itemStack = getCarried(player);
                setCarried(player, slot.getItem());
                slot.setItem(itemStack);
                break;
            }
            case QUICK_MOVE: {
                Slot slot = slots.get(slotIndex);
                if (!slot.mayPickup(player)) {
                    return true;
                }
                int i = slotIndex;
                ItemStack itemStack;
                for (itemStack = quickMoveStack(player, i); !itemStack.isEmpty() && ItemStack.isSame(slot.getItem(), itemStack); itemStack = quickMoveStack(player, i)) {
                }
                return true;
            }
            case PICKUP: {
                Slot slot = slots.get(slotIndex);
                ItemStack itemStack = getCarried(player);
                setCarried(player, slot.getItem());
                slot.setItem(itemStack);
                break;
            }
            case PICKUP_ALL: {
                Slot slot3 = slots.get(slotIndex);
                ItemStack itemStack2 = getCarried(player);
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
                if (player.getGameMode() == GameMode.CREATIVE) {
                    Slot slot3 = slots.get(slotIndex);
                    ItemStack itemStack = getCarried(player);
                    if (itemStack.isEmpty() && slot3.hasItem()) {
                        ItemStack itemStack2 = slot3.getItem().copy();
                        itemStack2.setCount(itemStack2.getMaxStackSize());
                        setCarried(player, itemStack2);
                    }
                }
                break;
            }
            case THROW: {
                ItemStack itemStack = getCarried(player);
                if (itemStack.isEmpty()) {
                    Slot slot3 = slots.get(slotIndex);
                    int l = button == 0 ? 1 : slot3.getItem().getCount();
                    itemStack = slot3.safeTake(l, Integer.MAX_VALUE, player);
                    BukkitStackUtils.drop(itemStack, player, true);
                }
                break;
            }
        }
        return true;
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


    private void doClick(int i, int j, ClickType clickType, Player player) {
////        Inventory inventory = player.getInventory();
//        Slot slot;
//        ItemStack itemStack;
//        ItemStack itemStack2;
//        int l;
//        int m;
//        if (clickType == ClickType.QUICK_CRAFT) {
//            int k = quickcraftStatus;
//            quickcraftStatus = getQuickcraftHeader(j);
//            if ((k != 1 || quickcraftStatus != 2) && k != quickcraftStatus) {
//                resetQuickCraft();
//            } else if (getCarried().isEmpty()) {
//                resetQuickCraft();
//            } else if (quickcraftStatus == 0) {
//                quickcraftType = getQuickcraftType(j);
//                if (isValidQuickcraftType(quickcraftType, player)) {
//                    quickcraftStatus = 1;
//                    quickcraftSlots.clear();
//                } else {
//                    resetQuickCraft();
//                }
//            } else if (quickcraftStatus == 1) {
//                slot = slots.get(i);
//                itemStack = getCarried();
//                if (canItemQuickReplace(slot, itemStack, true) && slot.mayPlace(itemStack) && (quickcraftType == 2 || itemStack.getCount() > quickcraftSlots.size()) && canDragTo(slot)) {
//                    quickcraftSlots.add(slot);
//                }
//            } else if (quickcraftStatus == 2) {
//                if (!quickcraftSlots.isEmpty()) {
//                    if (quickcraftSlots.size() == 1) {
//                        l = ((Slot) quickcraftSlots.iterator().next()).getIndex();
//                        resetQuickCraft();
//                        doClick(l, quickcraftType, ClickType.PICKUP, player);
//                        return;
//                    }
//
//                    itemStack2 = getCarried().copy();
//                    m = getCarried().getCount();
//                    Iterator var9 = quickcraftSlots.iterator();
//
//                    label305:
//                    while (true) {
//                        Slot slot2;
//                        ItemStack itemStack3;
//                        do {
//                            do {
//                                do {
//                                    do {
//                                        if (!var9.hasNext()) {
//                                            itemStack2.setCount(m);
//                                            setCarried(itemStack2);
//                                            break label305;
//                                        }
//
//                                        slot2 = (Slot) var9.next();
//                                        itemStack3 = this.getCarried();
//                                    } while (slot2 == null);
//                                } while (!canItemQuickReplace(slot2, itemStack3, true));
//                            } while (!slot2.mayPlace(itemStack3));
//                        } while (quickcraftType != 2 && itemStack3.getCount() < quickcraftSlots.size());
//
//                        if (canDragTo(slot2)) {
//                            ItemStack itemStack4 = itemStack2.copy();
//                            int n = slot2.hasItem() ? slot2.getItem().getCount() : 0;
//                            getQuickCraftSlotCount(quickcraftSlots, quickcraftType, itemStack4, n);
//                            int o = Math.min(itemStack4.getMaxStackSize(), slot2.getMaxStackSize(itemStack4));
//                            if (itemStack4.getCount() > o) {
//                                itemStack4.setCount(o);
//                            }
//
//                            m -= itemStack4.getCount() - n;
//                            slot2.setItem(itemStack4);
//                        }
//                    }
//                }
//
//                resetQuickCraft();
//            } else {
//                resetQuickCraft();
//            }
//        } else if (quickcraftStatus != 0) {
//            resetQuickCraft();
//        } else {
//            int p;
//            if ((clickType == ClickType.PICKUP || clickType == ClickType.QUICK_MOVE) && (j == 0 || j == 1)) {
//                ClickAction clickAction = j == 0 ? ClickAction.PRIMARY : ClickAction.SECONDARY;
//                if (i == -999) {
//                    if (!this.getCarried().isEmpty()) {
//                        if (clickAction == ClickAction.PRIMARY) {
//                            BukkitStackUtils.drop(getCarried(), player, true);
//                            setCarried(ItemStack.EMPTY);
//                        } else {
//                            BukkitStackUtils.drop(getCarried().split(1), player, true);
//                        }
//                    }
//                } else if (clickType == ClickType.QUICK_MOVE) {
//                    if (i < 0) {
//                        return;
//                    }
//
//                    slot = (Slot) this.slots.get(i);
//                    if (!slot.mayPickup(player)) {
//                        return;
//                    }
//
//                    for (itemStack = quickMoveStack(player, i); !itemStack.isEmpty() && ItemStack.isSame(slot.getItem(), itemStack); itemStack = quickMoveStack(player, i)) {
//                    }
//                } else {
//                    if (i < 0) {
//                        return;
//                    }
//
//                    slot = (Slot) this.slots.get(i);
//                    itemStack = slot.getItem();
//                    ItemStack itemStack5 = this.getCarried();
////                    player.updateTutorialInventoryAction(itemStack5, slot.getItem(), clickAction);
//                    if (!itemStack5.overrideStackedOnOther(slot, clickAction, player) && !itemStack.overrideOtherStackedOnMe(itemStack5, slot, clickAction, player, null)) {
//                        if (itemStack.isEmpty()) {
//                            if (!itemStack5.isEmpty()) {
//                                p = clickAction == ClickAction.PRIMARY ? itemStack5.getCount() : 1;
//                                this.setCarried(slot.safeInsert(itemStack5, p));
//                            }
//                        } else if (slot.mayPickup(player)) {
//                            if (itemStack5.isEmpty()) {
//                                p = clickAction == ClickAction.PRIMARY ? itemStack.getCount() : (itemStack.getCount() + 1) / 2;
//                                Optional<ItemStack> optional = slot.tryRemove(p, Integer.MAX_VALUE, player);
//                                optional.ifPresent((itemStackx) -> {
//                                    this.setCarried(itemStackx);
//                                    slot.onTake(player, itemStackx);
//                                });
//                            } else if (slot.mayPlace(itemStack5)) {
//                                if (ItemStack.isSameItemSameTags(itemStack, itemStack5)) {
//                                    p = clickAction == ClickAction.PRIMARY ? itemStack5.getCount() : 1;
//                                    this.setCarried(slot.safeInsert(itemStack5, p));
//                                } else if (itemStack5.getCount() <= slot.getMaxStackSize(itemStack5)) {
//                                    this.setCarried(itemStack);
//                                    slot.setItem(itemStack5);
//                                }
//                            } else if (ItemStack.isSameItemSameTags(itemStack, itemStack5)) {
//                                Optional<ItemStack> optional2 = slot.tryRemove(itemStack.getCount(), itemStack5.getMaxStackSize() - itemStack5.getCount(), player);
//                                optional2.ifPresent((itemStack2x) -> {
//                                    itemStack5.grow(itemStack2x.getCount());
//                                    slot.onTake(player, itemStack2x);
//                                });
//                            }
//                        }
//                    }
//
//                    slot.setChanged();
//                }
//            } else {
//                Slot slot3;
//                int q;
//                if (clickType == ClickType.SWAP) {
//                } else if (clickType == ClickType.PICKUP_ALL && i >= 0) {
//
//                }
//            }
//        }
    }
//
//    public static int getQuickcraftType(int i) {
//        return i >> 2 & 3;
//    }
//
//    public static int getQuickcraftHeader(int i) {
//        return i & 3;
//    }
//
//    public static int getQuickcraftMask(int i, int j) {
//        return i & 3 | (j & 3) << 2;
//    }
//
//    public static boolean isValidQuickcraftType(int i, Player player) {
//        if (i == 0) {
//            return true;
//        } else if (i == 1) {
//            return true;
//        } else {
//            return i == 2 && player.getAbilities().instabuild;
//        }
//    }
//
//    protected void resetQuickCraft() {
//        this.quickcraftStatus = 0;
//        this.quickcraftSlots.clear();
//    }
//
//    public void broadcastChanges() {
//    }
//
//
    public void setCarried(Player player, ItemStack itemStack) {
        player.setItemOnCursor(BukkitStackUtils.unwrap(itemStack));
    }

    public ItemStack getCarried(Player player) {
        return BukkitStackUtils.wrap(player.getItemOnCursor());
    }

    public static boolean canItemQuickReplace(@Nullable Slot slot, ItemStack itemStack, boolean bl) {
        boolean bl2 = slot == null || !slot.hasItem();
        if (!bl2 && ItemStack.isSameItemSameTags(itemStack, slot.getItem())) {
            return slot.getItem().getCount() + (bl ? 0 : itemStack.getCount()) <= itemStack.getMaxStackSize();
        } else {
            return bl2;
        }
    }

//    public static void getQuickCraftSlotCount(Set<Slot> set, int i, ItemStack itemStack, int j) {
//        switch (i) {
//            case 0:
//                itemStack.setCount(Mth.floor((float) itemStack.getCount() / (float) set.size()));
//                break;
//            case 1:
//                itemStack.setCount(1);
//                break;
//            case 2:
//                itemStack.setCount(itemStack.getMaxStackSize());
//        }
//
//        itemStack.grow(j);
//    }
//
//    public boolean canDragTo(Slot slot) {
//        return true;
//    }
//
    public boolean canTakeItemForPickAll(ItemStack itemStack, Slot slot) {
        return true;
    }
}
