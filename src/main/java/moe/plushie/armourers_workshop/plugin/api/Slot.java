package moe.plushie.armourers_workshop.plugin.api;

import org.bukkit.entity.Player;

import java.util.Optional;

public interface Slot {

    int getIndex();

    void setIndex(int index);

    ItemStack getItem();

    default ItemStack removeItem(int i) {
        ItemStack itemStack = getItem();
        if (itemStack.getCount() > i) {
            return itemStack.split(i);
        }
        set(ItemStack.EMPTY);
        return itemStack;
    }

    void set(ItemStack itemStack);

    boolean hasItem();

    default void onSwapCraft(int i) {
    }

    default void onTake(Player player, ItemStack itemStack) {
        this.setChanged();
    }

    default int getMaxStackSize() {
        return 64;
    }

    default int getMaxStackSize(ItemStack itemStack) {
        return Math.min(getMaxStackSize(), itemStack.getMaxStackSize());
    }

    default boolean mayPlace(ItemStack itemStack) {
        return true;
    }

    default boolean mayPickup(Player player) {
        return true;
    }

    default void setChanged() {

    }

    default Optional<ItemStack> tryRemove(int i, int j, Player player) {
        if (!mayPickup(player)) {
            return Optional.empty();
        }
        if (!allowModification(player) && j < getItem().getCount()) {
            return Optional.empty();
        }
        i = Math.min(i, j);
        ItemStack itemStack = removeItem(i);
        if (itemStack.isEmpty()) {
            return Optional.empty();
        }
        if (this.getItem().isEmpty()) {
            this.set(ItemStack.EMPTY);
        }
        return Optional.of(itemStack);
    }

    default ItemStack safeTake(int i, int j, Player player) {
        Optional<ItemStack> optional = tryRemove(i, j, player);
        optional.ifPresent((itemStack) -> {
            this.onTake(player, itemStack);
        });
        return optional.orElse(ItemStack.EMPTY);
    }

    default ItemStack safeInsert(ItemStack itemStack) {
        return safeInsert(itemStack, itemStack.getCount());
    }

    default ItemStack safeInsert(ItemStack itemStack, int i) {
        if (!itemStack.isEmpty() && this.mayPlace(itemStack)) {
            ItemStack itemStack2 = this.getItem();
            int j = Math.min(Math.min(i, itemStack.getCount()), this.getMaxStackSize(itemStack) - itemStack2.getCount());
            if (itemStack2.isEmpty()) {
                this.set(itemStack.split(j));
            } else if (ItemStack.isSameItemSameTags(itemStack2, itemStack)) {
                itemStack.shrink(j);
                itemStack2.grow(j);
                this.set(itemStack2);
            }

            return itemStack;
        } else {
            return itemStack;
        }
    }

    default boolean allowModification(Player player) {
        return this.mayPickup(player) && this.mayPlace(this.getItem());
    }
}
