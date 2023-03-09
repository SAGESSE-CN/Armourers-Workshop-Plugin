package moe.plushie.armourers_workshop.plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import moe.plushie.armourers_workshop.plugin.core.data.DataManager;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinDescriptor;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinSlotType;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinWardrobe;
import moe.plushie.armourers_workshop.plugin.init.ModConfig;
import moe.plushie.armourers_workshop.plugin.init.ModPackets;
import moe.plushie.armourers_workshop.plugin.network.NetworkManager;
import moe.plushie.armourers_workshop.plugin.network.UpdateContextPacket;
import moe.plushie.armourers_workshop.plugin.packet.PacketListener;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;

public final class ArmourersWorkshop extends JavaPlugin implements Listener {

    public static String CHANNEL = "armourers_workshop:play";

    public static ArmourersWorkshop INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;

        ModConfig.init();
        ModPackets.init();
        NetworkManager.init();
        DataManager.start();

        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new PacketListener(this, PacketType.Play.Client.WINDOW_CLICK));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        DataManager.stop();
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        try {
            Class<? extends CommandSender> senderClass = player.getClass();
            Method addChannel = senderClass.getDeclaredMethod("addChannel", String.class);
            addChannel.setAccessible(true);
            addChannel.invoke(player, CHANNEL);
            // first join, send the context.
            NetworkManager.sendTo(new UpdateContextPacket(player), player);
            sendSync(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
//        Player player = event.getPlayer();
//
//        FriendlyByteBuf buf1 = new FriendlyByteBuf();
//        buf1.writeByte(1);  // "fml:play" 打开容器标识
//        buf1.writeVarInt(24); // UI 标识
//        buf1.writeVarInt(100); //
//        buf1.writeUtf("{\"translate\":\"inventory.armourers_workshop.wardrobe\"}");
//
//        FriendlyByteBuf buf2 = new FriendlyByteBuf();
//        buf2.writeInt(event.getRightClicked().getEntityId());
//        buf2.writeUtf("armourers_workshop:player");
//
//        buf1.writeByteArray(buf2.array());
//
//        player.sendPluginMessage(ArmourersWorkshop.INSTANCE, "fml:play", buf1.array());

    }

    private void sendSync(Player player) {

        SkinWardrobe wardrobe = new SkinWardrobe(player);

        wardrobe.setItem(SkinSlotType.OUTFIT, 0, new SkinDescriptor("db:00001", "armourers:outfit").asItemStack());
        wardrobe.setItem(SkinSlotType.SWORD, 0, new SkinDescriptor("db:00002", "armourers:sword").asItemStack());

        wardrobe.broadcast(player);
    }
}
