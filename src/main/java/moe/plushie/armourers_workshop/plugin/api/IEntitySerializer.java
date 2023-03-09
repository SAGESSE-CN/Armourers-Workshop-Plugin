package moe.plushie.armourers_workshop.plugin.api;

public interface IEntitySerializer<T> {

    T read(FriendlyByteBuf buffer);

    void write(FriendlyByteBuf buffer, T descriptor);
}
