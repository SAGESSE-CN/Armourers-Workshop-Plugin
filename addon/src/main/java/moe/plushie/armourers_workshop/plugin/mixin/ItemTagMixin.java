package moe.plushie.armourers_workshop.plugin.mixin;

import net.minecraft.tags.ITag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.tags.TagRegistry$NamedTag")
public class ItemTagMixin<T> {

    @Shadow ITag<T> tag;

    @Inject(method = "contains", at = @At("HEAD"), cancellable = true)
    public void aw$contains(T value, CallbackInfoReturnable<Boolean> cir) {
        if (tag == null) {
            cir.setReturnValue(false);
        }
    }

}
