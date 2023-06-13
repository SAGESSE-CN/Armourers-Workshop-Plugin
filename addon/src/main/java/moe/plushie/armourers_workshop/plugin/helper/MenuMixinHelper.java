package moe.plushie.armourers_workshop.plugin.helper;

import io.netty.buffer.Unpooled;
import moe.plushie.armourers_workshop.plugin.annotation.Available;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

@Available("[1.19, )")
public class MenuMixinHelper {

    public static FriendlyByteBuf redirect(FriendlyByteBuf buffer) {
        int index = buffer.readerIndex();
        int id = buffer.readVarInt();
        if (id == Integer.MAX_VALUE) {
            ResourceLocation rl = buffer.readResourceLocation();
            FriendlyByteBuf resolvedBuffer = new FriendlyByteBuf(Unpooled.buffer(4 + buffer.readableBytes()));
            resolvedBuffer.writeVarInt(((ForgeRegistry<MenuType<?>>) ForgeRegistries.MENU_TYPES).getID(rl));
            resolvedBuffer.writeBytes(buffer);
            return resolvedBuffer;
        }
        buffer.readerIndex(index);
        return buffer;
    }
}
