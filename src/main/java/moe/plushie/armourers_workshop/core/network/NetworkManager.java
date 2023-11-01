package moe.plushie.armourers_workshop.core.network;

import io.netty.buffer.Unpooled;
import moe.plushie.armourers_workshop.api.IServerPacketHandler;
import moe.plushie.armourers_workshop.api.Packet;
import moe.plushie.armourers_workshop.core.skin.SkinWardrobe;
import moe.plushie.armourers_workshop.utils.PacketSplitter;
import moe.plushie.armourers_workshop.utils.Scheduler;
import net.cocoonmc.Cocoon;
import net.cocoonmc.core.network.FriendlyByteBuf;
import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.core.world.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class NetworkManager {

    private static String AW_CHANNEL = "armourers_workshop:play";
    private static String FML_CHANNEL = "fml:play";

    private static final Dispatcher dispatcher = new Dispatcher();
    private static final PacketSplitter splitter = new PacketSplitter();


    public static void init() {
        Messenger messenger = Bukkit.getServer().getMessenger();

        messenger.registerIncomingPluginChannel(Cocoon.getPlugin(), AW_CHANNEL, dispatcher);
        messenger.registerOutgoingPluginChannel(Cocoon.getPlugin(), AW_CHANNEL);

        messenger.registerIncomingPluginChannel(Cocoon.getPlugin(), FML_CHANNEL, dispatcher);
        messenger.registerOutgoingPluginChannel(Cocoon.getPlugin(), FML_CHANNEL);
    }

    public static void sendToAll(final CustomPacket message) {
        send(message, Bukkit.getServer().getOnlinePlayers().stream().map(Player::of).collect(Collectors.toList()));
    }

    public static void sendWardrobeTo(Entity entity, Player player) {
        SkinWardrobe wardrobe = SkinWardrobe.of(entity);
        if (wardrobe != null) {
            wardrobe.broadcast(player);
        }
    }

    public static void sendTo(final CustomPacket message, final Player player) {
        send(message, Collections.singleton(player));
    }

    public static void sendToTracking(final CustomPacket message, final Entity entity) {
        // TODO: IMPL
        send(message, Bukkit.getServer().getOnlinePlayers().stream().map(Player::of).collect(Collectors.toList()));
//        IMPL.sendToTracking(message, entity);
    }

    private static void send(final CustomPacket message, final Collection<? extends Player> players) {
        split(message, packet -> players.forEach(player -> player.asBukkit().sendPluginMessage(Cocoon.getPlugin(), packet.getChannel(), packet.getBytes())));
    }

    private static void split(final CustomPacket message, Consumer<Packet<?>> consumer) {
//        if (message instanceof FMLPacket) {
//            FriendlyByteBuf buf = new FriendlyByteBuf();
//            message.encode(buf);
//            consumer.accept(new Packet<>(FML_CHANNEL, buf));
//        }
        splitter.split(message, buf -> new Packet<>(AW_CHANNEL, buf), 32000, consumer);
    }


    public static class Dispatcher implements IServerPacketHandler, PluginMessageListener {

        @Override
        public void onPluginMessageReceived(@NotNull String channel, @NotNull org.bukkit.entity.Player player, @NotNull byte[] message) {
            if (channel.equals(AW_CHANNEL)) {
//                Context context = event.getSource().get();
//                ServerPlayer player = context.getSender();
//                if (player == null) {
//                    return;
//                }
                Player player1 = Player.of(player);
                IServerPacketHandler packetHandler = this;
                FriendlyByteBuf payload = new FriendlyByteBuf(Unpooled.wrappedBuffer(message));
                splitter.merge(player.getUniqueId(), payload, packet -> Scheduler.run(() -> packet.accept(packetHandler, player1)));
//                context.setPacketHandled(true);
            }
            if (channel.equals(FML_CHANNEL)) {
                // huh?
            }
        }
    }
}
