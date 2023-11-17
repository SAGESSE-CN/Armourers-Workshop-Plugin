package moe.plushie.armourers_workshop.init;

import moe.plushie.armourers_workshop.core.texture.PlayerTextureDescriptor;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.network.FriendlyByteBuf;
import net.cocoonmc.core.network.syncher.EntityDataSerializer;
import net.cocoonmc.core.network.syncher.EntityDataSerializers;

@SuppressWarnings("unused")
public class ModEntitySerializers {

    public static final EntityDataSerializer<CompoundTag> COMPOUND_TAG = EntityDataSerializers.COMPOUND_TAG;
    public static final EntityDataSerializer<Integer> INT = EntityDataSerializers.INT;
    public static final EntityDataSerializer<String> STRING = EntityDataSerializers.STRING;
    public static final EntityDataSerializer<Boolean> BOOLEAN = EntityDataSerializers.BOOLEAN;
    public static final EntityDataSerializer<Float> FLOAT = EntityDataSerializers.FLOAT;

    public static final EntityDataSerializer<PlayerTextureDescriptor> PLAYER_TEXTURE = new EntityDataSerializer<PlayerTextureDescriptor>() {
        @Override
        public void write(FriendlyByteBuf buf, PlayerTextureDescriptor descriptor) {
            buf.writeNbt(descriptor.serializeNBT());
        }

        @Override
        public PlayerTextureDescriptor read(FriendlyByteBuf buf) {
            return new PlayerTextureDescriptor(buf.readNbt());
        }
    };
}
