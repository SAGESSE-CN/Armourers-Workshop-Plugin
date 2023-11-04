package moe.plushie.armourers_workshop.core.block;

import moe.plushie.armourers_workshop.init.ModBlockEntities;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.BlockEntity;
import net.cocoonmc.core.block.BlockEntitySupplier;
import net.cocoonmc.core.block.BlockState;

public class SkinnableBlock extends AttachedDirectionalBlock implements BlockEntitySupplier {

    public SkinnableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.SKINNABLE.create(pos, state);
    }
}
