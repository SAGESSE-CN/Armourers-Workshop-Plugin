package moe.plushie.armourers_workshop.core.menu;

import moe.plushie.armourers_workshop.core.skin.SkinWardrobe;
import net.cocoonmc.core.inventory.MenuType;
import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.core.world.entity.Player;

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
