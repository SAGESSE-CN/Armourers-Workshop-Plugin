package moe.plushie.armourers_workshop.library.menu;

import moe.plushie.armourers_workshop.api.WorldAccessor;
import net.cocoonmc.core.inventory.MenuType;
import net.cocoonmc.core.world.entity.Player;

public class CreativeSkinLibraryMenu extends SkinLibraryMenu {

    public CreativeSkinLibraryMenu(MenuType<?> menuType, Player player, WorldAccessor accessor) {
        super(menuType, player, accessor);
    }

    @Override
    public boolean shouldLoadStack() {
        return outputSlots.get(0).getItem().isEmpty();
    }
}
