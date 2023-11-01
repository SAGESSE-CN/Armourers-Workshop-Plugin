package moe.plushie.armourers_workshop.api;

import net.cocoonmc.core.network.FriendlyByteBuf;

public interface IEntitySerializer<T> {

    T read(FriendlyByteBuf buffer);

    void write(FriendlyByteBuf buffer, T value);
}
