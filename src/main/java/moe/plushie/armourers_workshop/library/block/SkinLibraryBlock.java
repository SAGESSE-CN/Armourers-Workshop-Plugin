package moe.plushie.armourers_workshop.library.block;

import moe.plushie.armourers_workshop.api.WorldAccessor;
import moe.plushie.armourers_workshop.core.block.HorizontalDirectionalBlock;
import moe.plushie.armourers_workshop.core.blockentity.UpdatableContainerBlockEntity;
import moe.plushie.armourers_workshop.init.ModBlockEntities;
import moe.plushie.armourers_workshop.init.ModBlocks;
import moe.plushie.armourers_workshop.init.ModMenuTypes;
import moe.plushie.armourers_workshop.init.platform.MenuManager;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.BlockEntity;
import net.cocoonmc.core.block.BlockEntitySupplier;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.math.Vector3f;
import net.cocoonmc.core.utils.ContainerHelper;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.core.world.InteractionResult;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.Player;

import java.util.List;

public class SkinLibraryBlock extends HorizontalDirectionalBlock implements BlockEntitySupplier {

    public SkinLibraryBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.SKIN_LIBRARY.create(pos, state);
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

    @Override
    public void onRemove(Level level, BlockPos blockPos, BlockState oldBlockState, BlockState newBlockState, boolean bl) {
        if (!oldBlockState.is(newBlockState.getBlock())) {
            UpdatableContainerBlockEntity.dropContainerIfNeeded(level, blockPos);
        }
    }
}
