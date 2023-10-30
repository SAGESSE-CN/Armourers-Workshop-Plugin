package moe.plushie.armourers_workshop.plugin.core.blockentity;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.BlockState;
import org.bukkit.World;

public class SkinLibraryBlockEntity extends UpdatableContainerBlockEntity {

    public SkinLibraryBlockEntity(World world, BlockPos pos, BlockState blockState) {
        super(world, pos, blockState);
    }
}
