package moe.plushie.armourers_workshop.plugin.init;

import moe.plushie.armourers_workshop.plugin.api.MenuType;
import moe.plushie.armourers_workshop.plugin.core.menu.CreativeSkinLibraryMenu;
import moe.plushie.armourers_workshop.plugin.core.menu.DyeTableMenu;
import moe.plushie.armourers_workshop.plugin.core.menu.SkinLibraryMenu;
import moe.plushie.armourers_workshop.plugin.core.menu.SkinWardrobeMenu;
import moe.plushie.armourers_workshop.plugin.core.menu.SkinWardrobeOpMenu;

@SuppressWarnings("unused")
public class ModMenuTypes {

    public static final MenuType<SkinWardrobeMenu> WARDROBE = normal(SkinWardrobeMenu::new).build("wardrobe");
    public static final MenuType<SkinWardrobeOpMenu> WARDROBE_OP = normal(SkinWardrobeOpMenu::new).build("wardrobe-op");

    public static final MenuType<DyeTableMenu> DYE_TABLE = normal(DyeTableMenu::new).build("dye-table");
    //public static final MenuType<SkinningTableMenu> SKINNING_TABLE = normal(SkinningTableMenu::new, ModBlocks.SKINNING_TABLE).bind(() -> SkinningTableWindow::new).build("skinning-table");

    public static final MenuType<CreativeSkinLibraryMenu> SKIN_LIBRARY_CREATIVE = normal(CreativeSkinLibraryMenu::new).build("skin-library-creative");
    public static final MenuType<SkinLibraryMenu> SKIN_LIBRARY = normal(SkinLibraryMenu::new).build("skin-library");
    //public static final MenuType<DyeTableMenu> SKIN_LIBRARY_GLOBAL = normal(GlobalSkinLibraryMenu::new).bind(() -> GlobalSkinLibraryWindow::new).build("skin-library-global");

    //public static final MenuType<HologramProjectorMenu> HOLOGRAM_PROJECTOR = block(HologramProjectorMenu::new, ModBlocks.HOLOGRAM_PROJECTOR).bind(() -> HologramProjectorWindow::new).build("hologram-projector");
    //public static final MenuType<ColorMixerMenu> COLOR_MIXER = block(ColorMixerMenu::new, ModBlocks.COLOR_MIXER).bind(() -> ColorMixerWindow::new).build("colour-mixer");
    //public static final MenuType<ArmourerMenu> ARMOURER = block(ArmourerMenu::new, ModBlocks.ARMOURER).bind(() -> ArmourerWindow::new).build("armourer");
    //public static final MenuType<OutfitMakerMenu> OUTFIT_MAKER = block(OutfitMakerMenu::new, ModBlocks.OUTFIT_MAKER).bind(() -> OutfitMakerWindow::new).build("outfit-maker");
    //public static final MenuType<AdvancedSkinBuilderMenu> ADVANCED_SKIN_BUILDER = block(AdvancedSkinBuilderMenu::new, ModBlocks.ADVANCED_SKIN_BUILDER).bind(() -> AdvancedSkinBuilderWindow::new).build("advanced-skin-builder");


    public static void init() {
    }

    private static <M, T> Builder<M, T> normal(MenuType.Factory<M, T> factory) {
        return new Builder<>(factory);
    }

    public static class Builder<M, T> {

        private final MenuType.Factory<M, T> factory;

        public Builder(MenuType.Factory<M, T> factory) {
            this.factory = factory;
        }

        public MenuType<M> build(String registryName) {
            return new MenuType<>("armourers_workshop:" + registryName, factory);
        }
    }

}

