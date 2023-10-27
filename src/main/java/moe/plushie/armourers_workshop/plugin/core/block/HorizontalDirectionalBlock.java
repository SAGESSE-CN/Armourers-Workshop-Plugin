package moe.plushie.armourers_workshop.plugin.core.block;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.Direction;
import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.block.BlockStateProperties;
import net.cocoonmc.core.block.state.StateDefinition;
import net.cocoonmc.core.block.state.properties.EnumProperty;
import net.cocoonmc.core.item.context.BlockPlaceContext;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

public class HorizontalDirectionalBlock extends Block {

    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

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
        for (Direction direction : context.getNearestLookingDirections()) {
            if (direction == Direction.UP || direction == Direction.DOWN) {
                return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
            } else {
                return this.defaultBlockState().setValue(FACING, direction.getOpposite());
            }
        }
        return null;
    }
}
