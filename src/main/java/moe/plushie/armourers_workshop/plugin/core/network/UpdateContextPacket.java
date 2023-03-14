package moe.plushie.armourers_workshop.plugin.core.network;

import io.netty.buffer.ByteBufOutputStream;
import moe.plushie.armourers_workshop.plugin.api.FriendlyByteBuf;
import moe.plushie.armourers_workshop.plugin.init.ModConfig;
import moe.plushie.armourers_workshop.plugin.init.ModConstants;
import moe.plushie.armourers_workshop.plugin.init.ModContext;
import org.bukkit.entity.Player;

import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.UUID;

public class UpdateContextPacket extends CustomPacket {

    private UUID token = null;

    public UpdateContextPacket() {
    }

    public UpdateContextPacket(Player player) {
        this.token = player.getUniqueId();
    }

    public UpdateContextPacket(FriendlyByteBuf buffer) {
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        if (token != null) {
            buffer.writeBoolean(true);
            buffer.writeUUID(ModContext.t2(token));
            buffer.writeUUID(ModContext.t3(token));
            buffer.writeUtf(ModConstants.MOD_NET_ID);
        } else {
            buffer.writeBoolean(false);
        }
        writeConfigSpec(buffer);
    }

    private void writeConfigSpec(FriendlyByteBuf buffer) {
        try {
            Map<String, Object> fields = ModConfig.snapshot();
            buffer.writeInt(fields.size());
            if (fields.size() == 0) {
                return;
            }
            ByteBufOutputStream bo = new ByteBufOutputStream(buffer);
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            for (Map.Entry<String, Object> entry : fields.entrySet()) {
                oo.writeUTF(entry.getKey());
                oo.writeObject(entry.getValue());
            }
            oo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
