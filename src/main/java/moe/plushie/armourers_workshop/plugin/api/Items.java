package moe.plushie.armourers_workshop.plugin.api;

import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.function.Function;

public class Items {

    private static final HashMap<ResourceLocation, Material> ID_TO_MATERIALS = _v2k(Material.values(), it -> new ResourceLocation(it.getKey()));
    private static final HashMap<ResourceLocation, Item> ITEMS = new HashMap<>();
    private static final HashMap<String, Item> ITEMS_BK = new HashMap<>();

    public static Item AIR = byId("minecraft:air");

    public static Item byId(String id) {
        return ITEMS_BK.computeIfAbsent(id, id1 -> byKey(new ResourceLocation(id1)));
    }

    public static Item byKey(ResourceLocation key) {
        return ITEMS.computeIfAbsent(key, key1 -> {
            Item.Properties properties = new Item.Properties();
            Material material = ID_TO_MATERIALS.get(key1);
            if (material != null) {
                properties.material(material);
            }
            Item item = new Item(properties);
            item.setKey(key1);
            return item;
        });
    }


    public static void register(String id, Item item) {
        ResourceLocation key = new ResourceLocation(id);
        item.setKey(key);
        ITEMS.put(key, item);
    }

    @Nullable
    public static String getRealId(String id) {
        return _splitId(id, 0);
    }

    @Nullable
    public static String getWrapperId(String id) {
        return _splitId(id, 1);
    }

    public static String getWrapperIdBySize(int maxStackSize) {
        switch (maxStackSize) {
            case 1: {
                return "minecraft:flower_banner_pattern";
            }
            case 16: {
                return "minecraft:white_banner";
            }
            default: {
                return "minecraft:paper";
            }
        }
    }

    private static String _splitId(String id, int index) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        String[] ids = id.split("/");
        if (index < ids.length) {
            return ids[index];
        }
        return null;
    }

    private static <K, V> HashMap<K, V> _v2k(V[] values, Function<V, K> transform) {
        HashMap<K, V> map = new HashMap<>();
        for (V value : values) {
            map.put(transform.apply(value), value);
        }
        return map;
    }
}
