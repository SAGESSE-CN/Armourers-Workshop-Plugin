package moe.plushie.armourers_workshop.builder.blockentity;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.BlockEntity;
import net.cocoonmc.core.block.BlockEntityType;
import net.cocoonmc.core.block.BlockState;

public class ArmourerBlockEntity extends BlockEntity {

    public ArmourerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }
}
