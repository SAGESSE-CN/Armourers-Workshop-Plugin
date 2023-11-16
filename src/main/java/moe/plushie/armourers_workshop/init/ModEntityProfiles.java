package moe.plushie.armourers_workshop.init;

import moe.plushie.armourers_workshop.api.skin.ISkinType;
import moe.plushie.armourers_workshop.core.skin.EntityProfile;
import moe.plushie.armourers_workshop.core.skin.SkinTypes;
import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.core.world.entity.EntityType;
import net.cocoonmc.core.world.entity.EntityTypes;

import java.util.HashMap;
import java.util.function.Function;

public class ModEntityProfiles {

    public static final EntityProfile PLAYER = Builder.create()
            .add(SkinTypes.ARMOR_HEAD, ModEntityProfiles::playerSlots)
            .add(SkinTypes.ARMOR_CHEST, ModEntityProfiles::playerSlots)
            .add(SkinTypes.ARMOR_LEGS, ModEntityProfiles::playerSlots)
            .add(SkinTypes.ARMOR_FEET, ModEntityProfiles::playerSlots)
            .add(SkinTypes.ARMOR_WINGS, ModEntityProfiles::playerSlots)
            .add(SkinTypes.OUTFIT, ModEntityProfiles::playerSlots)
            .add(SkinTypes.ITEM_BOW, 1)
            .add(SkinTypes.ITEM_SWORD, 1)
            .add(SkinTypes.ITEM_SHIELD, 1)
            .add(SkinTypes.ITEM_TRIDENT, 1)
            .add(SkinTypes.TOOL_AXE, 1)
            .add(SkinTypes.TOOL_HOE, 1)
            .add(SkinTypes.TOOL_PICKAXE, 1)
            .add(SkinTypes.TOOL_SHOVEL, 1)
            .build("player");

    public static final EntityProfile MANNEQUIN = Builder.create()
            .add(SkinTypes.ARMOR_HEAD, 10)
            .add(SkinTypes.ARMOR_CHEST, 10)
            .add(SkinTypes.ARMOR_LEGS, 10)
            .add(SkinTypes.ARMOR_FEET, 10)
            .add(SkinTypes.ARMOR_WINGS, 10)
            .add(SkinTypes.OUTFIT, 10)
            .build("mannequin");

    public static final EntityProfile COMMON = Builder.create()
            .add(SkinTypes.ARMOR_HEAD, ModEntityProfiles::mobSlots)
            .add(SkinTypes.ARMOR_CHEST, ModEntityProfiles::mobSlots)
            .add(SkinTypes.ARMOR_LEGS, ModEntityProfiles::mobSlots)
            .add(SkinTypes.ARMOR_FEET, ModEntityProfiles::mobSlots)
            .add(SkinTypes.ARMOR_WINGS, ModEntityProfiles::mobSlots)
            .add(SkinTypes.OUTFIT, ModEntityProfiles::mobSlots)
            .add(SkinTypes.ITEM_BOW, 1)
            .add(SkinTypes.ITEM_SWORD, 1)
            .add(SkinTypes.ITEM_SHIELD, 1)
            .add(SkinTypes.ITEM_TRIDENT, 1)
            .add(SkinTypes.TOOL_AXE, 1)
            .add(SkinTypes.TOOL_HOE, 1)
            .add(SkinTypes.TOOL_PICKAXE, 1)
            .add(SkinTypes.TOOL_SHOVEL, 1)
            .build("common");

    public static final EntityProfile VILLAGER = Builder.create()
            .add(SkinTypes.ARMOR_HEAD, ModEntityProfiles::mobSlots)
            .add(SkinTypes.ARMOR_CHEST, ModEntityProfiles::mobSlots)
            .add(SkinTypes.ARMOR_LEGS, ModEntityProfiles::mobSlots)
            .add(SkinTypes.ARMOR_FEET, ModEntityProfiles::mobSlots)
            .add(SkinTypes.ARMOR_WINGS, ModEntityProfiles::mobSlots)
            .add(SkinTypes.OUTFIT, ModEntityProfiles::mobSlots)
            .build("villager");

    public static final EntityProfile ONLY_HEAD = Builder.create()
            .add(SkinTypes.ARMOR_HEAD, ModEntityProfiles::mobSlots)
            .build("only_head");

    public static final EntityProfile PROJECTING = Builder.create()
            .add(SkinTypes.ITEM_BOW, 1)
            .add(SkinTypes.ITEM_TRIDENT, 1)
            .fixed()
            .build("projecting");

    private static final HashMap<EntityType<?>, EntityProfile> PROFILES = new HashMap<>();

    private static int playerSlots(ISkinType type) {
        return ModConfig.Common.prefersWardrobePlayerSlots;
    }

    private static int mobSlots(ISkinType type) {
        return ModConfig.Common.prefersWardrobeMobSlots;
    }

    public static void init() {
        register(EntityTypes.PLAYER, PLAYER);

        register(EntityTypes.VILLAGER, VILLAGER);
        register(EntityTypes.WITCH, VILLAGER);
        register(EntityTypes.WANDERING_TRADER, VILLAGER);

        register(EntityTypes.SKELETON, COMMON);
        register(EntityTypes.STRAY, COMMON);
        register(EntityTypes.WITHER_SKELETON, COMMON);
        register(EntityTypes.ZOMBIE, COMMON);
        register(EntityTypes.HUSK, COMMON);
        register(EntityTypes.ZOMBIE_VILLAGER, COMMON);
        register(EntityTypes.DROWNED, COMMON);

        register(EntityTypes.EVOKER, COMMON);
        register(EntityTypes.ILLUSIONER, COMMON);
        register(EntityTypes.PILLAGER, COMMON);
        register(EntityTypes.VINDICATOR, COMMON);

        register(EntityTypes.VEX, COMMON);
        register(EntityTypes.PIGLIN, COMMON);
        register(EntityTypes.PIGLIN_BRUTE, COMMON);
        register(EntityTypes.ZOMBIFIED_PIGLIN, COMMON);

        register(EntityTypes.SLIME, ONLY_HEAD);
        register(EntityTypes.GHAST, ONLY_HEAD);
        register(EntityTypes.CHICKEN, ONLY_HEAD);
        register(EntityTypes.CREEPER, ONLY_HEAD);

        register(EntityTypes.ARROW, PROJECTING);
        register(EntityTypes.TRIDENT, PROJECTING);

//        register(EntityType.ARMOR_STAND, EntityProfiles.MANNEQUIN);
        register(EntityTypes.IRON_GOLEM, MANNEQUIN);

        register(ModEntityTypes.MANNEQUIN, MANNEQUIN);

//        ModCompatible.registerCustomEntityType();
    }

    public static void register(EntityType<?> entityType, EntityProfile entityProfile) {
        ModLog.debug("Registering Entity Profile '{}'", entityType.getRegistryName());
        PROFILES.put(entityType, entityProfile);
    }

//    public static void register(String registryName, EntityProfile entityProfile) {
//        EntityType.byString(registryName).ifPresent(entityType -> register(entityType, entityProfile));
//    }
//
//    public static void forEach(BiConsumer<EntityType<?>, EntityProfile> consumer) {
//        PROFILES.forEach(consumer);
//    }

    public static <T extends Entity> EntityProfile getProfile(T entity) {
        return getProfile(entity.getType());
    }

    public static <T extends Entity> EntityProfile getProfile(EntityType<?> entityType) {
        return PROFILES.get(entityType);
    }

    public static class Builder<T> {

        private final HashMap<ISkinType, Function<ISkinType, Integer>> supports = new HashMap<>();
        private boolean editable = true;

        public static <T extends Entity> Builder<T> create() {
            return new Builder<>();
        }

        private Builder<T> add(ISkinType type, Function<ISkinType, Integer> f) {
            supports.put(type, f);
            return this;
        }

        private Builder<T> add(ISkinType type, int maxCount) {
            supports.put(type, t -> maxCount);
            return this;
        }

        private Builder<T> fixed() {
            editable = false;
            return this;
        }

        public EntityProfile build(String name) {
            return new EntityProfile(ModConstants.key(name), supports, editable);
        }
    }
}
