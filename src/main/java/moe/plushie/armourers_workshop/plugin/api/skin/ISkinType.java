package moe.plushie.armourers_workshop.plugin.api.skin;

import net.cocoonmc.core.resources.ResourceLocation;

import java.util.List;

public interface ISkinType {

    int getId();

    ResourceLocation getRegistryName();

    List<? extends ISkinPartType> getParts();
}
