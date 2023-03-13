package moe.plushie.armourers_workshop.plugin.mixin;

import moe.plushie.armourers_workshop.plugin.helper.ItemHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PacketBuffer.class)
public abstract class PacketBufferMixin {

    @Inject(method = "readItem", at = @At("RETURN"), cancellable = true)
    public void aw$readItem(CallbackInfoReturnable<ItemStack> cir) {
        ItemStack itemStack = cir.getReturnValue();
        CompoundNBT itemTag = itemStack.getTag();
        Item item = ItemHelper.getRedirectedItem(itemStack.getItem(), itemStack.getTag());
        if (item != null) {
            itemStack = new ItemStack(item, itemStack.getCount());
            itemStack.readShareTag(itemTag);
            cir.setReturnValue(itemStack);
        }
    }

    @ModifyVariable(method = "writeItem", at = @At("HEAD"), argsOnly = true)
    public ItemStack aw$writeItem(ItemStack itemStack) {
        ItemStack redirectedStack = ItemHelper.setRedirectedItem(itemStack);
        if (redirectedStack != null) {
            return redirectedStack;
        }
        return itemStack;
    }

    @ModifyVariable(method = "writeItemStack", at = @At("HEAD"), argsOnly = true, remap = false, require = 1)
    public ItemStack aw$writeItemStack(ItemStack originItemStack, ItemStack itemStack, boolean flag) {
        ItemStack redirectedStack = ItemHelper.setRedirectedItem(itemStack);
        if (redirectedStack != null) {
            return redirectedStack;
        }
        return itemStack;
    }
}
