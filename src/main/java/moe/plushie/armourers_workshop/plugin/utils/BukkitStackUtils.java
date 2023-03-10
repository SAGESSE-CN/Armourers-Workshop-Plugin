package moe.plushie.armourers_workshop.plugin.utils;

import moe.plushie.armourers_workshop.plugin.api.ItemStack;
import net.querz.nbt.io.SNBTUtil;
import net.querz.nbt.tag.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.IOException;
import java.util.Map;

public class BukkitStackUtils {

    public static ItemStack wrap(org.bukkit.inventory.ItemStack itemStack) {
        if (itemStack == null || itemStack.getAmount() <= 0) {
            return ItemStack.EMPTY;
        }
        ItemStack item = ItemStack.EMPTY;
        try {
            NBTEditor.NBTCompound itemCompound = NBTEditor.getEmptyNBTCompound();
            itemCompound.set(itemStack.getType().getKey().toString(), "id");
            NBTEditor.NBTCompound tag = NBTEditor.getNBTCompound(itemStack, "tag");
            if (tag != null) {
                itemCompound.set(tag, "tag");
            }
            itemCompound.set((byte) itemStack.getAmount(), "Count");
            CompoundTag tag1 = (CompoundTag) SNBTUtil.fromSNBT(itemCompound.toJson());
            item = new ItemStack(tag1);
        } catch (Exception ignored) {
        }
        return item;
    }

    public static org.bukkit.inventory.ItemStack unwrap(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return null;
        }
        org.bukkit.inventory.ItemStack bukkitItem = new org.bukkit.inventory.ItemStack(Material.PAPER);
        try {
            CompoundTag tag = new CompoundTag();
            itemStack.save(tag);
            String nbt = SNBTUtil.toSNBT(tag);
            org.bukkit.inventory.ItemStack itemFromTag = NBTEditor.getItemFromTag(NBTEditor.getNBTCompound(nbt));
            if (itemFromTag != null) {
                bukkitItem = itemFromTag;
            }
        } catch (Exception ignored) {
        }
        return bukkitItem;
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
