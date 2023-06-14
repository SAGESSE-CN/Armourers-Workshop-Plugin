package moe.plushie.armourers_workshop.customapi;

import moe.plushie.armourers_workshop.customapi.version.Versions;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class CustomMenu implements CustomMenuProvider {

    protected final ArrayList<CustomSlot> slots = new ArrayList<>();
    protected final CustomMenuImpl impl;

    public CustomMenu(Player player, int size, String title) {
        this.impl = Versions.API.createCustomMenu(this, player, null, size, title);
    }

    public void open() {
        impl.openContainer();
    }

    public void close() {
        impl.closeContainer();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return slots.get(index).getItem();
    }

    @Override
    public void broadcastChanges() {
        impl.broadcastChanges();
    }

    @Override
    public void handleOpenWindowPacket(int windowId) {
        impl.sendOpenWindowPacket(windowId);
    }

    @Override
    public void handleCloseWindowPacket(int windowId) {
        impl.sendCloseWindowPacket(windowId);
    }

    public boolean moveItemStackTo(ItemStack itemStack, int i, int j, boolean bl) {
        return impl.moveItemStackTo(itemStack, i, j, bl);
    }

    public CustomSlot addSlot(CustomSlot slot) {
        slot.index = slots.size();
        slots.add(slot);
        impl.addSlot(slot.impl);
        return slot;
    }

    public Inventory getInventory() {
        return impl.getInventory();
    }
}
