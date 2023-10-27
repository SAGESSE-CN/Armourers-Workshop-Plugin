package moe.plushie.armourers_workshop.plugin.init;

import net.cocoonmc.core.resources.ResourceLocation;

public class ModConstants {

    public static final String MOD_ID = "armourers_workshop";
    public static final String MOD_NET_ID = "d";

    public static ResourceLocation key(String path) {
        return new ResourceLocation(ModConstants.MOD_ID, path);
    }
}
