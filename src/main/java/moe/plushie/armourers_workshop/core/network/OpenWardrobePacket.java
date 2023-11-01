package moe.plushie.armourers_workshop.core.network;

import moe.plushie.armourers_workshop.api.IServerPacketHandler;
import moe.plushie.armourers_workshop.core.skin.SkinWardrobe;
import moe.plushie.armourers_workshop.init.ModMenuTypes;
import moe.plushie.armourers_workshop.init.platform.MenuManager;
import net.cocoonmc.core.network.FriendlyByteBuf;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.core.world.entity.Player;

public class OpenWardrobePacket extends CustomPacket {

    private final int entityId;

    public OpenWardrobePacket(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(entityId);
    }

    @Override
    public void accept(IServerPacketHandler packetHandler, Player player) {
        Level level = player.getLevel();
        Entity entity = level.getEntity(entityId);
        SkinWardrobe wardrobe = SkinWardrobe.of(entity);
        if (wardrobe != null && wardrobe.isEditable(player)) {
            MenuManager.openMenu(ModMenuTypes.WARDROBE, player, wardrobe);
        }
    }
}
