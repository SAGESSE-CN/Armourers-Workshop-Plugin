package moe.plushie.armourers_workshop.plugin.utils;

import moe.plushie.armourers_workshop.plugin.api.ItemStack;
import net.querz.nbt.io.SNBTUtil;
import net.querz.nbt.tag.CompoundTag;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BukkitStackUtils {

    public static ItemStack wrap(org.bukkit.inventory.ItemStack itemStack) {
        if (itemStack == null || itemStack.getAmount() <= 0) {
            return ItemStack.EMPTY;
        }
        return FastCache.BUKKIT_ITEM_TO_ITEM.computeIfAbsent(itemStack, WrappedItemStack::new);
    }

    public static org.bukkit.inventory.ItemStack unwrap(ItemStack itemStack) {
        // yep, we not need convert it.
        if (itemStack instanceof WrappedItemStack) {
            return ((WrappedItemStack) itemStack).itemStack;
        }
        if (itemStack.isEmpty()) {
            return null;
        }
        return FastCache.ITEM_TO_BUKKIT_ITEM.computeIfAbsent(itemStack, it -> {
            try {
                CompoundTag tag = new CompoundTag();
                itemStack.save(tag);
                return NBTEditor.getItemFromTag(NBTEditor.NBTCompound.fromJson(SNBTUtil.toSNBT(tag)));
            } catch (Exception e) {
                return null;
            }
        });
    }

    public static void giveItemTo(ItemStack itemStack, Player player) {
        Inventory inventory = player.getInventory();
        if (isFull(inventory)) {
            org.bukkit.inventory.ItemStack itemStack1 = unwrap(itemStack);
            if (itemStack1 != null) {
                player.getWorld().dropItem(player.getLocation(), itemStack1);
            }
        } else {
            inventory.addItem(unwrap(itemStack));
        }
    }

    public static boolean isFull(Inventory inventory) {
        return inventory.firstEmpty() < 0;
    }

    public static class WrappedItemStack extends ItemStack {

        private CompoundTag lazyTag;
        private final org.bukkit.inventory.ItemStack itemStack;

        public WrappedItemStack(org.bukkit.inventory.ItemStack itemStack) {
            super(realId(itemStack), itemStack.getAmount());
            this.itemStack = itemStack;
        }

        @Override
        public CompoundTag getTag() {
            if (lazyTag != null) {
                return lazyTag;
            }
            lazyTag = super.getTag();
            try {
                NBTEditor.NBTCompound tag = NBTEditor.getNBTCompound(itemStack, "tag");
                if (tag != null) {
                    lazyTag = (CompoundTag) SNBTUtil.fromSNBT(tag.toJson());
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return lazyTag;
        }

        private static String realId(org.bukkit.inventory.ItemStack itemStack) {
            String id = itemStack.getType().getKey().toString();
            String redirectedId = NBTEditor.getString(itemStack, "__redirected_id__");
            if (redirectedId != null && !redirectedId.isEmpty()) {
                return redirectedId;
            }
            return id;
        }

    }
}
