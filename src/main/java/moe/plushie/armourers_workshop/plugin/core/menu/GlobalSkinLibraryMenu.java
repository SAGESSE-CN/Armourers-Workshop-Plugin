package moe.plushie.armourers_workshop.plugin.core.menu;

import moe.plushie.armourers_workshop.plugin.api.WorldAccessor;
import net.cocoonmc.core.inventory.MenuType;
import org.bukkit.entity.Player;

public class GlobalSkinLibraryMenu extends BlockContainerMenu {

    public GlobalSkinLibraryMenu(MenuType<?> menuType, Player player, WorldAccessor accessor) {
        super(menuType, player, accessor);
    }

    @Override
    public int getContainerSize() {
        return 2;
    }
}
