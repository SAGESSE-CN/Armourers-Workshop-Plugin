package moe.plushie.armourers_workshop.plugin.core.menu;

import moe.plushie.armourers_workshop.plugin.api.WorldAccessor;
import moe.plushie.armourers_workshop.plugin.core.blockentity.HologramProjectorBlockEntity;
import net.cocoonmc.core.inventory.Container;
import net.cocoonmc.core.inventory.MenuType;
import org.bukkit.entity.Player;

public class HologramProjectorMenu extends BlockContainerMenu {

    private final HologramProjectorBlockEntity blockEntity;

    public HologramProjectorMenu(MenuType<?> menuType, Player player, WorldAccessor accessor) {
        super(menuType, player, accessor);
        this.blockEntity = (HologramProjectorBlockEntity) accessor.getBlockEntity();
        this.addInputSlot(getInventory(), 0, 0, 0);
    }

    @Override
    protected Container createInventoryContainer() {
        return blockEntity.getInventory();
    }

    @Override
    protected int getContainerSize() {
        return 1;
    }
}
