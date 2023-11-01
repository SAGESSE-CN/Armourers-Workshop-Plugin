package moe.plushie.armourers_workshop.library.block;

import moe.plushie.armourers_workshop.api.WorldAccessor;
import moe.plushie.armourers_workshop.core.block.HorizontalDirectionalBlock;
import moe.plushie.armourers_workshop.init.ModBlocks;
import moe.plushie.armourers_workshop.init.ModMenuTypes;
import moe.plushie.armourers_workshop.init.platform.MenuManager;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.core.world.InteractionResult;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.Player;

public class SkinLibraryBlock extends HorizontalDirectionalBlock {

    public SkinLibraryBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand) {
        if (this == ModBlocks.SKIN_LIBRARY_CREATIVE) {
            return MenuManager.openMenu(ModMenuTypes.SKIN_LIBRARY_CREATIVE, player, WorldAccessor.of(level, blockPos));
        }
        if (this == ModBlocks.SKIN_LIBRARY) {
            return MenuManager.openMenu(ModMenuTypes.SKIN_LIBRARY, player, WorldAccessor.of(level, blockPos));
        }
        return InteractionResult.CONSUME;
    }

    //    @Override
//    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean p_196243_5_) {
//        if (state.is(newState.getBlock())) {
//            return;
//        }
//        SkinLibraryBlockEntity blockEntity = ObjectUtils.safeCast(level.getBlockEntity(pos), SkinLibraryBlockEntity.class);
//        if (blockEntity != null) {
//            DataSerializers.dropContents(level, pos, blockEntity.getInventory());
//        }
//        super.onRemove(state, level, pos, newState, p_196243_5_);
//    }
}
