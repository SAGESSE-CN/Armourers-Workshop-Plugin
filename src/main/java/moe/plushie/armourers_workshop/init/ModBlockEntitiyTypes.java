package moe.plushie.armourers_workshop.init;

import moe.plushie.armourers_workshop.builder.blockentity.ColorMixerBlockEntity;
import moe.plushie.armourers_workshop.core.blockentity.DyeTableBlockEntity;
import moe.plushie.armourers_workshop.core.blockentity.HologramProjectorBlockEntity;
import moe.plushie.armourers_workshop.core.blockentity.SkinnableBlockEntity;
import moe.plushie.armourers_workshop.library.blockentity.GlobalSkinLibraryBlockEntity;
import moe.plushie.armourers_workshop.library.blockentity.SkinLibraryBlockEntity;
import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.block.BlockEntity;
import net.cocoonmc.core.block.BlockEntityType;
import net.cocoonmc.core.resources.ResourceLocation;

import java.util.HashSet;

@SuppressWarnings("unused")
public class ModBlockEntitiyTypes {

    public static final BlockEntityType<HologramProjectorBlockEntity> HOLOGRAM_PROJECTOR = create(HologramProjectorBlockEntity::new).of(ModBlocks.HOLOGRAM_PROJECTOR).build("hologram-projector");
    //public static final BlockEntityType<OutfitMakerBlockEntity> OUTFIT_MAKER = create(OutfitMakerBlockEntity::new).of(ModBlocks.OUTFIT_MAKER).build("outfit-maker");
    public static final BlockEntityType<DyeTableBlockEntity> DYE_TABLE = create(DyeTableBlockEntity::new).of(ModBlocks.DYE_TABLE).build("dye-table");

    public static final BlockEntityType<ColorMixerBlockEntity> COLOR_MIXER = create(ColorMixerBlockEntity::new).of(ModBlocks.COLOR_MIXER).build("colour-mixer");
    //public static final BlockEntityType<ArmourerBlockEntity> ARMOURER = create(ArmourerBlockEntity::new).of(ModBlocks.ARMOURER).build("armourer");
    //public static final BlockEntityType<AdvancedSkinBuilderBlockEntity> ADVANCED_SKIN_BUILDER = create(AdvancedSkinBuilderBlockEntity::new).of(ModBlocks.ADVANCED_SKIN_BUILDER).build("advanced-skin-builder");

    public static final BlockEntityType<SkinLibraryBlockEntity> SKIN_LIBRARY = create(SkinLibraryBlockEntity::new).of(ModBlocks.SKIN_LIBRARY).build("skin-library");
    public static final BlockEntityType<GlobalSkinLibraryBlockEntity> SKIN_LIBRARY_GLOBAL = create(GlobalSkinLibraryBlockEntity::new).of(ModBlocks.SKIN_LIBRARY_GLOBAL).build("skin-library-global");

    public static final BlockEntityType<SkinnableBlockEntity> SKINNABLE = create(SkinnableBlockEntity::new).of(ModBlocks.SKINNABLE).build("skinnable");
    //public static final BlockEntityType<BoundingBoxBlockEntity> BOUNDING_BOX = create(BoundingBoxBlockEntity::new).of(ModBlocks.BOUNDING_BOX).build("bounding-box");
    //public static final BlockEntityType<SkinCubeBlockEntity> SKIN_CUBE = create(SkinCubeBlockEntity::new).of(ModBlocks.SKIN_CUBE).of(ModBlocks.SKIN_CUBE_GLASS).of(ModBlocks.SKIN_CUBE_GLASS_GLOWING).of(ModBlocks.SKIN_CUBE_GLOWING).build("skin-cube");

    public static void init() {
    }

    private static <T extends BlockEntity> Builder<T> create(BlockEntityType.Factory<T> factory) {
        return new Builder<>(factory);
    }

    public static class Builder<T extends BlockEntity> {

        private final HashSet<Block> validBlocks = new HashSet<>();
        private final BlockEntityType.Factory<T> factory;

        public Builder(BlockEntityType.Factory<T> factory) {
            this.factory = factory;
        }

        public Builder<T> of(Block block) {
            this.validBlocks.add(block);
            return this;
        }

        public BlockEntityType<T> build(String name) {
            ResourceLocation registryName = ModConstants.key(name);
            return BlockEntityType.register(registryName, new BlockEntityType<>(validBlocks, factory));
        }
    }
}
