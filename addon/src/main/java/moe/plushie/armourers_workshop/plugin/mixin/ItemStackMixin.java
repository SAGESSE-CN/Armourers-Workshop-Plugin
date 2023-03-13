package moe.plushie.armourers_workshop.plugin.mixin;

import moe.plushie.armourers_workshop.plugin.helper.ItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @ModifyVariable(method = "<init>(Lnet/minecraft/nbt/CompoundNBT;)V", at = @At("HEAD"), argsOnly = true)
    private static CompoundNBT aw$read(CompoundNBT tag) {
        String id = ItemHelper.getRedirectedId(tag);
        if (id != null) {
            CompoundNBT newTag = tag.copy();
            newTag.putString("id", id);
            return newTag;
        }
        return tag;
    }

    @Inject(method = "save", at = @At("RETURN"))
    public void aw$save(CompoundNBT tag, CallbackInfoReturnable<CompoundNBT> cir) {
        CompoundNBT itemTag = cir.getReturnValue();
        String id = itemTag.getString("id");
        if (ItemHelper.shouldRedirectId(id)) {
            itemTag.putString("id", "minecraft:paper");
            ItemHelper.setRedirectedId(tag, id);
        }
    }
}
