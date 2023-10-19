package moe.plushie.armourers_workshop.plugin.init.handler;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import moe.plushie.armourers_workshop.plugin.core.network.NetworkManager;
import moe.plushie.armourers_workshop.plugin.core.skin.EntityProfile;
import moe.plushie.armourers_workshop.plugin.init.ModEntityProfiles;
import moe.plushie.armourers_workshop.plugin.utils.BukkitUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PacketEventHandler extends PacketAdapter {

    public PacketEventHandler(Plugin plugin) {
        super(plugin, PacketType.Play.Server.SPAWN_ENTITY);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        int entityId = event.getPacket().getIntegers().read(0);
        Player player = event.getPlayer();
        Entity entity = BukkitUtils.findEntity(player.getWorld(), entityId);
        if (entity == null) {
            return;
        }
        EntityProfile entityProfile = ModEntityProfiles.getProfile(entity);
        if (entityProfile != null) {
            NetworkManager.sendWardrobeTo(entity, player);
        }
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
    }
}
