package moe.plushie.armourers_workshop.init;


import moe.plushie.armourers_workshop.api.registry.IRegistryKey;
import net.cocoonmc.core.resources.ResourceLocation;

public class ModItemGroups {

    public static final IRegistryKey<Object> MAIN_GROUP = create().build("main");
    public static final IRegistryKey<Object> BUILDING_GROUP = create().build("painting_tools");


    private static ItemGroupBuilder<Object> create() {
        return new ItemGroupBuilder<>();
    }

    public static void init() {
    }

    public static class ItemGroupBuilder<T> {

        public IRegistryKey<T> build(String name) {
            ResourceLocation registryName = ModConstants.key(name);
            ModLog.debug("Registering Item Group '{}'", registryName);
            return null;
        }
    }
}
