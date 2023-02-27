package moe.plushie.armourers_workshop.plugin.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
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
        if (event.getPacketType() == PacketType.Play.Client.WINDOW_CLICK) {
            PacketContainer packet = event.getPacket();
            event.getPlayer().sendMessage("你点击了窗口ID: " + packet.getIntegers().read(0)
                    + " 槽位ID: " +packet.getIntegers().read(1));

        }
    }

}
