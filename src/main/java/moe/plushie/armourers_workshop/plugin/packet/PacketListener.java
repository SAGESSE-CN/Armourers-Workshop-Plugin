package moe.plushie.armourers_workshop.plugin.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import moe.plushie.armourers_workshop.plugin.api.Menu;
import moe.plushie.armourers_workshop.plugin.core.menu.MenuManager;
import moe.plushie.armourers_workshop.plugin.init.ModLog;
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
        if (event.getPacketType() != PacketType.Play.Client.WINDOW_CLICK) {
            return;
        }
        PacketContainer packet = event.getPacket();
        int menuId = packet.getIntegers().read(0);
        Menu menu = MenuManager.getMenu(menuId);
        if (menu == null) {
            return;
        }
        int slotId = packet.getIntegers().read(1);
        String type = ((Enum<?>) packet.getStructures().read(1).getHandle()).name();
        menu.handSlotClick(slotId, type);
        event.setCancelled(true);
    }
}
