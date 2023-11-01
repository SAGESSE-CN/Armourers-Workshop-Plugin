package moe.plushie.armourers_workshop.library.blockentity;

import moe.plushie.armourers_workshop.core.blockentity.UpdatableContainerBlockEntity;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.utils.ContainerHelper;
import net.cocoonmc.core.utils.NonNullList;
import net.cocoonmc.core.world.Level;

public class SkinLibraryBlockEntity extends UpdatableContainerBlockEntity {

    private NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);

    public SkinLibraryBlockEntity(Level level, BlockPos pos, BlockState blockState) {
        super(level, pos, blockState);
    }

    @Override
    public void readFromNBT(CompoundTag nbt) {
        ContainerHelper.loadAllItems(nbt, items);
    }

    @Override
    public void writeToNBT(CompoundTag nbt) {
        ContainerHelper.saveAllItems(nbt, items);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }
}
