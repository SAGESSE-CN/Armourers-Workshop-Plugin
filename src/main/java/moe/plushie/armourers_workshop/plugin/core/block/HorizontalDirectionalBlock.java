package moe.plushie.armourers_workshop.plugin.core.block;

import moe.plushie.armourers_workshop.plugin.api.Block;
import moe.plushie.armourers_workshop.plugin.api.BlockPlaceContext;
import moe.plushie.armourers_workshop.plugin.api.BlockPos;
import moe.plushie.armourers_workshop.plugin.api.BlockState;
import moe.plushie.armourers_workshop.plugin.api.BlockStateProperties;
import moe.plushie.armourers_workshop.plugin.api.state.StateDefinition;
import moe.plushie.armourers_workshop.plugin.api.state.properties.EnumProperty;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.Nullable;

public class HorizontalDirectionalBlock extends Block {

    public static final EnumProperty<BlockFace> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public HorizontalDirectionalBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canSurvive(BlockState blockState, World world, BlockPos blockPos) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        for (BlockFace direction : context.getNearestLookingDirections()) {
            if (direction == BlockFace.UP || direction == BlockFace.DOWN) {
                return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOppositeFace());
            } else {
                return this.defaultBlockState().setValue(FACING, direction.getOppositeFace());
            }
        }
        return null;
    }
}
