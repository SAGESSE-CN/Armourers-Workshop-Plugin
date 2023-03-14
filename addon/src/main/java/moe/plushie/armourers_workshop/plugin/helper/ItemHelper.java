package moe.plushie.armourers_workshop.plugin.helper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashMap;

public class ItemHelper {

    private static final HashMap<String, Item> ID_TO_ITEMS = new HashMap<>();
    private static final HashMap<Item, String> ITEM_TO_IDS = new HashMap<>();

    public static final String REDIRECT_KEY = "__redirected_id__";

    public static Item findItem(String id) {
        return ID_TO_ITEMS.computeIfAbsent(id, it -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(id)));
    }

    public static String findItemId(Item item) {
        return ITEM_TO_IDS.computeIfAbsent(item, it -> {
            ResourceLocation rl = ForgeRegistries.ITEMS.getKey(it);
            if (rl != null) {
                return rl.toString();
            }
            return null;
        });
    }

    public static boolean shouldRedirectId(String id) {
        return !id.startsWith("minecraft:");
    }

    @Nullable
    public static String getId(CompoundTag tag, int index) {
        if (tag == null) {
            return null;
        }
        return _splitId(tag.getCompound("tag").getString(REDIRECT_KEY), index);
    }

    public static void setId(CompoundTag tag, String sourceId, String targetId) {
        CompoundTag itemTag = tag.getCompound("tag");
        itemTag.putString(REDIRECT_KEY, _makeId(sourceId, targetId));
        if (!tag.contains("tag")) {
            tag.put("tag", itemTag);
        }
    }

    @Nullable
    public static Item getItem(ItemStack itemStack, int index) {
        CompoundTag itemTag = itemStack.getTag();
        if (itemTag == null) {
            return null;
        }
        String id = _splitId(itemTag.getString(REDIRECT_KEY), index);
        if (id == null) {
            return null;
        }
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
    }

    public static void setItem(ItemStack itemStack, Item sourceItem, Item targetItem) {
        String sourceId = findItemId(sourceItem);
        String targetId = findItemId(targetItem);
        if (sourceId != null && targetId != null) {
            CompoundTag tag = itemStack.getOrCreateTag();
            tag.putString(REDIRECT_KEY, _makeId(sourceId, targetId));
        }
    }

    @Nullable
    public static ItemStack wrapItemStack(ItemStack itemStack) {
        if (itemStack == ItemStack.EMPTY) {
            return null;
        }
        String id = findItemId(itemStack.getItem());
        if (id == null || !shouldRedirectId(id)) {
            return null;
        }
        Item targetItem = itemStack.getItem();
        Item sourceItem = getItem(itemStack, 1);
        if (sourceItem == null) {
            sourceItem = getMatchItemBySize(targetItem);
        }
        ItemStack newItemStack = new ItemStack(sourceItem, itemStack.getCount());
        newItemStack.setTag(itemStack.getTag());
        setItem(newItemStack, sourceItem, targetItem);
        return newItemStack;
    }

    public static String getMatchIdBySize(String id) {
        Item target = findItem(id);
        if (target != null) {
            Item item = getMatchItemBySize(target);
            String newId = findItemId(item);
            if (newId != null) {
                return newId;
            }
        }
        return "minecraft:paper";
    }

    public static Item getMatchItemBySize(Item item) {
        switch (item.getMaxStackSize()) {
            case 1: {
                return Items.FLOWER_BANNER_PATTERN;
            }
            case 16: {
                return Items.WHITE_BANNER;
            }
            default: {
                return Items.PAPER;
            }
        }
    }

    private static String _splitId(String id, int index) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        String[] ids = id.split("/");
        if (index < ids.length) {
            return ids[index];
        }
        return null;
    }

    private static String _makeId(String source, String target) {
        return target + "/" + source;
    }
}
