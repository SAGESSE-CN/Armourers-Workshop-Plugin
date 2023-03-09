package moe.plushie.armourers_workshop.plugin.network;

import moe.plushie.armourers_workshop.plugin.api.FMLPacket;
import moe.plushie.armourers_workshop.plugin.api.FriendlyByteBuf;
import moe.plushie.armourers_workshop.plugin.api.TextComponent;

public class FMLOpenContainer extends CustomPacket implements FMLPacket {

    private final int id;
    private final int windowId;
    private final TextComponent title;
    private final FriendlyByteBuf additionalData;

    public FMLOpenContainer(int id, int windowId, TextComponent title, FriendlyByteBuf additionalData) {
        this.id = id;
        this.windowId = windowId;
        this.title = title;
        this.additionalData = additionalData;
    }
//
//    public FMLOpenContainer(FriendlyByteBuf buffer) {
//        this.id = buffer.readVarInt();
//        this.windowId = buffer.readVarInt();
//        this.title = buffer.readComponent();
//        this.additionalData = new FriendlyByteBuf(Unpooled.wrappedBuffer(buffer.readByteArray(32600)));
//    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeByte(1);  // open container id
        buffer.writeVarInt(id); // from Registry.MENU.getId((MenuType<?>)id)
        buffer.writeVarInt(windowId);
        buffer.writeComponent(title);
        buffer.writeByteArray(additionalData.array());
    }
}
