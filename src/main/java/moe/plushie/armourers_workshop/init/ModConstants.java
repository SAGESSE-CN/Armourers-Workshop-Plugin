package moe.plushie.armourers_workshop.init;

import net.cocoonmc.core.resources.ResourceLocation;

public class ModConstants {

    public static final String MOD_ID = "armourers_workshop";
    public static final String MOD_NET_ID = "f";

    public static final String ENTITY_MANNEQUIN = entityType("mannequin");
    public static final String ENTITY_SEAT = entityType("seat");

    public static final String BLOCK_HOLOGRAM_PROJECTOR = blockEntityType("hologram-projector");
    public static final String BLOCK_OUTFIT_MAKER = blockEntityType("outfit-maker");
    public static final String BLOCK_DYE_TABLE = blockEntityType("dye-table");

    public static final String BLOCK_COLOR_MIXER = blockEntityType("colour-mixer");
    public static final String BLOCK_ARMOURER = blockEntityType("armourer");
    public static final String BLOCK_ADVANCED_SKIN_BUILDER = blockEntityType("advanced-skin-builder");

    public static final String BLOCK_SKIN_LIBRARY = blockEntityType("skin-library");
    public static final String BLOCK_SKIN_LIBRARY_GLOBAL = blockEntityType("skin-library-global");

    public static final String BLOCK_SKINNABLE = blockEntityType("skinnable");
    public static final String BLOCK_BOUNDING_BOX = blockEntityType("bounding-box");
    public static final String BLOCK_SKIN_CUBE = blockEntityType("skin-cube");

    public static ResourceLocation key(String path) {
        return new ResourceLocation(ModConstants.MOD_ID, path);
    }

    private static String entityType(String name) {
        return name;
    }

    private static String blockEntityType(String name) {
        return name;
    }
}
