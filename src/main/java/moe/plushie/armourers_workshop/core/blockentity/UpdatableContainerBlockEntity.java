package moe.plushie.armourers_workshop.core.blockentity;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.BlockEntity;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.inventory.Container;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.math.Vector3f;
import net.cocoonmc.core.utils.ContainerHelper;
import net.cocoonmc.core.utils.NonNullList;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.Player;

import java.util.List;

public abstract class UpdatableContainerBlockEntity extends BlockEntity implements Container {

    public UpdatableContainerBlockEntity(Level level, BlockPos pos, BlockState blockState) {
        super(level, pos, blockState);
    }

    public static void dropContainerIfNeeded(Level level, BlockPos blockPos) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof UpdatableContainerBlockEntity) {
            List<ItemStack> items = ((UpdatableContainerBlockEntity) blockEntity).getItems();
            ContainerHelper.dropItems(items, level, Vector3f.of(blockPos));
        }
    }

    @Override
    public boolean isEmpty() {
        return getItems().stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int i) {
        return getItems().get(i);
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        ItemStack itemStack = ContainerHelper.removeItem(getItems(), i, j);
        if (!itemStack.isEmpty()) {
            setContainerChanged();
        }
        return itemStack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return ContainerHelper.takeItem(getItems(), i);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        getItems().set(i, itemStack);
        if (itemStack.getCount() > this.getMaxStackSize()) {
            itemStack.setCount(this.getMaxStackSize());
        }
        setContainerChanged();
    }

    @Override
    public boolean stillValid(Player player) {
//        if (getLevel() == null) {
//            return false;
//        }
        Level level = getLevel();
        BlockPos pos = getBlockPos();
//        BlockEntity blockEntity = getLevel().getBlockEntity(pos);
//        if (blockEntity != this) {
//            return false;
//        }
        return player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0;
    }

    @Override
    public void clearContent() {
        getItems().clear();
    }

    public void setContainerChanged() {
        setChanged();
    }

    @Override
    public int getContainerSize() {
        return getItems().size();
    }

    public abstract NonNullList<ItemStack> getItems();

    public Container getInventory() {
        return this;
    }
}
