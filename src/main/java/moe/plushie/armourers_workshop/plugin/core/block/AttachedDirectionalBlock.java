package moe.plushie.armourers_workshop.plugin.core.block;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.Direction;
import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.block.BlockStateProperties;
import net.cocoonmc.core.block.state.StateDefinition;
import net.cocoonmc.core.block.state.properties.AttachFace;
import net.cocoonmc.core.block.state.properties.EnumProperty;
import net.cocoonmc.core.item.context.BlockPlaceContext;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

public class AttachedDirectionalBlock extends HorizontalDirectionalBlock {

    public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;

    public AttachedDirectionalBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canSurvive(BlockState blockState, World world, BlockPos blockPos) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FACE);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        for (Direction direction : context.getNearestLookingDirections()) {
            BlockState blockstate;
            if (direction == Direction.UP || direction == Direction.DOWN) {
                blockstate = this.defaultBlockState().setValue(FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR).setValue(FACING, context.getHorizontalDirection().getOpposite());
            } else {
                blockstate = this.defaultBlockState().setValue(FACE, AttachFace.WALL).setValue(FACING, direction.getOpposite());
            }
            if (blockstate.canSurvive(context.getWorld(), context.getClickedPos())) {
                return blockstate;
            }
        }
        return null;
    }
}
