package moe.plushie.armourers_workshop.plugin.network;

import io.netty.buffer.Unpooled;
import moe.plushie.armourers_workshop.plugin.ArmourersWorkshop;
import moe.plushie.armourers_workshop.plugin.api.IServerPacketHandler;
import moe.plushie.armourers_workshop.plugin.api.Packet;
import moe.plushie.armourers_workshop.plugin.api.FriendlyByteBuf;
import moe.plushie.armourers_workshop.plugin.utils.PacketSplitter;
import moe.plushie.armourers_workshop.plugin.utils.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class NetworkManager {

    private static String AW_CHANNEL = "armourers_workshop:play";
    private static String FML_CHANNEL = "fml:play";

    private static final Dispatcher dispatcher = new Dispatcher();
    private static final PacketSplitter splitter = new PacketSplitter();


    public static void init() {

        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(ArmourersWorkshop.INSTANCE, AW_CHANNEL, dispatcher);
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(ArmourersWorkshop.INSTANCE, AW_CHANNEL);

        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(ArmourersWorkshop.INSTANCE, FML_CHANNEL, dispatcher);
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(ArmourersWorkshop.INSTANCE, FML_CHANNEL);
    }


    public static void sendToAll(final CustomPacket message) {

        //        FriendlyByteBuf buf1 = new FriendlyByteBuf();
//
//        buf1.writeInt(4); //UPDATE_WARDROBE(0x04, UpdateWardrobePacket.class, UpdateWardrobePacket::new),
//        buf1.writeVarInt(0); //UpdateWardrobePacket.SYNC.SYNC
//        buf1.writeInt(player.getEntityId());
//        buf1.writeNbt(wardrobe.serializeNBT());
//
//        player.sendPluginMessage(ArmourersWorkshop.INSTANCE, CHANNEL, buf1.array());
    }

    public static void sendTo(final CustomPacket message, final Player player) {
        // player.sendPluginMessage(ArmourersWorkshop.INSTANCE, CHANNEL, buf1.array());
        splitter.split(message, Packet::new, 32000, packet -> {
            player.sendPluginMessage(ArmourersWorkshop.INSTANCE, AW_CHANNEL, packet.getBytes());
        });
    }

    public static void sendToTracking(final CustomPacket message, final Entity entity) {
//        IMPL.sendToTracking(message, entity);
    }


    public static class Dispatcher implements IServerPacketHandler, PluginMessageListener {

        @Override
        public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
            if (channel.equals(AW_CHANNEL)) {
//                Context context = event.getSource().get();
//                ServerPlayer player = context.getSender();
//                if (player == null) {
//                    return;
//                }
                IServerPacketHandler packetHandler = this;
                FriendlyByteBuf payload = new FriendlyByteBuf(Unpooled.wrappedBuffer(message));
                splitter.merge(player.getUniqueId(), payload, packet -> Scheduler.runAsync(() -> packet.accept(packetHandler, player)));
//                context.setPacketHandled(true);
            }
            if (channel.equals(FML_CHANNEL)) {
                // huh?
            }
        }
    }
}
