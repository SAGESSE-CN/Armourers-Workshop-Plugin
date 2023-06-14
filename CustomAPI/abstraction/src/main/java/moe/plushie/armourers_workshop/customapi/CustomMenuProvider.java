package moe.plushie.armourers_workshop.customapi;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface CustomMenuProvider {

    boolean stillValid(Player player);

    ItemStack quickMoveStack(Player player, int index);

    void broadcastChanges();

    void handleOpenWindowPacket(int windowId);

    void handleCloseWindowPacket(int windowId);
}
