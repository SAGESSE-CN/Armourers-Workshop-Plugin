package moe.plushie.armourers_workshop.core.block;

import moe.plushie.armourers_workshop.core.blockentity.UpdatableContainerBlockEntity;
import moe.plushie.armourers_workshop.init.ModBlockEntityTypes;
import moe.plushie.armourers_workshop.init.ModMenuTypes;
import moe.plushie.armourers_workshop.init.platform.MenuManager;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.BlockEntity;
import net.cocoonmc.core.block.BlockEntitySupplier;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.core.world.InteractionResult;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.Player;

public class DyeTableBlock extends HorizontalDirectionalBlock implements BlockEntitySupplier {

    public DyeTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntityTypes.DYE_TABLE.get().create(pos, state);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand) {
        return MenuManager.openMenu(ModMenuTypes.DYE_TABLE, level.getBlockEntity(blockPos), player);
    }

    @Override
    public void onRemove(Level level, BlockPos blockPos, BlockState oldBlockState, BlockState newBlockState, boolean bl) {
        if (!oldBlockState.is(newBlockState.getBlock())) {
            UpdatableContainerBlockEntity.dropContainerIfNeeded(level, blockPos);
        }
    }
}
