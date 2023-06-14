package moe.plushie.armourers_workshop.customapi;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface CustomMenuImpl {

    void broadcastChanges();

    boolean moveItemStackTo(ItemStack itemStack, int i, int j, boolean bl);

    void addSlot(CustomSlotImpl slot);

    void openContainer();

    void closeContainer();

    void sendOpenWindowPacket(int windowId);

    void sendCloseWindowPacket(int windowId);

    Inventory getInventory();
}
