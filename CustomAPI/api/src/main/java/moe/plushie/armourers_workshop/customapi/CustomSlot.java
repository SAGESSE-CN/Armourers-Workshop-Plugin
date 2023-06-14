package moe.plushie.armourers_workshop.customapi;

import moe.plushie.armourers_workshop.customapi.version.Versions;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CustomSlot implements CustomSlotProvider {

    public int index;

    protected final Inventory inventory;
    protected final int slot;
    protected final CustomSlotImpl impl;

    public CustomSlot(Inventory inventory, int index, int x, int y) {
        this.inventory = inventory;
        this.slot = index;
        this.impl = Versions.API.createCustomSlot(this, inventory, index, x, y);
    }

    @Override
    public boolean hasItem() {
        return impl.hasContents();
    }

    @Override
    public ItemStack getItem() {
        return impl.getContents();
    }

    @Override
    public void set(ItemStack itemStack) {
        impl.setContents(itemStack);
    }

    @Override
    public ItemStack remove(int size) {
        return impl.removeContents(size);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return impl.mayPlace(itemStack);
    }

    @Override
    public boolean mayPickup(Player player) {
        return impl.mayPickup(player);
    }

    @Override
    public void setChanged() {
        impl.setChanged();
    }

    public int getSlot() {
        return slot;
    }
}
