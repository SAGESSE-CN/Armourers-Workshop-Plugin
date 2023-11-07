package moe.plushie.armourers_workshop.api;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.block.BlockEntity;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.world.Level;

public interface WorldAccessor {

    Block getBlock();

    BlockEntity getBlockEntity();

    BlockPos getBlockPos();

    Level getLevel();


    static WorldAccessor of(Level level, BlockPos blockPos) {
        return new WorldAccessor() {

            @Override
            public Block getBlock() {
                BlockState state = level.getBlockState(blockPos);
                if (state != null) {
                    return state.getBlock();
                }
                return null;
            }

            @Override
            public BlockEntity getBlockEntity() {
                return level.getBlockEntity(blockPos);
            }

            @Override
            public BlockPos getBlockPos() {
                return blockPos;
            }

            @Override
            public Level getLevel() {
                return level;
            }
        };
    }
}
