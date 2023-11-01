package moe.plushie.armourers_workshop.api;

import net.cocoonmc.core.nbt.Tag;

public interface ITagRepresentable<T extends Tag> {

    T serializeNBT();

    void deserializeNBT(T nbt);
}
