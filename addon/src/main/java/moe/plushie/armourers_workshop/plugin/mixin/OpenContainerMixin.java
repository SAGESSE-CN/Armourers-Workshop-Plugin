package moe.plushie.armourers_workshop.plugin.mixin;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Pseudo
@Mixin(targets = {
        "net.minecraftforge.fml.network.FMLPlayMessages$OpenContainer",
        "net.minecraftforge.network.PlayMessages$OpenContainer"
})
public class OpenContainerMixin {

    @ModifyVariable(method = "decode", at = @At("HEAD"), argsOnly = true, remap = false, require = 0)
    private static PacketBuffer aw$decode(PacketBuffer buffer) {
        int index = buffer.readerIndex();
        int id = buffer.readVarInt();
        if (id == Integer.MAX_VALUE) {
            ResourceLocation rl = buffer.readResourceLocation();
            PacketBuffer resolvedBuffer = new PacketBuffer(Unpooled.buffer(4 + buffer.readableBytes()));
            resolvedBuffer.writeVarInt(Registry.MENU.getId(Registry.MENU.get(rl)));
            resolvedBuffer.writeBytes(buffer);
            return resolvedBuffer;
        }
        buffer.readerIndex(index);
        return buffer;
    }
}
