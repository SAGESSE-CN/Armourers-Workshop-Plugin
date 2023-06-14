package moe.plushie.armourers_workshop.customapi;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface CustomSlotProvider {

    boolean hasItem();

    ItemStack getItem();

    void set(ItemStack itemStack);

    ItemStack remove(int i);

    boolean mayPlace(ItemStack itemStack);

    boolean mayPickup(Player player);

    void setChanged();
}
