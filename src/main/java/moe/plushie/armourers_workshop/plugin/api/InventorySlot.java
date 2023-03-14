package moe.plushie.armourers_workshop.plugin.api;

import moe.plushie.armourers_workshop.plugin.utils.BukkitUtils;
import org.bukkit.inventory.Inventory;

public class InventorySlot implements Slot {

    private int index;

    private final Inventory inventory;
    private final int slot;

    public InventorySlot(Inventory inventory, int slot) {
        this.inventory = inventory;
        this.slot = slot;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public ItemStack getItem() {
        return BukkitUtils.wrap(inventory.getItem(slot));
    }

    @Override
    public boolean hasItem() {
        return !getItem().isEmpty();
    }

    @Override
    public int getMaxStackSize() {
        return inventory.getMaxStackSize();
    }

    @Override
    public void set(ItemStack itemStack) {
        inventory.setItem(slot, BukkitUtils.unwrap(itemStack));
        setChanged();
    }

    @Override
    public void setChanged() {
    }
}
