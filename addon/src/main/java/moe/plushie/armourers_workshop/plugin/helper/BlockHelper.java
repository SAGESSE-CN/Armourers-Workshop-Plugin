package moe.plushie.armourers_workshop.plugin.helper;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Base64;
import java.util.HashMap;
import java.util.Optional;

public class BlockHelper {

    private static final HashMap<String, Block> ID_TO_BLOCKS = new HashMap<>();


    public static boolean isPlayerHead(BlockState state) {
        return state.is(Blocks.PLAYER_HEAD) || state.is(Blocks.PLAYER_WALL_HEAD);
    }

    public static Pair<BlockState, CompoundTag> getBlockFromTexture(String texture) {
        CompoundTag blockTag = getBlockTagFromTexture(texture);
        if (blockTag == null || blockTag.isEmpty()) {
            return null;
        }
        BlockState state = getBlockState(blockTag.getString("id"), blockTag.getCompound("state"));
        if (state == null) {
            return null;
        }
        CompoundTag tag = null;
        if (blockTag.contains("tag", 10)) {
            tag = blockTag.getCompound("tag");
        }
        return Pair.of(state, tag);
    }

    private static CompoundTag getBlockTagFromTexture(String texture) {
        // base64("{textures:{SKIN:{\"url":\"\",__redirected_block_")
        if (texture == null || texture.isEmpty() || !texture.startsWith("e3RleHR1cmVzOntTS0lOOnsidXJsIjoiIixfX3JlZGlyZWN0ZWRfYmxvY2tfX")) {
            return null;
        }
        try {
            String header = "{textures:{SKIN:{\"url\":\"\",__redirected_block__:";
            String footer = ",__block_redirected__:\"\"}}}";
            String tag = new String(Base64.getDecoder().decode(texture));
            if (tag.startsWith(header) && tag.endsWith(footer)) {
                String tag2 = tag.substring(header.length(), tag.length() - footer.length());
                return TagParser.parseTag(tag2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Block getBlock(String id) {
        return ID_TO_BLOCKS.computeIfAbsent(id, it -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation(id)));
    }

    private static BlockPos getBlockPos(CompoundTag arg) {
        return new BlockPos(arg.getInt("x"), arg.getInt("y"), arg.getInt("z"));
    }

    private static BlockState getBlockState(String id, CompoundTag prop) {
        Block block = getBlock(id);
        if (block == null) {
            return null;
        }
        StateDefinition<Block, BlockState> definition = block.getStateDefinition();
        BlockState state = block.defaultBlockState();
        for (String key : prop.getAllKeys()) {
            Property<?> property = definition.getProperty(key);
            if (property != null) {
                state = setBlockStateValue(state, property, prop.getString(key));
            }
        }
        return state;
    }

    private static <T extends Comparable<T>> BlockState setBlockStateValue(BlockState blockState, Property<T> arg, String string) {
        Optional<T> optional = arg.getValue(string);
        return optional.map(t -> blockState.setValue(arg, t)).orElse(blockState);
    }
}
