package moe.plushie.armourers_workshop.core.network;

import io.netty.buffer.Unpooled;
import moe.plushie.armourers_workshop.api.IServerPacketHandler;
import moe.plushie.armourers_workshop.api.Packet;
import moe.plushie.armourers_workshop.core.skin.SkinWardrobe;
import moe.plushie.armourers_workshop.init.ModConstants;
import moe.plushie.armourers_workshop.utils.PacketSplitter;
import moe.plushie.armourers_workshop.utils.Scheduler;
import net.cocoonmc.Cocoon;
import net.cocoonmc.core.network.FriendlyByteBuf;
import net.cocoonmc.core.resources.ResourceLocation;
import net.cocoonmc.core.utils.PacketHelper;
import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.core.world.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class NetworkManager {

    private static final ResourceLocation CHANNEL = ModConstants.key("play");

    private static final Dispatcher dispatcher = new Dispatcher();
    private static final PacketSplitter splitter = new PacketSplitter();


    public static void init() {
        Messenger messenger = Bukkit.getServer().getMessenger();

        messenger.registerIncomingPluginChannel(Cocoon.getPlugin(), CHANNEL.toString(), dispatcher);
        messenger.registerOutgoingPluginChannel(Cocoon.getPlugin(), CHANNEL.toString());
    }

    public static void sendWardrobeTo(Entity entity, Player player) {
        SkinWardrobe wardrobe = SkinWardrobe.of(entity);
        if (wardrobe != null) {
            wardrobe.broadcast(player);
        }
    }

    public static void sendTo(final CustomPacket message, final Player player) {
        split(message, packet -> PacketHelper.sendTo(packet.getChannel(), packet.getBuffer(), player));
    }

    public static void sendToAll(final CustomPacket message) {
        split(message, packet -> PacketHelper.sendToAll(packet.getChannel(), packet.getBuffer()));
    }

    public static void sendToTracking(final CustomPacket message, final Entity entity) {
        split(message, packet -> PacketHelper.sendToTrackingAndSelf(packet.getChannel(), packet.getBuffer(), entity));
    }

    private static void split(final CustomPacket message, Consumer<Packet<?>> consumer) {
//        if (message instanceof FMLPacket) {
//            FriendlyByteBuf buf = new FriendlyByteBuf();
//            message.encode(buf);
//            consumer.accept(new Packet<>(FML_CHANNEL, buf));
//        }
        splitter.split(message, buf -> new Packet<>(CHANNEL, buf), 32000, consumer);
    }


    public static class Dispatcher implements IServerPacketHandler, PluginMessageListener {

        @Override
        public void onPluginMessageReceived(@NotNull String channel, @NotNull org.bukkit.entity.Player player, @NotNull byte[] message) {
//            if (channel.equals(CHANNEL.toString())) {
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
//            }
        }
    }
}
