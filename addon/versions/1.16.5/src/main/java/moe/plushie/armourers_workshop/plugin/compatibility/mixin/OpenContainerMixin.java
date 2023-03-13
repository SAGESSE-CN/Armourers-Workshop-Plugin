package moe.plushie.armourers_workshop.plugin.compatibility.mixin;

import moe.plushie.armourers_workshop.plugin.helper.MenuMixinHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.network.FMLPlayMessages;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FMLPlayMessages.OpenContainer.class)
public class OpenContainerMixin {

    @ModifyVariable(method = "decode", at = @At("HEAD"), argsOnly = true, remap = false)
    private static FriendlyByteBuf aw$decode(FriendlyByteBuf buffer) {
        return MenuMixinHelper.redirect(buffer);
    }
}
