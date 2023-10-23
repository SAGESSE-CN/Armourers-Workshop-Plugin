package moe.plushie.armourers_workshop.plugin.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface IOriginalLevelChunk {

    void setOriginalBlockState(BlockPos pos, BlockState blockState);

    BlockState getOriginalBlockState(BlockPos pos);
}
