package moe.plushie.armourers_workshop.plugin.mixin;

import moe.plushie.armourers_workshop.plugin.annotation.Available;
import moe.plushie.armourers_workshop.plugin.helper.ItemHelper;
import net.minecraft.network.Connection;
import net.minecraftforge.network.NetworkHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Available("[1.18, )")
@Mixin(NetworkHooks.class)
public class ConnectionTypeMixin {

    @Inject(method = "handleClientLoginSuccess", at = @At("HEAD"), remap = false)
    private static void aw$updateConnectType(Connection manager, CallbackInfo cir) {
        ItemHelper.setEnableRedirect(NetworkHooks.isVanillaConnection(manager));
    }
}
