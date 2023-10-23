package moe.plushie.armourers_workshop.plugin.core.menu;

import moe.plushie.armourers_workshop.plugin.api.MenuType;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinWardrobe;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class SkinWardrobeOpMenu extends SkinWardrobeMenu {

    public SkinWardrobeOpMenu(MenuType<?> menuType, Player player, SkinWardrobe wardrobe) {
        super(menuType, player, wardrobe);
    }

    @Override
    public boolean stillValid(Player player) {
        // in op mode, we have access wardrobe anytime anywhere.
        Entity entity = getEntity();
        return entity != null && entity.isValid();
    }
}
