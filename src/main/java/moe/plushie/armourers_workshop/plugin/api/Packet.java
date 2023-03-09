package moe.plushie.armourers_workshop.plugin.api;

public class Packet<T> {

    private final byte[] bytes;
    private final FriendlyByteBuf buf;
    private final String channel;

    public Packet(String channel, FriendlyByteBuf buf) {
        this.channel = channel;
        this.buf = buf;
        this.bytes = buf.array();
    }

    public String getChannel() {
        return channel;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
