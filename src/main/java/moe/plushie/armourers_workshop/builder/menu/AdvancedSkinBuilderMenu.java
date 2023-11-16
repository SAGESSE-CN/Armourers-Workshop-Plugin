package moe.plushie.armourers_workshop.builder.menu;

import moe.plushie.armourers_workshop.api.WorldAccessor;
import moe.plushie.armourers_workshop.core.menu.BlockContainerMenu;
import net.cocoonmc.core.inventory.Container;
import net.cocoonmc.core.inventory.MenuType;
import net.cocoonmc.core.inventory.SimpleContainer;
import net.cocoonmc.core.world.entity.Player;

public class AdvancedSkinBuilderMenu extends BlockContainerMenu {

    public AdvancedSkinBuilderMenu(MenuType<?> menuType, Player player, WorldAccessor accessor) {
        super(menuType, player, accessor);
    }

    @Override
    protected Container createInventoryContainer() {
        return new SimpleContainer(2);
    }
}
