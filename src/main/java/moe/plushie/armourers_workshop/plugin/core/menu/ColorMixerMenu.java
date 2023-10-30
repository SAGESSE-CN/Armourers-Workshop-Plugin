package moe.plushie.armourers_workshop.plugin.core.menu;

import moe.plushie.armourers_workshop.plugin.api.WorldAccessor;
import moe.plushie.armourers_workshop.plugin.core.recipe.SkinningRecipes;
import net.cocoonmc.core.inventory.MenuType;
import net.cocoonmc.core.inventory.Slot;
import net.cocoonmc.core.item.ItemStack;
import org.bukkit.entity.Player;

public class ColorMixerMenu extends BlockContainerMenu {

    public ColorMixerMenu(MenuType<?> menuType, Player player, WorldAccessor accessor) {
        super(menuType, player, accessor);
    }

    @Override
    public int getContainerSize() {
        return 2;
    }
}
