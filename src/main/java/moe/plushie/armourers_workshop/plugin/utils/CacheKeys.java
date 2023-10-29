package moe.plushie.armourers_workshop.plugin.utils;

import moe.plushie.armourers_workshop.plugin.core.skin.SkinDescriptor;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinWardrobe;
import net.cocoonmc.core.utils.SimpleAssociatedKey;

public class CacheKeys {

    public static final SimpleAssociatedKey<SkinDescriptor> SKIN_KEY = SimpleAssociatedKey.of("Skin", SkinDescriptor.class);

    public static final SimpleAssociatedKey<SkinWardrobe> SKIN_WARDROBE_KEY = SimpleAssociatedKey.of("Wardrobe", SkinWardrobe.class);
}
