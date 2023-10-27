package moe.plushie.armourers_workshop.plugin.core.skin;

import moe.plushie.armourers_workshop.plugin.api.skin.ISkinType;
import net.cocoonmc.core.resources.ResourceLocation;

import java.util.HashMap;
import java.util.function.Function;

public class EntityProfile {

    private final ResourceLocation registryName;
    private final HashMap<ISkinType, Function<ISkinType, Integer>> supports;
    private final boolean locked;

    public EntityProfile(ResourceLocation registryName, HashMap<ISkinType, Function<ISkinType, Integer>> supports, boolean locked) {
        this.registryName = registryName;
        this.supports = supports;
        this.locked = locked;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean canSupport(ISkinType type) {
        return supports.containsKey(type);
    }

    public int getMaxCount(ISkinType type) {
        Function<ISkinType, Integer> provider = supports.get(type);
        if (provider != null) {
            return provider.apply(type);
        }
        return 0;
    }

    public ResourceLocation getRegistryName() {
        return registryName;
    }
}
