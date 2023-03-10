package moe.plushie.armourers_workshop.plugin.api;

import org.bukkit.entity.Player;

public interface Slot {

     ItemStack getItem();

    boolean hasItem();

    void set(ItemStack itemStack);

    default boolean mayPlace(ItemStack itemStack) {
        return true;
    }

    default boolean mayPickup(Player player) {
        return true;
    }

    default void setChanged() {

    }
}
