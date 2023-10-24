package moe.plushie.armourers_workshop.plugin.init.platform;

import moe.plushie.armourers_workshop.plugin.api.InteractionResult;
import moe.plushie.armourers_workshop.plugin.api.MenuType;
import moe.plushie.armourers_workshop.plugin.core.menu.ContainerMenu;
import org.bukkit.entity.Player;

public class MenuManager {

    public static <T extends ContainerMenu, V> InteractionResult openMenu(MenuType<T> menuType, Player player, V value) {
        T menu = menuType.createMenu(player, value);
        menu.open();
        return InteractionResult.SUCCESS;
    }

}
