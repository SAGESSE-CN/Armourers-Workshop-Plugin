package moe.plushie.armourers_workshop.plugin.core.menu;

import moe.plushie.armourers_workshop.plugin.core.skin.SkinWardrobe;
import org.bukkit.entity.Player;

public class SkinWardrobeOpMenu extends SkinWardrobeMenu {

    public SkinWardrobeOpMenu(SkinWardrobe wardrobe, Player player) {
        super("armourers_workshop:wardrobe-op", wardrobe, player);
    }

//    @Override
//    public boolean stillValid(Player player) {
//        // in op mode, we have access wardrobe anytime anywhere.
//        Entity entity = getEntity();
//        return entity != null && entity.isAlive();
//    }
}
