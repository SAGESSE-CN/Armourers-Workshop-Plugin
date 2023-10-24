package moe.plushie.armourers_workshop.plugin.api;

import net.querz.nbt.io.SNBTUtil;
import net.querz.nbt.tag.CompoundTag;
import org.bukkit.Material;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.util.Base64;
import java.util.HashMap;
import java.util.function.Function;

public class Blocks {

    private static final HashMap<ResourceLocation, Material> ID_TO_MATERIALS = _v2k(Material.values(), it -> new ResourceLocation(it.getKey()));
    private static final HashMap<ResourceLocation, Block> BLOCKS = new HashMap<>();
    private static final HashMap<String, Block> BLOCKS_BK = new HashMap<>();

    public static Block AIR = byId("minecraft:air");

    public static Block byId(String id) {
        return BLOCKS_BK.computeIfAbsent(id, id1 -> byKey(new ResourceLocation(id1)));
    }

    public static Block byKey(ResourceLocation key) {
        return BLOCKS.computeIfAbsent(key, key1 -> {
            Block.Properties properties = new Block.Properties();
            Material material = ID_TO_MATERIALS.get(key1);
            if (material != null) {
                properties.material(material);
            }
            Block block = new Block(properties);
            block.setKey(key1);
            return block;
        });
    }

    public static void register(ResourceLocation key, Block block) {
        block.setKey(key);
        BLOCKS.put(key, block);
    }

    @Nullable
    public static BlockEntity loadFromTexture(World world, BlockPos pos, String texture) {
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
                return loadFromTag(world, pos, (CompoundTag) SNBTUtil.fromSNBT(tag2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static BlockEntity loadFromTag(World world, BlockPos pos, CompoundTag blockTag) {
        Block block = byId(blockTag.getString("id"));
        if (block.asMaterial() != null) {
            return null;
        }
        BlockState blockState = block.defaultBlockState().deserialize(blockTag.getCompoundTag("state"));
        CompoundTag tag = null;
        if (blockTag.containsKey("tag")) {
            tag = blockTag.getCompoundTag("tag");
        }
        return block.createBlockEntity(world, pos, blockState, tag);
    }

    private static <K, V> HashMap<K, V> _v2k(V[] values, Function<V, K> transform) {
        HashMap<K, V> map = new HashMap<>();
        for (V value : values) {
            map.put(transform.apply(value), value);
        }
        return map;
    }
}
