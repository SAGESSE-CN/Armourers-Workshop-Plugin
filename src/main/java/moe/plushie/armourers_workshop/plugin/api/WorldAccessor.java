package moe.plushie.armourers_workshop.plugin.api;

import moe.plushie.armourers_workshop.plugin.utils.BukkitUtils;
import org.bukkit.Location;
import org.bukkit.World;

public interface WorldAccessor {

    Block getBlock();

    BlockEntity getBlockEntity();

    BlockPos getBlockPos();

    World getWorld();


    static WorldAccessor of(World world, BlockPos blockPos) {
        Location location = new Location(world, blockPos.getX(), blockPos.getY(), blockPos.getZ());
        return new WorldAccessor() {

            @Override
            public Block getBlock() {
                return BukkitUtils.wrap(location.getBlock());
            }

            @Override
            public BlockEntity getBlockEntity() {
                return BukkitUtils.getBlockEntity(location.getBlock());
            }

            @Override
            public BlockPos getBlockPos() {
                return blockPos;
            }

            @Override
            public World getWorld() {
                return world;
            }
        };
    }
}
