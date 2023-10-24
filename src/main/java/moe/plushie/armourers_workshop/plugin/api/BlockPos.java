package moe.plushie.armourers_workshop.plugin.api;

import moe.plushie.armourers_workshop.plugin.utils.MathUtils;
import org.bukkit.Location;

import java.util.Objects;

public class BlockPos {

    public static BlockPos ZERO = new BlockPos(0, 0, 0);

    private static final int PACKED_X_LENGTH;
    private static final int PACKED_Z_LENGTH;
    private static final int PACKED_Y_LENGTH;
    private static final long PACKED_X_MASK;
    private static final long PACKED_Y_MASK;
    private static final long PACKED_Z_MASK;
    private static final int Y_OFFSET = 0;
    private static final int Z_OFFSET;
    private static final int X_OFFSET;

    private final int x;
    private final int y;
    private final int z;

    public BlockPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static BlockPos of(Location loc) {
        return new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public static BlockPos of(long l) {
        return new BlockPos(BlockPos.getX(l), BlockPos.getY(l), BlockPos.getZ(l));
    }

    public static int getX(long l) {
        return (int) (l << 64 - X_OFFSET - PACKED_X_LENGTH >> 64 - PACKED_X_LENGTH);
    }

    public static int getY(long l) {
        return (int) (l << 64 - PACKED_Y_LENGTH >> 64 - PACKED_Y_LENGTH);
    }

    public static int getZ(long l) {
        return (int) (l << 64 - Z_OFFSET - PACKED_Z_LENGTH >> 64 - PACKED_Z_LENGTH);
    }

    public static long asLong(int i, int j, int k) {
        long l = 0L;
        l |= ((long) i & PACKED_X_MASK) << X_OFFSET;
        l |= ((long) j & PACKED_Y_MASK) << 0;
        return l |= ((long) k & PACKED_Z_MASK) << Z_OFFSET;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public long asLong() {
        return BlockPos.asLong(this.getX(), this.getY(), this.getZ());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlockPos)) return false;
        BlockPos blockPos = (BlockPos) o;
        return x == blockPos.x && y == blockPos.y && z == blockPos.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    static {
        PACKED_Z_LENGTH = PACKED_X_LENGTH = 1 + MathUtils.log2(MathUtils.smallestEncompassingPowerOfTwo(30000000));
        PACKED_Y_LENGTH = 64 - PACKED_X_LENGTH - PACKED_Z_LENGTH;
        PACKED_X_MASK = (1L << PACKED_X_LENGTH) - 1L;
        PACKED_Y_MASK = (1L << PACKED_Y_LENGTH) - 1L;
        PACKED_Z_MASK = (1L << PACKED_Z_LENGTH) - 1L;
        Z_OFFSET = PACKED_Y_LENGTH;
        X_OFFSET = PACKED_Y_LENGTH + PACKED_Z_LENGTH;
    }
}
