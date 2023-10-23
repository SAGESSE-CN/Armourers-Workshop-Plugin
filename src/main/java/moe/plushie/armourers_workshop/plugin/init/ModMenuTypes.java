package moe.plushie.armourers_workshop.plugin.init;

import moe.plushie.armourers_workshop.plugin.api.MenuType;
import moe.plushie.armourers_workshop.plugin.core.menu.SkinWardrobeMenu;
import moe.plushie.armourers_workshop.plugin.core.menu.SkinWardrobeOpMenu;

@SuppressWarnings("unused")
public class ModMenuTypes {

    public static final MenuType<SkinWardrobeMenu> WARDROBE = normal("wardrobe", SkinWardrobeMenu::new);
    public static final MenuType<SkinWardrobeOpMenu> WARDROBE_OP = normal("wardrobe-op", SkinWardrobeOpMenu::new);

    public static void init() {
    }

    private static <M, T> MenuType<M> normal(String registryName,  MenuType.Factory<M, T> factory) {
        return new MenuType<>("armourers_workshop:" + registryName, factory);
    }
}
