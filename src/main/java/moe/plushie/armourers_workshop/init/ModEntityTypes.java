package moe.plushie.armourers_workshop.init;

import moe.plushie.armourers_workshop.api.registry.IRegistryKey;
import moe.plushie.armourers_workshop.core.entity.MannequinEntity;
import moe.plushie.armourers_workshop.core.entity.SeatEntity;
import net.cocoonmc.core.resources.ResourceLocation;
import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.core.world.entity.EntityType;
import net.cocoonmc.core.world.entity.EntityTypes;

@SuppressWarnings("unused")
public class ModEntityTypes {

    public static final IRegistryKey<EntityType<MannequinEntity>> MANNEQUIN = normal(MannequinEntity::new).delegate(EntityTypes.ARMOR_STAND).fixed(0.6f, 1.88f).build(ModConstants.ENTITY_MANNEQUIN);
    public static final IRegistryKey<EntityType<SeatEntity>> SEAT = normal(SeatEntity::new).delegate(EntityTypes.MINECART).fixed(0.0f, 0.0f).noSummon().build(ModConstants.ENTITY_SEAT);

    private static <T extends Entity> EntityTypeBuilder<T> normal(EntityType.Factory<T> entityFactory) {
        return new EntityTypeBuilder<>(entityFactory);
    }

    public static void init() {
    }

    private static class EntityTypeBuilder<T extends Entity> {

        private EntityType<?> delegate;
        private final EntityType.Factory<T> factory;

        public EntityTypeBuilder(EntityType.Factory<T> factory) {
            this.factory = factory;
        }

        public EntityTypeBuilder<T> noSummon() {
            return this;
        }

        public EntityTypeBuilder<T> fixed(double width, double height) {
            return this;
        }

        public EntityTypeBuilder<T> delegate(EntityType<?> delegate) {
            this.delegate = delegate;
            return this;
        }

        public IRegistryKey<EntityType<T>> build(String name) {
            ResourceLocation registryName = ModConstants.key(name);
            EntityType<T> entityType = new EntityType<>(factory);
            entityType.setDelegate(delegate);
            ModLog.debug("Registering Entity '{}'", registryName);
            EntityType.register(registryName, entityType);
            return new IRegistryKey<EntityType<T>>() {
                @Override
                public ResourceLocation getRegistryName() {
                    return entityType.getRegistryName();
                }

                @Override
                public EntityType<T> get() {
                    return entityType;
                }
            };
        }
    }
}
