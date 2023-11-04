package moe.plushie.armourers_workshop.core.blockentity;

import moe.plushie.armourers_workshop.utils.OptionalCompoundTag;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.BlockEntity;
import net.cocoonmc.core.block.BlockEntityType;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.math.Vector3f;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.utils.ContainerHelper;
import net.cocoonmc.core.utils.NonNullList;
import net.cocoonmc.core.world.Level;

import java.util.Collections;
import java.util.List;

public class DyeTableBlockEntity extends UpdatableContainerBlockEntity {

    private final NonNullList<ItemStack> items = NonNullList.withSize(10, ItemStack.EMPTY);

    public DyeTableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        ContainerHelper.loadAllItems(tag, items);
    }

    @Override
    public void writeToNBT(CompoundTag tag) {
        ContainerHelper.saveAllItems(tag, items);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public List<ItemStack> getDropItems() {
        return Collections.singletonList(items.get(items.size() - 1));
    }
}
