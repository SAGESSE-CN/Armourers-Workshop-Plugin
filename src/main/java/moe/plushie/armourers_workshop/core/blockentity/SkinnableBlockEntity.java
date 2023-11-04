package moe.plushie.armourers_workshop.core.blockentity;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.BlockEntity;
import net.cocoonmc.core.block.BlockEntityType;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.world.Level;

public class SkinnableBlockEntity extends BlockEntity {

    public SkinnableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return serializeNBT();
    }
}
