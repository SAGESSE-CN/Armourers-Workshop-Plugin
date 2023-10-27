package moe.plushie.armourers_workshop.plugin.api;

import net.cocoonmc.Cocoon;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.block.BlockEntity;
import org.bukkit.Location;
import org.bukkit.World;

public interface WorldAccessor {

    Block getBlock();

    BlockEntity getBlockEntity();

    BlockPos getBlockPos();

    World getWorld();


    static WorldAccessor of(World world, BlockPos blockPos) {
        return new WorldAccessor() {

            @Override
            public Block getBlock() {
                Location location = new Location(world, blockPos.getX(), blockPos.getY(), blockPos.getZ());
//                return location.getBlock();
//                return BukkitUtils.wrap(location.getBlock());
                return null;
            }

            @Override
            public BlockEntity getBlockEntity() {
                return Cocoon.API.BLOCK.getBlockEntity(world, blockPos);
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
