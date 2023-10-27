package moe.plushie.armourers_workshop.plugin.init.platform;

import moe.plushie.armourers_workshop.plugin.core.menu.ContainerMenu;
import net.cocoonmc.core.inventory.MenuType;
import net.cocoonmc.core.world.InteractionResult;
import org.bukkit.entity.Player;

public class MenuManager {

    public static <T extends ContainerMenu, V> InteractionResult openMenu(MenuType<T> menuType, Player player, V value) {
        T menu = menuType.createMenu(player, value);
        menu.openMenu();
        return InteractionResult.SUCCESS;
    }

}
