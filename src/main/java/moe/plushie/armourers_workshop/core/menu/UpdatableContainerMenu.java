package moe.plushie.armourers_workshop.core.menu;

import moe.plushie.armourers_workshop.api.WorldAccessor;
import moe.plushie.armourers_workshop.core.blockentity.UpdatableContainerBlockEntity;
import net.cocoonmc.core.inventory.Container;
import net.cocoonmc.core.inventory.MenuType;
import net.cocoonmc.core.world.entity.Player;

public class UpdatableContainerMenu<T extends UpdatableContainerBlockEntity> extends BlockContainerMenu {

    protected final T blockEntity;

    @SuppressWarnings("unchecked")
    public UpdatableContainerMenu(MenuType<?> menuType, Player player, WorldAccessor accessor) {
        super(menuType, player, accessor);
        this.blockEntity = (T) accessor.getBlockEntity();
    }

    @Override
    protected Container createInventoryContainer() {
        return blockEntity.getInventory();
    }
}
