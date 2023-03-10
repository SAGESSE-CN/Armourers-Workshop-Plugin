package moe.plushie.armourers_workshop.plugin.network;

import moe.plushie.armourers_workshop.plugin.api.FriendlyByteBuf;
import moe.plushie.armourers_workshop.plugin.api.IServerPacketHandler;
import moe.plushie.armourers_workshop.plugin.core.menu.MenuManager;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinWardrobe;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinWardrobeMenu;
import moe.plushie.armourers_workshop.plugin.utils.ObjectUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

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
        Entity entity = ObjectUtils.findEntity(player.getWorld(), entityId);
        SkinWardrobe wardrobe = SkinWardrobe.of(entity);
        if (wardrobe != null && wardrobe.isEditable(player)) {
            SkinWardrobeMenu menu = new SkinWardrobeMenu(wardrobe, player);
            MenuManager.openMenu(menu, player, buf -> {
                buf.writeInt(player.getEntityId());
                buf.writeUtf("armourers_workshop:player");
            });
        }
    }
}
