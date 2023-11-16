package moe.plushie.armourers_workshop.init;

import moe.plushie.armourers_workshop.core.entity.MannequinEntity;
import moe.plushie.armourers_workshop.core.entity.SeatEntity;
import net.cocoonmc.core.resources.ResourceLocation;
import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.core.world.entity.EntityType;
import net.cocoonmc.core.world.entity.EntityTypes;

@SuppressWarnings("unused")
public class ModEntityTypes {

    public static final EntityType<MannequinEntity> MANNEQUIN = normal(MannequinEntity::new).delegate(EntityTypes.ARMOR_STAND).fixed(0.6f, 1.88f).build("mannequin");
    public static final EntityType<SeatEntity> SEAT = normal(SeatEntity::new).delegate(EntityTypes.MINECART).fixed(0.0f, 0.0f).noSummon().build("seat");

    private static <T extends Entity> Builder<T> normal(EntityType.Factory<T> factory) {
        return new Builder<>(factory);
    }

    public static void init() {
    }

    public static class Builder<T extends Entity> {

        private EntityType<?> delegate;
        private final EntityType.Factory<T> factory;

        public Builder(EntityType.Factory<T> factory) {
            this.factory = factory;
        }

        public Builder<T> noSummon() {
            return this;
        }

        public Builder<T> fixed(double width, double height) {
            return this;
        }

        public Builder<T> delegate(EntityType<?> delegate) {
            this.delegate = delegate;
            return this;
        }

        public EntityType<T> build(String name) {
            ResourceLocation registryName = ModConstants.key(name);
            EntityType<T> entityType = new EntityType<>(factory);
            entityType.setDelegate(delegate);
            return EntityType.register(registryName, entityType);
        }
    }
}
