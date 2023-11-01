package moe.plushie.armourers_workshop.init.handler;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import moe.plushie.armourers_workshop.core.network.NetworkManager;
import moe.plushie.armourers_workshop.core.skin.EntityProfile;
import moe.plushie.armourers_workshop.init.ModEntityProfiles;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.core.world.entity.Player;
import org.bukkit.plugin.Plugin;

public class PacketEventHandler extends PacketAdapter {

    public PacketEventHandler(Plugin plugin) {
        super(plugin, PacketType.Play.Server.SPAWN_ENTITY);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        int entityId = event.getPacket().getIntegers().read(0);
        Player player = Player.of(event.getPlayer());
        Level level = player.getLevel();
        Entity entity = level.getEntity(entityId);
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
