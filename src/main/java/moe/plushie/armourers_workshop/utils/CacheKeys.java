package moe.plushie.armourers_workshop.utils;

import moe.plushie.armourers_workshop.core.skin.SkinDescriptor;
import moe.plushie.armourers_workshop.core.skin.SkinWardrobe;
import net.cocoonmc.core.utils.SimpleAssociatedKey;

public class CacheKeys {

    public static final SimpleAssociatedKey<SkinDescriptor> SKIN_KEY = SimpleAssociatedKey.of(SkinDescriptor.class);

    public static final SimpleAssociatedKey<SkinWardrobe> SKIN_WARDROBE_KEY = SimpleAssociatedKey.of(SkinWardrobe.class);
}
