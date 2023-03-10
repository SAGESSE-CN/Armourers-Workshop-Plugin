package moe.plushie.armourers_workshop.plugin.utils;

import moe.plushie.armourers_workshop.plugin.api.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BukkitStackUtils {

    public static ItemStack wrap(org.bukkit.inventory.ItemStack itemStack) {
        if (itemStack == null || itemStack.getAmount() <= 0) {
            return ItemStack.EMPTY;
        }
        // TODO: IMPL
        return null;
    }

    public static org.bukkit.inventory.ItemStack unwrap(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return null;
        }
        // TODO: IMPL
        return null;
    }

    public static void giveItemTo(ItemStack itemStack, Player player) {
        Inventory inventory = player.getInventory();
        if (isFull(inventory)) {
            player.getWorld().dropItem(player.getLocation(), unwrap(itemStack));
        } else {
            inventory.addItem(unwrap(itemStack));
        }
    }

    public static boolean isFull(Inventory inventory) {
        return inventory.firstEmpty() < 0;
    }
}
