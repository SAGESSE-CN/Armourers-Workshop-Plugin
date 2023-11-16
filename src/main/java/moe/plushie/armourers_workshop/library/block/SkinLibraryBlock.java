package moe.plushie.armourers_workshop.library.block;

import moe.plushie.armourers_workshop.core.block.HorizontalDirectionalBlock;
import moe.plushie.armourers_workshop.core.blockentity.UpdatableContainerBlockEntity;
import moe.plushie.armourers_workshop.init.ModBlockEntityTypes;
import moe.plushie.armourers_workshop.init.ModBlocks;
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

public class SkinLibraryBlock extends HorizontalDirectionalBlock implements BlockEntitySupplier {

    public SkinLibraryBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntityTypes.SKIN_LIBRARY.get().create(pos, state);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand) {
        if (this == ModBlocks.SKIN_LIBRARY_CREATIVE.get()) {
            return MenuManager.openMenu(ModMenuTypes.SKIN_LIBRARY_CREATIVE, level.getBlockEntity(blockPos), player);
        }
        if (this == ModBlocks.SKIN_LIBRARY.get()) {
            return MenuManager.openMenu(ModMenuTypes.SKIN_LIBRARY, level.getBlockEntity(blockPos), player);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(Level level, BlockPos blockPos, BlockState oldBlockState, BlockState newBlockState, boolean bl) {
        if (!oldBlockState.is(newBlockState.getBlock())) {
            UpdatableContainerBlockEntity.dropContainerIfNeeded(level, blockPos);
        }
    }
}
