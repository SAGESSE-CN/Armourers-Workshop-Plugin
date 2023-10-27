package moe.plushie.armourers_workshop.plugin.core.network;

import moe.plushie.armourers_workshop.plugin.api.IServerPacketHandler;
import net.cocoonmc.core.network.FriendlyByteBuf;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.function.Function;

public class CustomPacket {

    private static final HashMap<Integer, Function<FriendlyByteBuf, CustomPacket>> DECODERS = new HashMap<>();
    private static final HashMap<Class<? extends CustomPacket>, Integer> ENCODERS = new HashMap<>();

    public static void register(int id, Class<? extends CustomPacket> clazz, Function<FriendlyByteBuf, CustomPacket> decoder) {
        ENCODERS.put(clazz, id);
        DECODERS.put(id, decoder);
    }

    public static Function<FriendlyByteBuf, CustomPacket> getPacketType(int id) {
        return DECODERS.get(id);
    }

    public void accept(final IServerPacketHandler packetHandler, final Player player) {
        throw new UnsupportedOperationException("This packet ( " + this.getPacketID() + ") does not implement a server side handler.");
    }

    public void encode(final FriendlyByteBuf buffer) {
    }

    public int getPacketID() {
        return ENCODERS.getOrDefault(this.getClass(), -1);
    }
}
