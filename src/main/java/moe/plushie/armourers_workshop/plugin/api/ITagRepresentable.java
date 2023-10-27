package moe.plushie.armourers_workshop.plugin.api;

import net.cocoonmc.core.nbt.Tag;

public interface ITagRepresentable<T extends Tag> {

    T serializeNBT();

    void deserializeNBT(T nbt);
}
