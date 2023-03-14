package moe.plushie.armourers_workshop.plugin.api;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.function.Function;

public class Item {

    private static final HashMap<Material, String> MATERIAL_TO_IDS = _k2v(Material.values(), it -> it.getKey().toString());
    private static final HashMap<String, Material> ID_TO_MATERIALS = _v2k(Material.values(), it -> it.getKey().toString());

    private static final ImmutableMap<String, Integer> MAX_STACK_SIZES = ImmutableMap.<String, Integer>builder()
            .put("armourers_workshop:skin", 1)
            .build();

    @Nullable
    public static String getIdByMaterial(Material material) {
        return MATERIAL_TO_IDS.get(material);
    }

    @Nullable
    public static Material getMaterialById(String id) {
        return ID_TO_MATERIALS.get(id);
    }

    public static int getMaxStackSizeById(String id) {
        Material material = getMaterialById(id);
        if (material != null) {
            return material.getMaxStackSize();
        }
        return MAX_STACK_SIZES.getOrDefault(id, 64);
    }

    public static String matchIdBySize(int maxStackSize) {
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

    @Nullable
    public static String getRealId(String id) {
        return _splitId(id, 0);
    }

    @Nullable
    public static String getWrapperId(String id) {
        return _splitId(id, 1);
    }

    private static <K, V> HashMap<K, V> _k2v(K[] keys, Function<K, V> transform) {
        HashMap<K, V> map = new HashMap<>();
        for (K key : keys) {
            map.put(key, transform.apply(key));
        }
        return map;
    }

    private static <K, V> HashMap<K, V> _v2k(V[] values, Function<V, K> transform) {
        HashMap<K, V> map = new HashMap<>();
        for (V value : values) {
            map.put(transform.apply(value), value);
        }
        return map;
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
}
