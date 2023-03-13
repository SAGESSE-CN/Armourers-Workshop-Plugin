package moe.plushie.armourers_workshop.plugin.helper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class ItemMixinHelper {

    public static void readItem(CallbackInfoReturnable<ItemStack> cir) {
        ItemStack itemStack = cir.getReturnValue();
        CompoundTag itemTag = itemStack.getTag();
        Item item = ItemHelper.getRedirectedItem(itemStack.getItem(), itemStack.getTag());
        if (item != null) {
            itemStack = new ItemStack(item, itemStack.getCount());
            itemStack.readShareTag(itemTag);
            cir.setReturnValue(itemStack);
        }
    }

    public static ItemStack writeItem(ItemStack itemStack) {
        ItemStack redirectedStack = ItemHelper.setRedirectedItem(itemStack);
        if (redirectedStack != null) {
            return redirectedStack;
        }
        return itemStack;
    }
}
