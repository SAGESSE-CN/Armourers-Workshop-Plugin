package moe.plushie.armourers_workshop.core.block;

import moe.plushie.armourers_workshop.api.WorldAccessor;
import moe.plushie.armourers_workshop.core.blockentity.HologramProjectorBlockEntity;
import moe.plushie.armourers_workshop.core.blockentity.UpdatableContainerBlockEntity;
import moe.plushie.armourers_workshop.init.ModBlockEntitiyTypes;
import moe.plushie.armourers_workshop.init.ModMenuTypes;
import moe.plushie.armourers_workshop.init.platform.MenuManager;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.Direction;
import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.block.BlockEntity;
import net.cocoonmc.core.block.BlockEntitySupplier;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.block.BlockStateProperties;
import net.cocoonmc.core.block.state.StateDefinition;
import net.cocoonmc.core.block.state.properties.AttachFace;
import net.cocoonmc.core.block.state.properties.BooleanProperty;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.core.world.InteractionResult;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.Player;

public class HologramProjectorBlock extends AttachedDirectionalBlock implements BlockEntitySupplier {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public HologramProjectorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FACE, AttachFace.WALL).setValue(LIT, false));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntitiyTypes.HOLOGRAM_PROJECTOR.create(pos, state);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand) {
        return MenuManager.openMenu(ModMenuTypes.HOLOGRAM_PROJECTOR, player, WorldAccessor.of(level, blockPos));
    }

    @Override
    public void onRemove(Level level, BlockPos blockPos, BlockState oldBlockState, BlockState newBlockState, boolean bl) {
        if (!oldBlockState.is(newBlockState.getBlock())) {
            UpdatableContainerBlockEntity.dropContainerIfNeeded(level, blockPos);
        }
    }

    @Override
    public void onNeighborChanged(Level level, BlockPos pos, BlockState state, BlockPos sourcePos, Block sourceBlock, boolean bl) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof HologramProjectorBlockEntity) {
            ((HologramProjectorBlockEntity) blockEntity).updatePowerStats();
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FACE, LIT);
    }
}
