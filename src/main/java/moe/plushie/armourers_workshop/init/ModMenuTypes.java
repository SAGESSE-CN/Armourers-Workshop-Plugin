package moe.plushie.armourers_workshop.init;

import moe.plushie.armourers_workshop.api.WorldAccessor;
import moe.plushie.armourers_workshop.api.registry.IRegistryKey;
import moe.plushie.armourers_workshop.builder.menu.AdvancedSkinBuilderMenu;
import moe.plushie.armourers_workshop.builder.menu.ArmourerMenu;
import moe.plushie.armourers_workshop.builder.menu.ColorMixerMenu;
import moe.plushie.armourers_workshop.builder.menu.OutfitMakerMenu;
import moe.plushie.armourers_workshop.core.menu.DyeTableMenu;
import moe.plushie.armourers_workshop.core.menu.HologramProjectorMenu;
import moe.plushie.armourers_workshop.core.menu.SkinWardrobeMenu;
import moe.plushie.armourers_workshop.core.menu.SkinWardrobeOpMenu;
import moe.plushie.armourers_workshop.core.menu.SkinnableMenu;
import moe.plushie.armourers_workshop.core.menu.SkinningTableMenu;
import moe.plushie.armourers_workshop.library.menu.CreativeSkinLibraryMenu;
import moe.plushie.armourers_workshop.library.menu.GlobalSkinLibraryMenu;
import moe.plushie.armourers_workshop.library.menu.SkinLibraryMenu;
import moe.plushie.armourers_workshop.utils.DataSerializers;
import moe.plushie.armourers_workshop.utils.ObjectUtils;
import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.inventory.Menu;
import net.cocoonmc.core.inventory.MenuType;
import net.cocoonmc.core.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("unused")
public class ModMenuTypes {

    private static final ArrayList<IRegistryKey<MenuType<?>>> REGISTERED_MENU_TYPES = new ArrayList<>();

    public static final IRegistryKey<MenuType<SkinWardrobeMenu>> WARDROBE = normal(SkinWardrobeMenu::new, DataSerializers.ENTITY_WARDROBE).build("wardrobe");
    public static final IRegistryKey<MenuType<SkinWardrobeOpMenu>> WARDROBE_OP = normal(SkinWardrobeOpMenu::new, DataSerializers.ENTITY_WARDROBE).build("wardrobe-op");

    public static final IRegistryKey<MenuType<SkinnableMenu>> SKINNABLE = block(SkinnableMenu::new, ModBlocks.SKINNABLE).build("skinnable");

    public static final IRegistryKey<MenuType<DyeTableMenu>> DYE_TABLE = block(DyeTableMenu::new, ModBlocks.DYE_TABLE).build("dye-table");
    public static final IRegistryKey<MenuType<SkinningTableMenu>> SKINNING_TABLE = block(SkinningTableMenu::new, ModBlocks.SKINNING_TABLE).build("skinning-table");

    public static final IRegistryKey<MenuType<CreativeSkinLibraryMenu>> SKIN_LIBRARY_CREATIVE = block(CreativeSkinLibraryMenu::new, ModBlocks.SKIN_LIBRARY_CREATIVE).build("skin-library-creative");
    public static final IRegistryKey<MenuType<SkinLibraryMenu>> SKIN_LIBRARY = block(SkinLibraryMenu::new, ModBlocks.SKIN_LIBRARY).build("skin-library");
    public static final IRegistryKey<MenuType<GlobalSkinLibraryMenu>> SKIN_LIBRARY_GLOBAL = block(GlobalSkinLibraryMenu::new, ModBlocks.SKIN_LIBRARY_GLOBAL).build("skin-library-global");

    public static final IRegistryKey<MenuType<HologramProjectorMenu>> HOLOGRAM_PROJECTOR = block(HologramProjectorMenu::new, ModBlocks.HOLOGRAM_PROJECTOR).build("hologram-projector");
    public static final IRegistryKey<MenuType<ColorMixerMenu>> COLOR_MIXER = block(ColorMixerMenu::new, ModBlocks.COLOR_MIXER).build("colour-mixer");
    public static final IRegistryKey<MenuType<ArmourerMenu>> ARMOURER = block(ArmourerMenu::new, ModBlocks.ARMOURER).build("armourer");
    public static final IRegistryKey<MenuType<OutfitMakerMenu>> OUTFIT_MAKER = block(OutfitMakerMenu::new, ModBlocks.OUTFIT_MAKER).build("outfit-maker");
    public static final IRegistryKey<MenuType<AdvancedSkinBuilderMenu>> ADVANCED_SKIN_BUILDER = block(AdvancedSkinBuilderMenu::new, ModBlocks.ADVANCED_SKIN_BUILDER).build("advanced-skin-builder");

    public static Collection<IRegistryKey<MenuType<?>>> getEntities() {
        return REGISTERED_MENU_TYPES;
    }

    private static <T extends Menu, V> MenuTypeBuilder<T, V> normal(MenuType.Factory<T, V> factory, Object serializer) {
        return new MenuTypeBuilder<>(factory, serializer);
    }

    private static <T extends Menu> MenuTypeBuilder<T, WorldAccessor> block(MenuType.Factory<T, WorldAccessor> factory, IRegistryKey<Block> block) {
//        IMenuProvider<T, IContainerLevelAccess> factory1 = (menuType, containerId, inventory, object) -> factory.createMenu(menuType, block.get(), containerId, inventory, object);
//        return BuilderManager.getInstance().createMenuTypeBuilder(factory1, DataSerializers.WORLD_POS);
        return new MenuTypeBuilder<>(factory, null);
    }

    public static void init() {
    }

    private static class MenuTypeBuilder<M extends Menu, T> {

        private final MenuType.Factory<M, T> factory;

        public MenuTypeBuilder(MenuType.Factory<M, T> factory, Object serializer) {
            this.factory = factory;
        }

        public IRegistryKey<MenuType<M>> build(String name) {
            ResourceLocation registryName = ModConstants.key(name);
            MenuType<M> menuType = new MenuType<>(factory);
            ModLog.debug("Registering Menu '{}'", registryName);
            MenuType.register(registryName, menuType);
            IRegistryKey<MenuType<M>> registryKey = new IRegistryKey<MenuType<M>>() {
                @Override
                public ResourceLocation getRegistryName() {
                    return menuType.getRegistryName();
                }

                @Override
                public MenuType<M> get() {
                    return menuType;
                }
            };
            REGISTERED_MENU_TYPES.add(ObjectUtils.unsafeCast(registryKey));
            return registryKey;
        }
    }
}

