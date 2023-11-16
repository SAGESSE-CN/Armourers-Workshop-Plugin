package moe.plushie.armourers_workshop.init;

import moe.plushie.armourers_workshop.api.registry.IRegistryKey;
import moe.plushie.armourers_workshop.builder.blockentity.AdvancedSkinBuilderBlockEntity;
import moe.plushie.armourers_workshop.builder.blockentity.ArmourerBlockEntity;
import moe.plushie.armourers_workshop.builder.blockentity.BoundingBoxBlockEntity;
import moe.plushie.armourers_workshop.builder.blockentity.ColorMixerBlockEntity;
import moe.plushie.armourers_workshop.builder.blockentity.OutfitMakerBlockEntity;
import moe.plushie.armourers_workshop.builder.blockentity.SkinCubeBlockEntity;
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
public class ModBlockEntityTypes {

    public static final IRegistryKey<BlockEntityType<HologramProjectorBlockEntity>> HOLOGRAM_PROJECTOR = create(HologramProjectorBlockEntity::new).of(ModBlocks.HOLOGRAM_PROJECTOR).build(ModConstants.BLOCK_HOLOGRAM_PROJECTOR);
    public static final IRegistryKey<BlockEntityType<OutfitMakerBlockEntity>> OUTFIT_MAKER = create(OutfitMakerBlockEntity::new).of(ModBlocks.OUTFIT_MAKER).build(ModConstants.BLOCK_OUTFIT_MAKER);
    public static final IRegistryKey<BlockEntityType<DyeTableBlockEntity>> DYE_TABLE = create(DyeTableBlockEntity::new).of(ModBlocks.DYE_TABLE).build(ModConstants.BLOCK_DYE_TABLE);

    public static final IRegistryKey<BlockEntityType<ColorMixerBlockEntity>> COLOR_MIXER = create(ColorMixerBlockEntity::new).of(ModBlocks.COLOR_MIXER).build(ModConstants.BLOCK_COLOR_MIXER);
    public static final IRegistryKey<BlockEntityType<ArmourerBlockEntity>> ARMOURER = create(ArmourerBlockEntity::new).of(ModBlocks.ARMOURER).build(ModConstants.BLOCK_ARMOURER);
    public static final IRegistryKey<BlockEntityType<AdvancedSkinBuilderBlockEntity>> ADVANCED_SKIN_BUILDER = create(AdvancedSkinBuilderBlockEntity::new).of(ModBlocks.ADVANCED_SKIN_BUILDER).build(ModConstants.BLOCK_ADVANCED_SKIN_BUILDER);

    public static final IRegistryKey<BlockEntityType<SkinLibraryBlockEntity>> SKIN_LIBRARY = create(SkinLibraryBlockEntity::new).of(ModBlocks.SKIN_LIBRARY).build(ModConstants.BLOCK_SKIN_LIBRARY);
    public static final IRegistryKey<BlockEntityType<GlobalSkinLibraryBlockEntity>> SKIN_LIBRARY_GLOBAL = create(GlobalSkinLibraryBlockEntity::new).of(ModBlocks.SKIN_LIBRARY_GLOBAL).build(ModConstants.BLOCK_SKIN_LIBRARY_GLOBAL);

    public static final IRegistryKey<BlockEntityType<SkinnableBlockEntity>> SKINNABLE = create(SkinnableBlockEntity::new).of(ModBlocks.SKINNABLE).build(ModConstants.BLOCK_SKINNABLE);
    public static final IRegistryKey<BlockEntityType<BoundingBoxBlockEntity>> BOUNDING_BOX = create(BoundingBoxBlockEntity::new).of(ModBlocks.BOUNDING_BOX).build(ModConstants.BLOCK_BOUNDING_BOX);
    public static final IRegistryKey<BlockEntityType<SkinCubeBlockEntity>> SKIN_CUBE = create(SkinCubeBlockEntity::new).of(ModBlocks.SKIN_CUBE).of(ModBlocks.SKIN_CUBE_GLASS).of(ModBlocks.SKIN_CUBE_GLASS_GLOWING).of(ModBlocks.SKIN_CUBE_GLOWING).build(ModConstants.BLOCK_SKIN_CUBE);

    public static void init() {
    }

    private static <T extends BlockEntity> BlockEntityTypeBuilder<T> create(BlockEntityType.Factory<T> factory) {
        return new BlockEntityTypeBuilder<>(factory);
    }

    private static class BlockEntityTypeBuilder<T extends BlockEntity> {

        private final HashSet<Block> validBlocks = new HashSet<>();
        private final BlockEntityType.Factory<T> factory;

        public BlockEntityTypeBuilder(BlockEntityType.Factory<T> factory) {
            this.factory = factory;
        }

        public BlockEntityTypeBuilder<T> of(IRegistryKey<Block> block) {
            this.validBlocks.add(block.get());
            return this;
        }

        public IRegistryKey<BlockEntityType<T>> build(String name) {
            ResourceLocation registryName = ModConstants.key(name);
            BlockEntityType<T> blockEntityType = new BlockEntityType<>(validBlocks, factory);
            ModLog.debug("Registering Block Entity '{}'", registryName);
            BlockEntityType.register(registryName, blockEntityType);
            return new IRegistryKey<BlockEntityType<T>>() {
                @Override
                public ResourceLocation getRegistryName() {
                    return blockEntityType.getRegistryName();
                }

                @Override
                public BlockEntityType<T> get() {
                    return blockEntityType;
                }
            };
        }
    }
}
