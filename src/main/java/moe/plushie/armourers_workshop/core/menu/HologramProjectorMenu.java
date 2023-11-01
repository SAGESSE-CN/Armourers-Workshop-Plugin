package moe.plushie.armourers_workshop.core.menu;

import moe.plushie.armourers_workshop.api.WorldAccessor;
import moe.plushie.armourers_workshop.core.blockentity.HologramProjectorBlockEntity;
import net.cocoonmc.core.inventory.MenuType;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.world.entity.Player;

public class HologramProjectorMenu extends UpdatableContainerMenu<HologramProjectorBlockEntity> {


    public HologramProjectorMenu(MenuType<?> menuType, Player player, WorldAccessor accessor) {
        super(menuType, player, accessor);
        this.addInputSlot(getInventory(), 0, 0, 0);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return quickMoveStack(player, index, slots.size());
    }
}
