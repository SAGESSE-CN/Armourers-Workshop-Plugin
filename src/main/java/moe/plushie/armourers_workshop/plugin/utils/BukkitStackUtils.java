package moe.plushie.armourers_workshop.plugin.utils;

import moe.plushie.armourers_workshop.plugin.api.ItemStack;

public interface BukkitStackUtils {

    static ItemStack wrap(org.bukkit.inventory.ItemStack itemStack) {
        if (itemStack == null || itemStack.getAmount() <= 0) {
            return ItemStack.EMPTY;
        }
        // TODO: IMPL
        return null;
    }

    static org.bukkit.inventory.ItemStack unwrap(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return null;
        }
        // TODO: IMPL
        return null;
    }
}
