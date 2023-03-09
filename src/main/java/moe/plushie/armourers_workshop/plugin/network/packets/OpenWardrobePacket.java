package moe.plushie.armourers_workshop.plugin.network.packets;

import moe.plushie.armourers_workshop.plugin.ArmourersWorkshop;
import moe.plushie.armourers_workshop.plugin.network.CustomPacket;
import moe.plushie.armourers_workshop.plugin.api.IServerPacketHandler;
import moe.plushie.armourers_workshop.plugin.api.FriendlyByteBuf;
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
//        Entity entity = player.level.getEntity(entityId);
//        SkinWardrobe wardrobe = SkinWardrobe.of(entity);
//        if (wardrobe != null && wardrobe.isEditable(player)) {
//            MenuManager.openMenu(ModMenuTypes.WARDROBE, player, wardrobe);
//        }

        FriendlyByteBuf buf1 = new FriendlyByteBuf();
        buf1.writeByte(1);  // "fml:play" 打开容器标识
        buf1.writeVarInt(24); // UI 标识
        buf1.writeVarInt(100); //
        buf1.writeUtf("{\"translate\":\"inventory.armourers_workshop.wardrobe\"}");

        FriendlyByteBuf buf2 = new FriendlyByteBuf();
        buf2.writeInt(player.getEntityId());
        buf2.writeUtf("armourers_workshop:player");

        buf1.writeByteArray(buf2.array());

        player.sendPluginMessage(ArmourersWorkshop.INSTANCE, "fml:play", buf1.array());
    }
}
