package moe.plushie.armourers_workshop.plugin.network.packets;

import moe.plushie.armourers_workshop.plugin.core.skin.SkinLoader;
import moe.plushie.armourers_workshop.plugin.init.ModLog;
import moe.plushie.armourers_workshop.plugin.network.CustomPacket;
import moe.plushie.armourers_workshop.plugin.api.IServerPacketHandler;
import moe.plushie.armourers_workshop.plugin.network.NetworkManager;
import moe.plushie.armourers_workshop.plugin.api.FriendlyByteBuf;
import org.bukkit.entity.Player;

public class RequestSkinPacket extends CustomPacket {

    private final String identifier;

    public RequestSkinPacket(String identifier) {
        this.identifier = identifier;
    }

    public RequestSkinPacket(FriendlyByteBuf buffer) {
        this.identifier = buffer.readUtf(Short.MAX_VALUE);
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(identifier);
    }

    @Override
    public void accept(IServerPacketHandler packetHandler, Player player) {
        ModLog.debug("'{}' => accept skin request", identifier);
        SkinLoader.getInstance().loadSkin(identifier, (skin, exception) -> {
            ModLog.debug("'{}' => response skin data, exception: {}", identifier, exception);
            ResponseSkinPacket packet = new ResponseSkinPacket(identifier, skin, exception);
            NetworkManager.sendTo(packet, player);
        });
    }
}
