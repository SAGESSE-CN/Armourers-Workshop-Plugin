package moe.plushie.armourers_workshop.plugin.api;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class BlockPlaceContext extends UseOnContext {

    public BlockPlaceContext(UseOnContext context) {
        super(context.getPlayer(), context.getItem(), context.getClickedBlock(), context.getClickedFace(), context.getHand());
    }

    public BlockFace getNearestLookingDirection() {
        return findOrderedByNearest(player)[0];
    }

//    public BlockFace getNearestLookingVerticalDirection() {
//        return Direction.getFacingAxis(this.getPlayer(), Direction.Axis.Y);
//    }

    public BlockFace[] getNearestLookingDirections() {
        int i;
        BlockFace[] directions = findOrderedByNearest(getPlayer());
        if (getClickedBlock().isEmpty()) {
            return directions;
        }
        BlockFace direction = getClickedFace();
        for (i = 0; i < directions.length && directions[i] != direction.getOppositeFace(); ++i) {
        }
        if (i > 0) {
            System.arraycopy(directions, 0, directions, 1, i);
            directions[0] = direction.getOppositeFace();
        }
        return directions;
    }


    private BlockFace[] findOrderedByNearest(Entity entity) {
        Vector vec = entity.getLocation().getDirection();
        double f = vec.getX() * (Math.PI / 180);
        double g = -vec.getY() * (Math.PI / 180);
        double h = Math.sin(f);
        double i = Math.cos(f);
        double j = Math.sin(g);
        double k = Math.cos(g);
        boolean bl = j > 0.0f;
        boolean bl2 = h < 0.0f;
        boolean bl3 = k > 0.0f;
        double l = bl ? j : -j;
        double m = bl2 ? -h : h;
        double n = bl3 ? k : -k;
        double o = l * i;
        double p = n * i;
        BlockFace direction = bl ? BlockFace.EAST : BlockFace.WEST;
        BlockFace direction2 = bl2 ? BlockFace.UP : BlockFace.DOWN;
        BlockFace direction3 = bl3 ? BlockFace.SOUTH : BlockFace.NORTH;
        if (l > n) {
            if (m > o) {
                return makeDirectionArray(direction2, direction, direction3);
            }
            if (p > m) {
                return makeDirectionArray(direction, direction3, direction2);
            }
            return makeDirectionArray(direction, direction2, direction3);
        }
        if (m > p) {
            return makeDirectionArray(direction2, direction3, direction);
        }
        if (o > m) {
            return makeDirectionArray(direction3, direction, direction2);
        }
        return makeDirectionArray(direction3, direction2, direction);
    }

    private static BlockFace[] makeDirectionArray(BlockFace direction, BlockFace direction2, BlockFace direction3) {
        return new BlockFace[]{direction, direction2, direction3, direction3.getOppositeFace(), direction2.getOppositeFace(), direction.getOppositeFace()};
    }
}
