package moe.plushie.armourers_workshop.builder.network;

import moe.plushie.armourers_workshop.api.IEntitySerializer;
import moe.plushie.armourers_workshop.api.IServerPacketHandler;
import moe.plushie.armourers_workshop.builder.blockentity.ColorMixerBlockEntity;
import moe.plushie.armourers_workshop.core.network.CustomPacket;
import moe.plushie.armourers_workshop.utils.DataAccessor;
import moe.plushie.armourers_workshop.utils.DataSerializers;
import moe.plushie.armourers_workshop.utils.ObjectUtils;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.BlockEntity;
import net.cocoonmc.core.network.FriendlyByteBuf;
import net.cocoonmc.core.world.entity.Player;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class UpdateColorMixerPacket extends CustomPacket {

    private final BlockPos pos;
    private final Field field;
    private final Object fieldValue;

    public UpdateColorMixerPacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        this.field = buffer.readEnum(Field.class);
        this.fieldValue = field.getDataAccessor().dataSerializer.read(buffer);
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeEnum(field);
        field.getDataAccessor().dataSerializer.write(buffer, fieldValue);
    }

    @Override
    public void accept(IServerPacketHandler packetHandler, Player player) {
        // TODO: check player
        BlockEntity entity = player.getLevel().getBlockEntity(pos);
        if (entity instanceof ColorMixerBlockEntity) {
            field.set((ColorMixerBlockEntity) entity, fieldValue);
        }
    }

    public enum Field {

        COLOR(DataSerializers.PAINT_COLOR, ColorMixerBlockEntity::getColor, ColorMixerBlockEntity::setColor);

        private final DataAccessor<ColorMixerBlockEntity, ?> dataAccessor;

        <T> Field(IEntitySerializer<T> dataSerializer, Function<ColorMixerBlockEntity, T> supplier, BiConsumer<ColorMixerBlockEntity, T> applier) {
            this.dataAccessor = DataAccessor.of(dataSerializer, supplier, applier);
        }

        public <T> T get(ColorMixerBlockEntity entity) {
            DataAccessor<ColorMixerBlockEntity, T> dataAccessor = getDataAccessor();
            return dataAccessor.get(entity);
        }

        public <T> void set(ColorMixerBlockEntity entity, T value) {
            DataAccessor<ColorMixerBlockEntity, T> dataAccessor = getDataAccessor();
            dataAccessor.set(entity, value);
        }

        public <T> DataAccessor<ColorMixerBlockEntity, T> getDataAccessor() {
            return ObjectUtils.unsafeCast(dataAccessor);
        }
    }
}
