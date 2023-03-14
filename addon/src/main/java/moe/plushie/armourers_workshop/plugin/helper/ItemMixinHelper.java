package moe.plushie.armourers_workshop.plugin.helper;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class ItemMixinHelper {

    public static void readItem(CallbackInfoReturnable<ItemStack> cir) {
        ItemStack itemStack = cir.getReturnValue();
        Item targetItem = ItemHelper.getItem(itemStack, 0);
        if (targetItem != null) {
            ItemStack newItemStack = new ItemStack(targetItem, itemStack.getCount());
            newItemStack.readShareTag(itemStack.getTag());
            cir.setReturnValue(newItemStack);
        }
    }

    public static ItemStack writeItem(ItemStack itemStack) {
        ItemStack redirectedStack = ItemHelper.wrapItemStack(itemStack);
        if (redirectedStack != null) {
            return redirectedStack;
        }
        return itemStack;
    }

}
