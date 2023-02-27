package moe.plushie.armourers_workshop.plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.steveice10.opennbt.NBTIO;
import com.github.steveice10.opennbt.tag.builtin.ByteTag;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.IntTag;
import com.github.steveice10.opennbt.tag.builtin.ListTag;
import com.github.steveice10.opennbt.tag.builtin.StringTag;
import com.github.steveice10.opennbt.tag.builtin.Tag;
import io.netty.buffer.ByteBufOutputStream;
import moe.plushie.armourers_workshop.plugin.network.MessageListener;
import moe.plushie.armourers_workshop.plugin.packet.PacketListener;
import moe.plushie.armourers_workshop.plugin.utils.FriendlyByteBuf;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;

public final class ArmourersWorkshop extends JavaPlugin implements Listener {

    public static String CHANNEL = "armourers_workshop:play";

    public static ArmourersWorkshop INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new PacketListener(this, PacketType.Play.Client.WINDOW_CLICK));

        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, CHANNEL, new MessageListener());
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, CHANNEL);

        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "fml:play", new MessageListener());
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "fml:play");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        try {
            Class<? extends CommandSender> senderClass = player.getClass();
            Method addChannel = senderClass.getDeclaredMethod("addChannel", String.class);
            addChannel.setAccessible(true);
            addChannel.invoke(player, CHANNEL);
            sendSync(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        FriendlyByteBuf buf1 = new FriendlyByteBuf();
        buf1.writeByte(1);  // "fml:play" 打开容器标识
        buf1.writeVarInt(24); // UI 标识
        buf1.writeVarInt(100); //
        buf1.writeUtf("{\"translate\":\"inventory.armourers_workshop.wardrobe\"}");

        FriendlyByteBuf buf2 = new FriendlyByteBuf();
        buf2.writeInt(event.getRightClicked().getEntityId());
        buf2.writeUtf("armourers_workshop:player");

        buf1.writeByteArray(buf2.array());

        player.sendPluginMessage(ArmourersWorkshop.INSTANCE, "fml:play", buf1.array());

    }

    private void sendSync(Player player) {
        FriendlyByteBuf buf1 = new FriendlyByteBuf();
//        byte[] bytes = Base64.getDecoder().decode("AAAABAAAAAMRCgAAAwAKVmlzaWJpbGl0eQAAAEYJAAVJdGVtcwoAAAABAQAEU2xvdEYIAAJpZAAXYXJtb3VyZXJzX3dvcmtzaG9wOnNraW4BAAVDb3VudAEKAAN0YWcKABFBcm1vdXJlcnNXb3Jrc2hvcAgACFNraW5UeXBlABBhcm1vdXJlcnM6b3V0Zml0CAAKSWRlbnRpZmllcgANZGI6UWNSOENhZEtqaQAAAAEAC0RhdGFWZXJzaW9uAQA=");
//        buf1.writeBytes(bytes);
//        buf1.writerIndex(0);

        buf1.writeInt(4); //UPDATE_WARDROBE(0x04, UpdateWardrobePacket.class, UpdateWardrobePacket::new),
        buf1.writeVarInt(0); //UpdateWardrobePacket.SYNC.SYNC
        buf1.writeInt(player.getEntityId());

        ArrayList<Tag> items = new ArrayList<>();

        CompoundTag mmm = new CompoundTag("");
        mmm.put(new ByteTag("Slot", (byte) 70));
        mmm.put(new StringTag("id", "armourers_workshop:skin"));
        mmm.put(new ByteTag("Count", (byte) 1));

        CompoundTag mmm1 = new CompoundTag("tag");
        CompoundTag mmm2 = new CompoundTag("ArmourersWorkshop");
        mmm2.put(new StringTag("SkinType", "armourers:outfit"));
        mmm2.put(new StringTag("Identifier", "db:QcR8CadKji"));
        mmm1.put(mmm2);
        mmm.put(mmm1);

        items.add(mmm);

        CompoundTag tag = new CompoundTag("");
        tag.put(new IntTag("Visibility", 70));
        tag.put(new ListTag("Items", items));
        tag.put(new ByteTag("DataVersion", (byte) 1));

        try {
            OutputStream outputStream = new ByteBufOutputStream(buf1);
            NBTIO.writeTag(outputStream, tag);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // buf1.writeNbt({Visibility: 70, Items: [{Slot: 70b, id: "armourers_workshop:skin", Count: 1b, tag: {ArmourersWorkshop: {SkinType: "armourers:outfit", Identifier: "db:QcR8CadKji"}}}], DataVersion: 1b})

        player.sendPluginMessage(ArmourersWorkshop.INSTANCE, CHANNEL, buf1.array());
    }
}
