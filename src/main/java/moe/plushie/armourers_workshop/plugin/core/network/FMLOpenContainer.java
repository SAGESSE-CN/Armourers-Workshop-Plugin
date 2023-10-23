package moe.plushie.armourers_workshop.plugin.core.network;

import moe.plushie.armourers_workshop.plugin.api.FMLPacket;
import moe.plushie.armourers_workshop.plugin.api.FriendlyByteBuf;
import moe.plushie.armourers_workshop.plugin.api.Component;
import net.md_5.bungee.api.chat.BaseComponent;

public class FMLOpenContainer extends CustomPacket implements FMLPacket {

    private final String id;
    private final int windowId;
    private final Component title;
    private final FriendlyByteBuf additionalData;

    public FMLOpenContainer(String id, int windowId, Component title, FriendlyByteBuf additionalData) {
        this.id = id;
        this.windowId = windowId;
        this.title = title;
        this.additionalData = additionalData;
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeByte(1);  // open container id
        buffer.writeVarInt(Integer.MAX_VALUE); // yep, a special flag by the addon mod.
        buffer.writeUtf(id);
        buffer.writeVarInt(windowId);
        buffer.writeComponent(title);
        buffer.writeByteArray(additionalData.array());
    }
}
