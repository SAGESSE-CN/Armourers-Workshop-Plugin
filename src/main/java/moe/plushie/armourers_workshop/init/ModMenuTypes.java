package moe.plushie.armourers_workshop.init;

import moe.plushie.armourers_workshop.builder.menu.ColorMixerMenu;
import moe.plushie.armourers_workshop.library.menu.CreativeSkinLibraryMenu;
import moe.plushie.armourers_workshop.core.menu.DyeTableMenu;
import moe.plushie.armourers_workshop.library.menu.GlobalSkinLibraryMenu;
import moe.plushie.armourers_workshop.core.menu.HologramProjectorMenu;
import moe.plushie.armourers_workshop.library.menu.SkinLibraryMenu;
import moe.plushie.armourers_workshop.core.menu.SkinWardrobeMenu;
import moe.plushie.armourers_workshop.core.menu.SkinWardrobeOpMenu;
import moe.plushie.armourers_workshop.core.menu.SkinningTableMenu;
import net.cocoonmc.core.inventory.Menu;
import net.cocoonmc.core.inventory.MenuType;
import net.cocoonmc.core.resources.ResourceLocation;

@SuppressWarnings("unused")
public class ModMenuTypes {

    public static final MenuType<SkinWardrobeMenu> WARDROBE = normal(SkinWardrobeMenu::new).build("wardrobe");
    public static final MenuType<SkinWardrobeOpMenu> WARDROBE_OP = normal(SkinWardrobeOpMenu::new).build("wardrobe-op");

    public static final MenuType<DyeTableMenu> DYE_TABLE = normal(DyeTableMenu::new).build("dye-table");
    public static final MenuType<SkinningTableMenu> SKINNING_TABLE = normal(SkinningTableMenu::new).build("skinning-table");

    public static final MenuType<CreativeSkinLibraryMenu> SKIN_LIBRARY_CREATIVE = normal(CreativeSkinLibraryMenu::new).build("skin-library-creative");
    public static final MenuType<SkinLibraryMenu> SKIN_LIBRARY = normal(SkinLibraryMenu::new).build("skin-library");
    public static final MenuType<GlobalSkinLibraryMenu> SKIN_LIBRARY_GLOBAL = normal(GlobalSkinLibraryMenu::new).build("skin-library-global");

    public static final MenuType<HologramProjectorMenu> HOLOGRAM_PROJECTOR = normal(HologramProjectorMenu::new).build("hologram-projector");
    public static final MenuType<ColorMixerMenu> COLOR_MIXER = normal(ColorMixerMenu::new).build("colour-mixer");
    //public static final MenuType<ArmourerMenu> ARMOURER = block(ArmourerMenu::new, ModBlocks.ARMOURER).bind(() -> ArmourerWindow::new).build("armourer");
    //public static final MenuType<OutfitMakerMenu> OUTFIT_MAKER = block(OutfitMakerMenu::new, ModBlocks.OUTFIT_MAKER).bind(() -> OutfitMakerWindow::new).build("outfit-maker");
    //public static final MenuType<AdvancedSkinBuilderMenu> ADVANCED_SKIN_BUILDER = block(AdvancedSkinBuilderMenu::new, ModBlocks.ADVANCED_SKIN_BUILDER).bind(() -> AdvancedSkinBuilderWindow::new).build("advanced-skin-builder");


    public static void init() {
    }

    private static <M extends Menu, T> Builder<M, T> normal(MenuType.Factory<M, T> factory) {
        return new Builder<>(factory);
    }

    public static class Builder<M extends Menu, T> {

        private final MenuType.Factory<M, T> factory;

        public Builder(MenuType.Factory<M, T> factory) {
            this.factory = factory;
        }

        public MenuType<M> build(String name) {
            ResourceLocation registryName = new ResourceLocation("armourers_workshop", name);
            return MenuType.register(registryName, new MenuType<>(factory));
        }
    }

}

