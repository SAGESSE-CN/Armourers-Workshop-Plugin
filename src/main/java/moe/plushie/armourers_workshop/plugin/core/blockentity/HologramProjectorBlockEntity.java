package moe.plushie.armourers_workshop.plugin.core.blockentity;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.inventory.SimpleContainer;
import org.bukkit.World;

public class HologramProjectorBlockEntity extends UpdatableContainerBlockEntity {

    private final SimpleContainer inventory = new SimpleContainer(1);

    public HologramProjectorBlockEntity(World world, BlockPos pos, BlockState blockState) {
        super(world, pos, blockState);
    }

    public SimpleContainer getInventory() {
        return inventory;
    }
}
