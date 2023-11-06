package moe.plushie.armourers_workshop.core.skin;

import moe.plushie.armourers_workshop.api.skin.ISkinType;
import moe.plushie.armourers_workshop.core.skin.serialize.SkinSerializer;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.math.Rectangle3i;
import net.cocoonmc.core.utils.MathHelper;

import java.util.HashMap;

public class Skin {

    public String path;

    private ISkinType type;
    private SkinProperties properties;

    private byte[] bytes;

    public Skin(ISkinType type, SkinProperties properties, byte[] bytes) {
        this.type = type;
        this.properties = properties;
        this.bytes = bytes;
    }

    public HashMap<BlockPos, Rectangle3i> getBlockBounds() {
        HashMap<BlockPos, Rectangle3i> blockBounds = new HashMap<>();
        if (type != SkinTypes.BLOCK) {
            return blockBounds;
        }
        HashMap<Long, Rectangle3i> blockGrid = new HashMap<>();
        SkinSerializer.readSkinBlockFromSkin(this).forEach(it -> {
            int tx = MathHelper.floor((it.getX() + 8) / 16f);
            int ty = MathHelper.floor((it.getY() + 8) / 16f);
            int tz = MathHelper.floor((it.getZ() + 8) / 16f);
            long key = BlockPos.asLong(-tx, -ty, tz);
            Rectangle3i rec = new Rectangle3i(-(it.getX() - tx * 16) - 1, -(it.getY() - ty * 16) - 1, it.getZ() - tz * 16, 1, 1, 1);
            blockGrid.computeIfAbsent(key, k -> rec).union(rec);
        });
        blockGrid.forEach((key, value) -> blockBounds.put(BlockPos.of(key), value));
        return blockBounds;
    }

    public ISkinType getType() {
        return type;
    }

    public SkinProperties getProperties() {
        return properties;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
