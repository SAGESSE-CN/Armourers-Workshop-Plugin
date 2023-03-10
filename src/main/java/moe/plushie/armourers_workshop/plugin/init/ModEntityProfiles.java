package moe.plushie.armourers_workshop.plugin.init;

import moe.plushie.armourers_workshop.plugin.api.skin.ISkinType;
import moe.plushie.armourers_workshop.plugin.core.skin.EntityProfile;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinTypes;
import org.bukkit.entity.Entity;

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

//    public static final EntityProfile MANNEQUIN = Builder.create()
//            .add(SkinTypes.ARMOR_HEAD, 10)
//            .add(SkinTypes.ARMOR_CHEST, 10)
//            .add(SkinTypes.ARMOR_LEGS, 10)
//            .add(SkinTypes.ARMOR_FEET, 10)
//            .add(SkinTypes.ARMOR_WINGS, 10)
//            .add(SkinTypes.OUTFIT, 10)
//            .build();
//
//    public static final EntityProfile COMMON = Builder.create()
//            .add(SkinTypes.ARMOR_HEAD, ModEntityProfiles::mobSlots)
//            .add(SkinTypes.ARMOR_CHEST, ModEntityProfiles::mobSlots)
//            .add(SkinTypes.ARMOR_LEGS, ModEntityProfiles::mobSlots)
//            .add(SkinTypes.ARMOR_FEET, ModEntityProfiles::mobSlots)
//            .add(SkinTypes.ARMOR_WINGS, ModEntityProfiles::mobSlots)
//            .add(SkinTypes.OUTFIT, ModEntityProfiles::mobSlots)
//            .add(SkinTypes.ITEM_BOW, 1)
//            .add(SkinTypes.ITEM_SWORD, 1)
//            .add(SkinTypes.ITEM_SHIELD, 1)
//            .add(SkinTypes.ITEM_TRIDENT, 1)
//            .add(SkinTypes.TOOL_AXE, 1)
//            .add(SkinTypes.TOOL_HOE, 1)
//            .add(SkinTypes.TOOL_PICKAXE, 1)
//            .add(SkinTypes.TOOL_SHOVEL, 1)
//            .build();
//
//    public static final EntityProfile VILLAGER = Builder.create()
//            .add(SkinTypes.ARMOR_HEAD, ModEntityProfiles::mobSlots)
//            .add(SkinTypes.ARMOR_CHEST, ModEntityProfiles::mobSlots)
//            .add(SkinTypes.ARMOR_LEGS, ModEntityProfiles::mobSlots)
//            .add(SkinTypes.ARMOR_FEET, ModEntityProfiles::mobSlots)
//            .add(SkinTypes.ARMOR_WINGS, ModEntityProfiles::mobSlots)
//            .add(SkinTypes.OUTFIT, ModEntityProfiles::mobSlots)
//            .build();
//
//    public static final EntityProfile ONLY_HEAD = Builder.create()
//            .add(SkinTypes.ARMOR_HEAD, ModEntityProfiles::mobSlots)
//            .build();
//
//    public static final EntityProfile PROJECTING = Builder.create()
//            .add(SkinTypes.ITEM_BOW, 1)
//            .add(SkinTypes.ITEM_TRIDENT, 1)
//            .fixed()
//            .build();
//
//    private static final HashMap<EntityType<?>, EntityProfile> PROFILES = new HashMap<>();

    private static int playerSlots(ISkinType type) {
        return ModConfig.Common.prefersWardrobePlayerSlots;
    }

    private static int mobSlots(ISkinType type) {
        return ModConfig.Common.prefersWardrobeMobSlots;
    }

    public static void init() {
//        register(EntityType.PLAYER, PLAYER);
//
//        register(EntityType.VILLAGER, VILLAGER);
//        register(EntityType.WITCH, VILLAGER);
//        register(EntityType.WANDERING_TRADER, VILLAGER);
//
//        register(EntityType.SKELETON, COMMON);
//        register(EntityType.STRAY, COMMON);
//        register(EntityType.WITHER_SKELETON, COMMON);
//        register(EntityType.ZOMBIE, COMMON);
//        register(EntityType.HUSK, COMMON);
//        register(EntityType.ZOMBIE_VILLAGER, COMMON);
//        register(EntityType.DROWNED, COMMON);
//
//        register(EntityType.EVOKER, COMMON);
//        register(EntityType.ILLUSIONER, COMMON);
//        register(EntityType.PILLAGER, COMMON);
//        register(EntityType.VINDICATOR, COMMON);
//
//        register(EntityType.VEX, COMMON);
//        register(EntityType.PIGLIN, COMMON);
//        register(EntityType.PIGLIN_BRUTE, COMMON);
//        register(EntityType.ZOMBIFIED_PIGLIN, COMMON);
//
//        register(EntityType.SLIME, ONLY_HEAD);
//        register(EntityType.GHAST, ONLY_HEAD);
//        register(EntityType.CHICKEN, ONLY_HEAD);
//        register(EntityType.CREEPER, ONLY_HEAD);
//
//        register(EntityType.ARROW, PROJECTING);
//        register(EntityType.TRIDENT, PROJECTING);
//
////        register(EntityType.ARMOR_STAND, EntityProfiles.MANNEQUIN);
//        register(EntityType.IRON_GOLEM, MANNEQUIN);
//
//        register(ModEntities.MANNEQUIN.get(), MANNEQUIN);
//
//        ModCompatible.registerCustomEntityType();
    }

//    public static void register(EntityType<?> entityType, EntityProfile entityProfile) {
//        ModLog.debug("Registering Entity Profile '{}'", Registry.ENTITY_TYPE.getKey(entityType));
//        PROFILES.put(entityType, entityProfile);
//    }
//
//    public static void register(String registryName, EntityProfile entityProfile) {
//        EntityType.byString(registryName).ifPresent(entityType -> register(entityType, entityProfile));
//    }
//
//    public static void forEach(BiConsumer<EntityType<?>, EntityProfile> consumer) {
//        PROFILES.forEach(consumer);
//    }
//
//    @Nullable
//    public static <T extends Entity> EntityProfile getProfile(T entity) {
//        return getProfile(entity.getType());
//    }
//
//    @Nullable
//    public static <T extends Entity> EntityProfile getProfile(EntityType<T> entityType) {
//        return PROFILES.get(entityType);
//    }

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
