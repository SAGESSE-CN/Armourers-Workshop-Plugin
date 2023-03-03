package moe.plushie.armourers_workshop.plugin.network;

import com.google.common.io.ByteStreams;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import moe.plushie.armourers_workshop.plugin.ArmourersWorkshop;
import moe.plushie.armourers_workshop.plugin.utils.FriendlyByteBuf;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class MessageListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (ArmourersWorkshop.CHANNEL.equals(channel)) {
            FriendlyByteBuf bf = new FriendlyByteBuf(Unpooled.wrappedBuffer(message));
            int id = bf.readInt();
            player.sendMessage("Received packet(" + id + ") from " + channel + ".");

            if (id == 0x03) {
                //    OPEN_WARDROBE(0x03, OpenWardrobePacket.class, OpenWardrobePacket::new),
                FriendlyByteBuf buf1 = new FriendlyByteBuf();
                buf1.writeByte(1);  // "fml:play" 打开容器标识
                buf1.writeVarInt(24); // UI 标识
                buf1.writeVarInt(100); //
                buf1.writeUtf("{\"translate\":\"inventory.armourers_workshop.wardrobe\"}");

                FriendlyByteBuf buf2 = new FriendlyByteBuf();
                buf2.writeInt(player.getEntityId());
                buf2.writeUtf("armourers_workshop:player");

                buf1.writeByteArray(buf2.array());

                player.sendPluginMessage(ArmourersWorkshop.INSTANCE, "fml:play", buf1.array());

//            player.sendPluginMessage(ArmourersWorkshop.INSTANCE, "fml:play", Base64.getDecoder().decode("ARgDNXsidHJhbnNsYXRlIjoiaW52ZW50b3J5LmFybW91cmVyc193b3Jrc2hvcC53YXJkcm9iZSJ9HgAAAA0ZYXJtb3VyZXJzX3dvcmtzaG9wOnBsYXllcg=="));
            }

            if (id == 0x01) {
                // REQUEST_FILE(0x01, RequestSkinPacket.class, RequestSkinPacket::new),
                String kn = bf.readUtf();

                String filePath = "";
                if (kn.equals("db:00001")) {
                    filePath = "armourers_workshop/skin-library/胡桃/胡桃.armour";
                }
                if (kn.equals("db:00002")) {
                    filePath = "armourers_workshop/skin-library/胡桃/护摩之杖.armour";
                }
                ByteBuf buf = Unpooled.buffer();
                FriendlyByteBuf buf1 = new FriendlyByteBuf();
                try {
                    File file = new File(filePath);
                    // read file
                    FileInputStream is = new FileInputStream(file);
                    ByteBufOutputStream os = new ByteBufOutputStream(buf);
                    ByteStreams.copy(is, os);
                    // stream
                    buf1.writeInt(0x02);
                    buf1.writeUtf(kn);
                    buf1.writeVarInt(1); // MODE.STREAM
                    buf1.writeBoolean(true); // compress
                    writeSkinStream(buf1, buf, true);

                } catch (Exception e) {
                    // exception
                    buf1.writeInt(0x02);
                    buf1.writeUtf(kn);
                    buf1.writeVarInt(0); // MODE.EXCEPTION
                    buf1.writeBoolean(true); // compress
                    writeException(buf1, e, true);
                }
                // RESPONSE_FILE(0x02, ResponseSkinPacket.class, ResponseSkinPacket::new),
                player.sendPluginMessage(ArmourersWorkshop.INSTANCE, ArmourersWorkshop.CHANNEL, buf1.array());
            }



        }
    }

    private void writeException(FriendlyByteBuf buffer, Exception exception, boolean compress) {
        OutputStream outputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            outputStream = createOutputStream(buffer, compress);
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(exception);
        } catch (Exception exception1) {
            exception1.printStackTrace();
        } finally {
            // StreamUtils.closeQuietly(objectOutputStream, outputStream);
        }
    }

    private void writeSkinStream(FriendlyByteBuf buffer, ByteBuf buf, boolean compress) {
        OutputStream outputStream = null;
        try {
            outputStream = createOutputStream(buffer, compress);
            outputStream.write(buf.array());
            outputStream.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            // StreamUtils.closeQuietly(outputStream);
        }
    }

    private OutputStream createOutputStream(FriendlyByteBuf buffer, boolean compress) throws Exception {
        ByteBufOutputStream outputStream = new ByteBufOutputStream(buffer);
        if (compress) {
            return new GZIPOutputStream(outputStream);
        }
        return outputStream;
    }

}
