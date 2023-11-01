package moe.plushie.armourers_workshop.library.menu;

import moe.plushie.armourers_workshop.api.WorldAccessor;
import moe.plushie.armourers_workshop.core.menu.BlockContainerMenu;
import net.cocoonmc.core.inventory.Container;
import net.cocoonmc.core.inventory.MenuType;
import net.cocoonmc.core.inventory.SimpleContainer;
import net.cocoonmc.core.world.entity.Player;

public class GlobalSkinLibraryMenu extends BlockContainerMenu {

    public GlobalSkinLibraryMenu(MenuType<?> menuType, Player player, WorldAccessor accessor) {
        super(menuType, player, accessor);
    }

    @Override
    protected Container createInventoryContainer() {
        return new SimpleContainer(2);
    }

    public void crafting() {
    }
}
