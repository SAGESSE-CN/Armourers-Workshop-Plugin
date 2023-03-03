package moe.plushie.armourers_workshop.plugin.data.impl;

import net.querz.nbt.tag.Tag;

public interface ITagRepresentable<T extends Tag> {

    T serializeNBT();

    void deserializeNBT(T nbt);
}
