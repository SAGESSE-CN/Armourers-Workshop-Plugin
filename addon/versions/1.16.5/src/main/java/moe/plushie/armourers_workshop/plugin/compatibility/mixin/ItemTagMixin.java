package moe.plushie.armourers_workshop.plugin.compatibility.mixin;

import net.minecraft.tags.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.tags.StaticTagHelper$Wrapper")
public class ItemTagMixin<T> {

    @Shadow Tag<T> tag;

    @Inject(method = "contains", at = @At("HEAD"), cancellable = true)
    public void aw$contains(T value, CallbackInfoReturnable<Boolean> cir) {
        if (tag == null) {
            cir.setReturnValue(false);
        }
    }

}
