package moe.plushie.armourers_workshop.init;

import moe.plushie.armourers_workshop.builder.block.ColorMixerBlock;
import moe.plushie.armourers_workshop.core.block.DyeTableBlock;
import moe.plushie.armourers_workshop.library.block.GlobalSkinLibraryBlock;
import moe.plushie.armourers_workshop.core.block.HologramProjectorBlock;
import moe.plushie.armourers_workshop.library.block.SkinLibraryBlock;
import moe.plushie.armourers_workshop.core.block.SkinnableBlock;
import moe.plushie.armourers_workshop.core.block.SkinningTableBlock;
import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.block.BlockEntityType;
import net.cocoonmc.core.block.Blocks;
import net.cocoonmc.core.resources.ResourceLocation;

import java.util.function.Function;

public class ModBlocks {

    public static final Block HOLOGRAM_PROJECTOR = normal(HologramProjectorBlock::new).lightLevel(13).strength(5f, 1200f).build("hologram-projector");
    public static final Block SKINNABLE = normal(SkinnableBlock::new).lightLevel(15).noOcclusion().build("skinnable");

    public static final Block DYE_TABLE = normal(DyeTableBlock::new).noOcclusion().build("dye-table");
    public static final Block SKINNING_TABLE = normal(SkinningTableBlock::new).noOcclusion().build("skinning-table");

    public static final Block SKIN_LIBRARY_CREATIVE = normal(SkinLibraryBlock::new).noOcclusion().build("skin-library-creative");
    public static final Block SKIN_LIBRARY = normal(SkinLibraryBlock::new).noOcclusion().build("skin-library");
    public static final Block SKIN_LIBRARY_GLOBAL = normal(GlobalSkinLibraryBlock::new).noOcclusion().build("skin-library-global");

    //public static final Block OUTFIT_MAKER = normal(ColorMixerBlock::new).noOcclusion().build("outfit-maker");
    public static final Block COLOR_MIXER = normal(ColorMixerBlock::new).build("colour-mixer");
    //public static final Block ARMOURER = normal(HorizontalDirectionalBlock::new).build("armourer");
    //public static final Block ADVANCED_SKIN_BUILDER = normal(HorizontalDirectionalBlock::new).build("advanced-skin-builder");

    //public static final Block SKIN_CUBE = normal(HorizontalDirectionalBlock::new).build("skin-cube");
    //public static final Block SKIN_CUBE_GLASS = normal(HorizontalDirectionalBlock::new).build("skin-cube-glass");
    //public static final Block SKIN_CUBE_GLOWING = normal(HorizontalDirectionalBlock::new).lightLevel(15).build("skin-cube-glowing");
    //public static final Block SKIN_CUBE_GLASS_GLOWING = normal(HorizontalDirectionalBlock::new).lightLevel(15).build("skin-cube-glass-glowing");

    //public static final Block BOUNDING_BOX = normal(Block::new).noDrops().noCollission().build("bounding-box");

    public static void init() {
    }

    private static Builder normal(Function<Block.Properties, Block> factory) {
        return new Builder(factory).isInteractable();
    }

    private static class Builder {

        final Block.Properties properties;
        final Function<Block.Properties, Block> factory;

        public Builder(Function<Block.Properties, Block> factory) {
            this.factory = factory;
            this.properties = new Block.Properties();
            this.properties.delegate(Blocks.ANDESITE);
        }

        public Builder isInteractable() {
            this.properties.isInteractable();
            return this;
        }

        public Builder lightLevel(int level) {
            //this.properties.delegate(Blocks.REDSTONE_LAMP);
            return this;
        }

        public Builder noDrops() {
            this.properties.noDrops();
            return this;
        }

        public Builder noOcclusion() {
            this.properties.noOcclusion();
            this.properties.delegate(Blocks.GLASS);
            return this;
        }

        public Builder noCollission() {
            this.properties.noCollission();
            return this;
        }

        public Builder strength(float f, float g) {
            return this;
        }

        public Block build(String name) {
            ResourceLocation key = new ResourceLocation("armourers_workshop", name);
            Block block = factory.apply(properties);
            ModLog.debug("Registering Block '{}'", key);
            return Block.register(key, block);
        }
    }
}
