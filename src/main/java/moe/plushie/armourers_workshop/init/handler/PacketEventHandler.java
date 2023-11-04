package moe.plushie.armourers_workshop.init.handler;

import moe.plushie.armourers_workshop.ArmourersWorkshopImpl;
import moe.plushie.armourers_workshop.core.network.NetworkManager;
import moe.plushie.armourers_workshop.core.skin.EntityProfile;
import moe.plushie.armourers_workshop.init.ModEntityProfiles;
import net.cocoonmc.Cocoon;
import net.cocoonmc.core.network.protocol.ClientboundAddEntityPacket;
import net.cocoonmc.core.network.protocol.Packet;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.core.world.entity.Player;
import org.bukkit.Bukkit;

public class PacketEventHandler {

    public static void init() {

        Cocoon.API.TRANSFORMER.register(PacketEventHandler::handleAddEntity, ClientboundAddEntityPacket.class);
    }

    public static Packet handleAddEntity(ClientboundAddEntityPacket packet, Player player) {
        int entityId = packet.getEntityId();
        Bukkit.getScheduler().runTask(ArmourersWorkshopImpl.INSTANCE, () -> {
            Level level = player.getLevel();
            Entity entity = level.getEntity(entityId);
            if (entity == null) {
                return;
            }
            EntityProfile entityProfile = ModEntityProfiles.getProfile(entity);
            if (entityProfile != null) {
                NetworkManager.sendWardrobeTo(entity, player);
            }
        });
        return packet;
    }
}
