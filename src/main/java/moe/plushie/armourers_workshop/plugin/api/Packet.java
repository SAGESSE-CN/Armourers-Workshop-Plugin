package moe.plushie.armourers_workshop.plugin.api;

public class Packet<T> {

    private final byte[] bytes;
    private final FriendlyByteBuf buf;
    public Packet(FriendlyByteBuf buf) {
        this.buf = buf;
        this.bytes = buf.array();
    }

    public byte[] getBytes() {
        return bytes;
    }
}
