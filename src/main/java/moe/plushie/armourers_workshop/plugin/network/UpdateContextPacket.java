package moe.plushie.armourers_workshop.plugin.network;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import moe.plushie.armourers_workshop.plugin.api.FriendlyByteBuf;
import moe.plushie.armourers_workshop.plugin.init.ModConfig;
import moe.plushie.armourers_workshop.plugin.init.ModConstants;
import moe.plushie.armourers_workshop.plugin.init.ModContext;
import moe.plushie.armourers_workshop.plugin.init.ModLog;
import moe.plushie.armourers_workshop.plugin.network.CustomPacket;
import org.bukkit.entity.Player;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
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
        if (buffer.readBoolean()) {
            ModContext.init(buffer.readUUID(), buffer.readUUID());
            checkNetworkVersion(buffer.readUtf());
        }
        readConfigSpec(buffer);
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

    private void readConfigSpec(FriendlyByteBuf buffer) {
        int size = buffer.readInt();
        if (size == 0) {
            return;
        }
        try {
            HashMap<String, Object> fields = new HashMap<>();
            ByteBufInputStream bi = new ByteBufInputStream(buffer);
            ObjectInputStream oi = new ObjectInputStream(bi);
            for (int i = 0; i < size; ++i) {
                String name = oi.readUTF();
                Object value = oi.readObject();
                fields.put(name, value);
            }
            oi.close();
            ModConfig.apply(fields);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkNetworkVersion(String version) {
        if (version.equals(ModConstants.MOD_NET_ID)) {
            ModLog.warn("inconsistent network protocol version, server: {}, client: {}", version, ModConstants.MOD_NET_ID);
        }
    }
}
