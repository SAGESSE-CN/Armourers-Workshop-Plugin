package moe.plushie.armourers_workshop.core.network;


import moe.plushie.armourers_workshop.api.IEntitySerializer;
import moe.plushie.armourers_workshop.api.IServerPacketHandler;
import moe.plushie.armourers_workshop.core.blockentity.HologramProjectorBlockEntity;
import moe.plushie.armourers_workshop.utils.DataSerializers;
import moe.plushie.armourers_workshop.utils.ObjectUtils;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.BlockEntity;
import net.cocoonmc.core.network.FriendlyByteBuf;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.Player;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class UpdateHologramProjectorPacket extends CustomPacket {

    private final BlockPos pos;
    private final Field field;
    private final Object fieldValue;

    public UpdateHologramProjectorPacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        this.field = buffer.readEnum(Field.class);
        this.fieldValue = field.dataSerializer.read(buffer);
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeEnum(field);
        field.dataSerializer.write(buffer, ObjectUtils.unsafeCast(fieldValue));
    }

    @Override
    public void accept(IServerPacketHandler packetHandler, Player player) {
        // TODO: check player
        Level level = player.getLevel();
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof HologramProjectorBlockEntity) {
            field.set((HologramProjectorBlockEntity) entity, fieldValue);
        }
    }

    public enum Field {

        POWER_MODE(DataSerializers.INT, HologramProjectorBlockEntity::getPowerMode, HologramProjectorBlockEntity::setPowerMode),
        IS_GLOWING(DataSerializers.BOOLEAN, HologramProjectorBlockEntity::isGlowing, HologramProjectorBlockEntity::setGlowing),

        SHOWS_ROTATION_POINT(DataSerializers.BOOLEAN, HologramProjectorBlockEntity::shouldShowRotationPoint, HologramProjectorBlockEntity::setShowRotationPoint),

        OFFSET(DataSerializers.VECTOR_3F, HologramProjectorBlockEntity::getModelOffset, HologramProjectorBlockEntity::setModelOffset),
        ANGLE(DataSerializers.VECTOR_3F, HologramProjectorBlockEntity::getModelAngle, HologramProjectorBlockEntity::setModelAngle),

        ROTATION_OFFSET(DataSerializers.VECTOR_3F, HologramProjectorBlockEntity::getRotationOffset, HologramProjectorBlockEntity::setRotationOffset),
        ROTATION_SPEED(DataSerializers.VECTOR_3F, HologramProjectorBlockEntity::getRotationSpeed, HologramProjectorBlockEntity::setRotationSpeed);

        private final IEntitySerializer<?> dataSerializer;
        private final BiConsumer<HologramProjectorBlockEntity, ?> applier;

        <T> Field(IEntitySerializer<T> dataSerializer, Function<HologramProjectorBlockEntity, T> supplier, BiConsumer<HologramProjectorBlockEntity, T> applier) {
            this.dataSerializer = dataSerializer;
            this.applier = applier;
        }

        public void set(HologramProjectorBlockEntity entity, Object value) {
            applier.accept(entity, ObjectUtils.unsafeCast(value));
        }
    }
}
