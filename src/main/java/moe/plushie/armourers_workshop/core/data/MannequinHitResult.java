package moe.plushie.armourers_workshop.core.data;

import moe.plushie.armourers_workshop.core.item.MannequinItem;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.Direction;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.item.context.BlockHitResult;
import net.cocoonmc.core.math.Vector3d;
import net.cocoonmc.core.math.Vector3f;
import net.cocoonmc.core.utils.MathHelper;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.Player;

public class MannequinHitResult extends BlockHitResult {

    private final float scale;
    private final float rotation;

    public MannequinHitResult(BlockPos pos, Vector3d location, float scale, float rotation) {
        super(false, location, Direction.UP, pos, false);
        this.scale = scale;
        this.rotation = rotation;
    }

    public static MannequinHitResult test(Player player, Vector3d origin, Vector3d target, BlockPos pos) {
        Level level = player.getLevel();
        ItemStack itemStack = player.getMainHandItem();
        float scale = MannequinItem.getScale(itemStack);
        float rotation = (float) getAngleDegrees(origin.getX(), origin.getZ(), target.getX(), target.getZ()) + 90.0f;

        if (MannequinItem.isSmall(itemStack)) {
            scale *= 0.5f;
        }

//        BlockState blockState = level.getBlockState(pos);
        if (player.isSneaking()) {
//            VoxelShape shape = blockState.getShape(level, pos);
            target = new Vector3d(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5);
//            target = Vec3.upFromBottomCenterOf(pos, shape.max(Direction.Axis.Y));
//            VoxelShape collisionShape = blockState.getCollisionShape(level, pos);
//            if (!Block.isFaceFull(collisionShape, Direction.UP)) {
//                Vec3 newLocation = Vec3.atBottomCenterOf(pos); // can't stand, reset to bottom
//                if (!collisionShape.isEmpty()) {
//                    BlockHitResult collisionBox = shape.clip(target, newLocation, pos);
//                    if (collisionBox != null) {
//                        newLocation = collisionBox.getLocation();
//                    }
//                }
//                target = newLocation;
//            }
            Vector3f rot = player.getBodyRot();
            int l = MathHelper.floor(rot.getY() * 16 / 360 + 0.5) % 16;
            rotation = l * 22.5f + 180f;
        }

        // AABB box = MannequinEntity.STANDING_DIMENSIONS.scale(scale).makeBoundingBox(target);
        return new MannequinHitResult(pos, target, scale, rotation);
    }

    public float getScale() {
        return scale;
    }

    public float getRotation() {
        return rotation;
    }

    private static double getAngleRadians(double x1, double y1, double x2, double y2) {
        double x = x2 - x1;
        double y = y2 - y1;
        if (x == 0 && y == 0) {
            return 0;
        }
        return Math.atan2(y, x);
    }

    private static double getAngleDegrees(double x1, double y1, double x2, double y2) {
        return Math.toDegrees(getAngleRadians(x1, y1, x2, y2));
    }
}
