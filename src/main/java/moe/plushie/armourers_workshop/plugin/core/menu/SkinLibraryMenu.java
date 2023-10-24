package moe.plushie.armourers_workshop.plugin.core.menu;

import moe.plushie.armourers_workshop.plugin.api.MenuType;
import moe.plushie.armourers_workshop.plugin.api.WorldAccessor;
import org.bukkit.entity.Player;

public class SkinLibraryMenu extends BlockContainerMenu {

    public SkinLibraryMenu(MenuType<?> menuType, Player player, WorldAccessor accessor) {
        super(menuType, player, accessor);
    }

    @Override
    public int getSlotSize() {
        return 2;
    }
}
