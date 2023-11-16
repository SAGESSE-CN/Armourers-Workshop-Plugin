package moe.plushie.armourers_workshop.init;

import moe.plushie.armourers_workshop.api.registry.IRegistryKey;
import moe.plushie.armourers_workshop.builder.block.AdvancedSkinBuilderBlock;
import moe.plushie.armourers_workshop.builder.block.ArmourerBlock;
import moe.plushie.armourers_workshop.builder.block.BoundingBoxBlock;
import moe.plushie.armourers_workshop.builder.block.ColorMixerBlock;
import moe.plushie.armourers_workshop.builder.block.OutfitMakerBlock;
import moe.plushie.armourers_workshop.builder.block.SkinCubeBlock;
import moe.plushie.armourers_workshop.core.block.DyeTableBlock;
import moe.plushie.armourers_workshop.core.block.HologramProjectorBlock;
import moe.plushie.armourers_workshop.core.block.SkinnableBlock;
import moe.plushie.armourers_workshop.core.block.SkinningTableBlock;
import moe.plushie.armourers_workshop.library.block.GlobalSkinLibraryBlock;
import moe.plushie.armourers_workshop.library.block.SkinLibraryBlock;
import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.block.BlockStateProperties;
import net.cocoonmc.core.block.Blocks;
import net.cocoonmc.core.resources.ResourceLocation;

import java.util.function.Function;
import java.util.function.ToIntFunction;

public class ModBlocks {

    public static final IRegistryKey<Block> HOLOGRAM_PROJECTOR = normal(HologramProjectorBlock::new).lightLevel(lit(13)).noOcclusion().strength(5f, 1200f).build("hologram-projector");
    public static final IRegistryKey<Block> SKINNABLE = half(SkinnableBlock::new).lightLevel(lit(15)).noOcclusion().dynamicShape().build("skinnable");

    public static final IRegistryKey<Block> DYE_TABLE = half(DyeTableBlock::new).build("dye-table");
    public static final IRegistryKey<Block> SKINNING_TABLE = half(SkinningTableBlock::new).build("skinning-table");

    public static final IRegistryKey<Block> SKIN_LIBRARY_CREATIVE = half(SkinLibraryBlock::new).build("skin-library-creative");
    public static final IRegistryKey<Block> SKIN_LIBRARY = half(SkinLibraryBlock::new).build("skin-library");
    public static final IRegistryKey<Block> SKIN_LIBRARY_GLOBAL = half(GlobalSkinLibraryBlock::new).build("skin-library-global");

    public static final IRegistryKey<Block> OUTFIT_MAKER = half(OutfitMakerBlock::new).build("outfit-maker");
    public static final IRegistryKey<Block> COLOR_MIXER = normal(ColorMixerBlock::new).build("colour-mixer");
    public static final IRegistryKey<Block> ARMOURER = normal(ArmourerBlock::new).build("armourer");
    public static final IRegistryKey<Block> ADVANCED_SKIN_BUILDER = normal(AdvancedSkinBuilderBlock::new).build("advanced-skin-builder");

    public static final IRegistryKey<Block> SKIN_CUBE = half(SkinCubeBlock::new).build("skin-cube");
    public static final IRegistryKey<Block> SKIN_CUBE_GLASS = glass(SkinCubeBlock::new).build("skin-cube-glass");
    public static final IRegistryKey<Block> SKIN_CUBE_GLOWING = half(SkinCubeBlock::new).lightLevel(15).build("skin-cube-glowing");
    public static final IRegistryKey<Block> SKIN_CUBE_GLASS_GLOWING = glass(SkinCubeBlock::new).lightLevel(15).build("skin-cube-glass-glowing");

    public static final IRegistryKey<Block> BOUNDING_BOX = glass(BoundingBoxBlock::new).noDrops().noCollission().build("bounding-box");

    private static ToIntFunction<BlockState> lit(int level) {
        return state -> state.getValue(BlockStateProperties.LIT) ? level : 0;
    }

    private static BlockBuilder<Block> create(Function<Block.Properties, Block> supplier, Object material, Object materialColor) {
        BlockBuilder<Block> builder = new BlockBuilder<>(supplier);
        return builder.strength(1.5f, 6.f);
    }

    private static BlockBuilder<Block> normal(Function<Block.Properties, Block> supplier) {
        return create(supplier, "STONE", "NONE");
    }

    private static BlockBuilder<Block> half(Function<Block.Properties, Block> supplier) {
        return normal(supplier).noOcclusion();
    }

    private static BlockBuilder<Block> glass(Function<Block.Properties, Block> supplier) {
        return create(supplier, "GLASS", "NONE").noOcclusion();
    }

    public static void init() {
    }

    private static class BlockBuilder<T extends Block> {

        final Block.Properties properties;
        final Function<Block.Properties, T> factory;

        public BlockBuilder(Function<Block.Properties, T> factory) {
            this.factory = factory;
            this.properties = new Block.Properties();
            this.properties.delegate(Blocks.ANDESITE);
        }

        public BlockBuilder<T> lightLevel(int level) {
            return lightLevel(state -> level);
        }

        public BlockBuilder<T> lightLevel(ToIntFunction<BlockState> toIntFunction) {
            //this.properties.delegate(Blocks.REDSTONE_LAMP);
            return this;
        }

        public BlockBuilder<T> noDrops() {
            this.properties.noDrops();
            return this;
        }

        public BlockBuilder<T> noOcclusion() {
            this.properties.noOcclusion();
            this.properties.delegate(Blocks.GLASS);
            return this;
        }

        public BlockBuilder<T> noCollission() {
            this.properties.noCollission();
            return this;
        }

        public BlockBuilder<T> dynamicShape() {
            return this;
        }

        public BlockBuilder<T> strength(float f, float g) {
            return this;
        }

        public IRegistryKey<T> build(String name) {
            ResourceLocation registryName = ModConstants.key(name);
            T block = factory.apply(properties);
            ModLog.debug("Registering Block '{}'", registryName);
            Block.register(registryName, block);
            return new IRegistryKey<T>() {
                @Override
                public ResourceLocation getRegistryName() {
                    return block.getRegistryName();
                }

                @Override
                public T get() {
                    return block;
                }
            };
        }
    }
}
