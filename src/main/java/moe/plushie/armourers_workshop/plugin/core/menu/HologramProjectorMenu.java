package moe.plushie.armourers_workshop.plugin.core.menu;

import moe.plushie.armourers_workshop.plugin.api.WorldAccessor;
import net.cocoonmc.core.inventory.MenuType;
import org.bukkit.entity.Player;

public class HologramProjectorMenu extends BlockContainerMenu {

    public HologramProjectorMenu(MenuType<?> menuType, Player player, WorldAccessor accessor) {
        super(menuType, player, accessor);
    }

    @Override
    public int getSlotSize() {
        return 10;
    }
}
