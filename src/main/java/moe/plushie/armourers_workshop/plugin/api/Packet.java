package moe.plushie.armourers_workshop.plugin.api;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class Packet<T> {

    private final byte[] bytes;
    private final ByteBuf buffer;
    private final String channel;

    public Packet(String channel, FriendlyByteBuf buf) {
        this.channel = channel;
        this.buffer = Unpooled.copiedBuffer(buf);
        this.bytes = buffer.array();
    }

    public String getChannel() {
        return channel;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
