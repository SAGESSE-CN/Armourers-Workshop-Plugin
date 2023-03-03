package moe.plushie.armourers_workshop.plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import io.netty.buffer.ByteBufOutputStream;
import moe.plushie.armourers_workshop.plugin.data.SkinDescriptor;
import moe.plushie.armourers_workshop.plugin.data.SkinSlotType;
import moe.plushie.armourers_workshop.plugin.data.SkinWardrobe;
import moe.plushie.armourers_workshop.plugin.data.impl.ItemStack;
import moe.plushie.armourers_workshop.plugin.network.MessageListener;
import moe.plushie.armourers_workshop.plugin.packet.PacketListener;
import moe.plushie.armourers_workshop.plugin.utils.FriendlyByteBuf;
import net.querz.nbt.io.NBTOutputStream;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.Tag;
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

        SkinWardrobe wardrobe = new SkinWardrobe();

        wardrobe.setItem(SkinSlotType.OUTFIT, 0, new SkinDescriptor("db:00001", "armourers:outfit").asItemStack());
        wardrobe.setItem(SkinSlotType.SWORD, 0, new SkinDescriptor("db:00002", "armourers:sword").asItemStack());

        FriendlyByteBuf buf1 = new FriendlyByteBuf();

        buf1.writeInt(4); //UPDATE_WARDROBE(0x04, UpdateWardrobePacket.class, UpdateWardrobePacket::new),
        buf1.writeVarInt(0); //UpdateWardrobePacket.SYNC.SYNC
        buf1.writeInt(player.getEntityId());
        buf1.writeNbt(wardrobe.serializeNBT());

        player.sendPluginMessage(ArmourersWorkshop.INSTANCE, CHANNEL, buf1.array());
    }
}
