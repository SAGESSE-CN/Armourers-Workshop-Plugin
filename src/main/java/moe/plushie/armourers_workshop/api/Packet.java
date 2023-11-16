package moe.plushie.armourers_workshop.api;

import net.cocoonmc.core.network.FriendlyByteBuf;
import net.cocoonmc.core.resources.ResourceLocation;

public class Packet<T> {

    private final FriendlyByteBuf buffer;
    private final ResourceLocation channel;

    public Packet(ResourceLocation channel, FriendlyByteBuf buf) {
        this.channel = channel;
        this.buffer = buf;
    }

    public ResourceLocation getChannel() {
        return channel;
    }

    public FriendlyByteBuf getBuffer() {
        return buffer;
    }
}
