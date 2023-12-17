package moe.plushie.armourers_workshop.library.network;

import moe.plushie.armourers_workshop.api.IServerPacketHandler;
import moe.plushie.armourers_workshop.core.network.CustomPacket;
import net.cocoonmc.core.network.FriendlyByteBuf;
import net.cocoonmc.core.world.entity.Player;

public class EmptyPacket extends CustomPacket {

    public EmptyPacket(FriendlyByteBuf buf) {
        buf.readerIndex(buf.writerIndex());
    }

    @Override
    public void accept(IServerPacketHandler packetHandler, Player player) {
    }
}
