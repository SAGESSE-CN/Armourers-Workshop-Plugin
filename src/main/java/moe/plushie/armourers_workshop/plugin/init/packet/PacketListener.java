package moe.plushie.armourers_workshop.plugin.init.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.plugin.Plugin;

public class PacketListener extends PacketAdapter {

    public PacketListener(Plugin plugin, PacketType... types) {
        super(plugin, types);
    }

    @Override
    public void onPacketSending(PacketEvent ev) {

    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
//        if (event.getPacketType().equals(PacketType.Play.Client.WINDOW_CLICK)) {
//            Player player = event.getPlayer();
//            if (player == null) {
//                return;
//            }
//            PacketContainer packet = event.getPacket();
//            int menuId = packet.getIntegers().read(0);
//            Menu menu = MenuManager.getMenu(menuId);
//            if (menu == null) {
//                return;
//            }
//            int slot = packet.getIntegers().read(1);
//            int button = packet.getIntegers().read(2);
//            String type = ((Enum<?>) packet.getStructures().read(1).getHandle()).name();
//            ClickType clickType = ClickType.valueOf(type);
//            HandleResult result = menu.handSlotClick(slot, button, clickType, player);
//            ModLog.info("slot: {}, button: {}, click: {} => {}/{}", slot, button, clickType, result, player.getItemOnCursor());
//            if (result != HandleResult.PASS) {
//                event.setCancelled(true);
//            }
//        }
//        if (event.getPacketType().equals(PacketType.Play.Client.CLOSE_WINDOW)) {
//            PacketContainer packet = event.getPacket();
//            int menuId = packet.getIntegers().read(0);
//            MenuManager.clearMenu(menuId);
//        }
    }
}
