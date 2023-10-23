package moe.plushie.armourers_workshop.plugin.core.menu;

import moe.plushie.armourers_workshop.plugin.api.MenuType;
import org.bukkit.entity.Player;

public class MenuManager {

    public static <T extends ContainerMenu, V> void openMenu(MenuType<T> menuType, Player player, V value) {
        T menu = menuType.createMenu(player, value);
        menu.open();
    }

}
