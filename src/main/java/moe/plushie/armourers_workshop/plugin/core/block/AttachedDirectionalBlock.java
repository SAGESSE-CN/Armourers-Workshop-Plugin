package moe.plushie.armourers_workshop.plugin.core.block;

import moe.plushie.armourers_workshop.plugin.api.AttachFace;
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
        for (BlockFace direction : context.getNearestLookingDirections()) {
            BlockState blockstate;
            if (direction == BlockFace.UP || direction == BlockFace.DOWN) {
                blockstate = this.defaultBlockState().setValue(FACE, direction == BlockFace.UP ? AttachFace.CEILING : AttachFace.FLOOR).setValue(FACING, context.getHorizontalDirection().getOppositeFace());
            } else {
                blockstate = this.defaultBlockState().setValue(FACE, AttachFace.WALL).setValue(FACING, direction.getOppositeFace());
            }
            if (blockstate.canSurvive(context.getWorld(), context.getClickedPos())) {
                return blockstate;
            }
        }
        return null;
    }
}
