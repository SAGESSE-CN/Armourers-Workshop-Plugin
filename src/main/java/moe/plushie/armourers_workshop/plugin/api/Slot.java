package moe.plushie.armourers_workshop.plugin.api;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Slot {

    private final Inventory inventory;
    private final int slot;

    public Slot(Inventory inventory, int slot) {
        this.inventory = inventory;
        this.slot = slot;
    }


    public ItemStack getItem() {
        return inventory.getItem(slot);
    }

    public boolean hasItem() {
        return false;
//        return !getItem().isEmpty();
    }

    public void set(ItemStack itemStack) {
        inventory.setItem(slot, itemStack);
        setChanged();
    }

    public void setChanged() {
    }
}
