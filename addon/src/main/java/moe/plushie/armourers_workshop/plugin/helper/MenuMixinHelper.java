package moe.plushie.armourers_workshop.plugin.helper;

import io.netty.buffer.Unpooled;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class MenuMixinHelper {

    public static FriendlyByteBuf redirect(FriendlyByteBuf buffer) {
        int index = buffer.readerIndex();
        int id = buffer.readVarInt();
        if (id == Integer.MAX_VALUE) {
            ResourceLocation rl = buffer.readResourceLocation();
            FriendlyByteBuf resolvedBuffer = new FriendlyByteBuf(Unpooled.buffer(4 + buffer.readableBytes()));
            resolvedBuffer.writeVarInt(Registry.MENU.getId(Registry.MENU.get(rl)));
            resolvedBuffer.writeBytes(buffer);
            return resolvedBuffer;
        }
        buffer.readerIndex(index);
        return buffer;
    }
}
