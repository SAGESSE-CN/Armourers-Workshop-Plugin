package moe.plushie.armourers_workshop.plugin.network;

import io.netty.buffer.Unpooled;
import moe.plushie.armourers_workshop.plugin.ArmourersWorkshop;
import moe.plushie.armourers_workshop.plugin.api.FMLPacket;
import moe.plushie.armourers_workshop.plugin.api.FriendlyByteBuf;
import moe.plushie.armourers_workshop.plugin.api.IServerPacketHandler;
import moe.plushie.armourers_workshop.plugin.api.Packet;
import moe.plushie.armourers_workshop.plugin.utils.PacketSplitter;
import moe.plushie.armourers_workshop.plugin.utils.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

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
        Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
        send(message, players);
    }

    public static void sendTo(final CustomPacket message, final Player player) {
        send(message, Collections.singleton(player));
    }

    public static void sendToTracking(final CustomPacket message, final Entity entity) {
        // TODO: IMPL
        Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
        send(message, players);
//        IMPL.sendToTracking(message, entity);
    }

    private static void send(final CustomPacket message, final Collection<? extends Player> players) {
        split(message, packet -> players.forEach(player -> player.sendPluginMessage(ArmourersWorkshop.INSTANCE, packet.getChannel(), packet.getBytes())));
    }

    private static void split(final CustomPacket message, Consumer<Packet<?>> consumer) {
        if (message instanceof FMLPacket) {
            FriendlyByteBuf buf = new FriendlyByteBuf();
            message.encode(buf);
            consumer.accept(new Packet<>(FML_CHANNEL, buf));
        } else {
            splitter.split(message, buf -> new Packet<>(AW_CHANNEL, buf), 32000, consumer);
        }
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
