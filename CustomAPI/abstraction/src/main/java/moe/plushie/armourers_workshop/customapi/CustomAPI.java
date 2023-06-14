package moe.plushie.armourers_workshop.customapi;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public interface CustomAPI {

    CustomSlotImpl createCustomSlot(CustomSlotProvider impl, Inventory inventory, int index, int x, int y);

    CustomMenuImpl createCustomMenu(CustomMenuProvider impl, Player player, InventoryHolder holder, int size, String title);
}
