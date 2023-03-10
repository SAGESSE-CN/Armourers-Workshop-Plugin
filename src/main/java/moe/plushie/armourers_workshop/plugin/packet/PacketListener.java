package moe.plushie.armourers_workshop.plugin.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import moe.plushie.armourers_workshop.plugin.api.ClickType;
import moe.plushie.armourers_workshop.plugin.api.Menu;
import moe.plushie.armourers_workshop.plugin.core.menu.MenuManager;
import org.bukkit.entity.Player;
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
        if (event.getPacketType().equals(PacketType.Play.Client.WINDOW_CLICK)) {
            Player player = event.getPlayer();
            if (player == null) {
                return;
            }
            PacketContainer packet = event.getPacket();
            int menuId = packet.getIntegers().read(0);
            Menu menu = MenuManager.getMenu(menuId);
            if (menu == null) {
                return;
            }
            int slot = packet.getIntegers().read(1);
            int button = packet.getIntegers().read(2);
            if (slot < 0) {
                return; // is drop, ignored.
            }
            String type = ((Enum<?>) packet.getStructures().read(1).getHandle()).name();
            menu.handSlotClick(slot, button, ClickType.valueOf(type), player);
            event.setCancelled(true);
        }
        if (event.getPacketType().equals(PacketType.Play.Client.CLOSE_WINDOW)) {
            PacketContainer packet = event.getPacket();
            int menuId = packet.getIntegers().read(0);
            MenuManager.clearMenu(menuId);
        }
    }
}
