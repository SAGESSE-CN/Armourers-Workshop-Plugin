package moe.plushie.armourers_workshop.customapi;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface CustomSlotImpl {

    boolean hasContents();

    ItemStack getContents();

    void setContents(ItemStack itemStack);

    ItemStack removeContents(int i);

    boolean mayPlace(ItemStack itemStack);

    boolean mayPickup(Player player);

    void setChanged();
}
