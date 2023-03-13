package moe.plushie.armourers_workshop.plugin.helper;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class ItemHelper {

    public static final String REDIRECT_KEY = "__redirected_id__";

    public static boolean shouldRedirectId(String id) {
        return !id.startsWith("minecraft:");
    }

    public static boolean shouldRedirectId(ResourceLocation rl) {
        return !rl.getNamespace().equals("minecraft");
    }


    @Nullable
    public static String getRedirectedId(CompoundNBT tag) {
        if (tag == null) {
            return null;
        }
        String id = tag.getString("id");
        if (!shouldRedirectId(id)) {
            return null;
        }
        id = tag.getCompound("tag").getString(REDIRECT_KEY);
        if (id.isEmpty()) {
            return null;
        }
        return id;
    }

    public static void setRedirectedId(CompoundNBT tag, String id) {
        CompoundNBT itemTag = tag.getCompound("tag");
        itemTag.putString(REDIRECT_KEY, id);
        if (!tag.contains("tag")) {
            tag.put("tag", itemTag);
        }
    }

    @Nullable
    public static Item getRedirectedItem(Item item, CompoundNBT itemTag) {
        if (itemTag == null) {
            return null;
        }
        String id = itemTag.getString(REDIRECT_KEY);
        if (id.isEmpty()) {
            return null;
        }
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
    }

    @Nullable
    public static ItemStack setRedirectedItem(ItemStack itemStack) {
        if (itemStack == ItemStack.EMPTY) {
            return null;
        }
        ResourceLocation rl = ForgeRegistries.ITEMS.getKey(itemStack.getItem());
        if (rl == null || !shouldRedirectId(rl)) {
            return null;
        }
        CompoundNBT tag = new CompoundNBT();
        tag.putString(REDIRECT_KEY, rl.toString());
        if (itemStack.getTag() != null) {
            tag.merge(itemStack.getTag());
        }
        itemStack = new ItemStack(Items.PAPER, itemStack.getCount());
        itemStack.setTag(tag);
        return itemStack;
    }
}
