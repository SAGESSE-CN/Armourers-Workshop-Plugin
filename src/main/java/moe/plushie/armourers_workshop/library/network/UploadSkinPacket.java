package moe.plushie.armourers_workshop.library.network;


import moe.plushie.armourers_workshop.api.IServerPacketHandler;
import moe.plushie.armourers_workshop.core.network.CustomPacket;
import moe.plushie.armourers_workshop.library.menu.GlobalSkinLibraryMenu;
import net.cocoonmc.core.network.FriendlyByteBuf;
import net.cocoonmc.core.world.entity.Player;

public class UploadSkinPacket extends CustomPacket {

    public UploadSkinPacket() {
    }

    public UploadSkinPacket(FriendlyByteBuf buffer) {
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
    }

    @Override
    public void accept(IServerPacketHandler packetHandler, Player player) {
        if (player.getActivedMenu() instanceof GlobalSkinLibraryMenu) {
            GlobalSkinLibraryMenu container = (GlobalSkinLibraryMenu) player.getActivedMenu();
            container.crafting();
        }
    }
}
