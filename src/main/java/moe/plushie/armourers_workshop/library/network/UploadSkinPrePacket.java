package moe.plushie.armourers_workshop.library.network;


import moe.plushie.armourers_workshop.core.network.CustomPacket;
import net.cocoonmc.core.network.FriendlyByteBuf;

public class UploadSkinPrePacket extends CustomPacket {

    public UploadSkinPrePacket() {
    }

    public UploadSkinPrePacket(FriendlyByteBuf buf) {
//        super(DataSerializers.BOOLEAN, buf);
    }

//    @Override
//    public void accept(IServerPacketHandler packetHandler, ServerPlayer player, IResultHandler<Boolean> reply) {
//        // check the user global skin upload permission.
//        reply.accept(ModPermissions.SKIN_LIBRARY_GLOBAL_SKIN_UPLOAD.accept(player));
//    }
}
