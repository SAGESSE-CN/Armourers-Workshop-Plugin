package moe.plushie.armourers_workshop.plugin.api;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class Item {

    private static final ImmutableMap<String, Material> MATERIALS = Maps.uniqueIndex(Arrays.stream(Material.values()).iterator(), it -> it.getKey().toString());
    private static final ImmutableMap<String, Integer> MAX_STACK_SIZES = ImmutableMap.<String, Integer>builder()
            .put("armourers_workshop:skin", 1)
            .build();

    @Nullable
    public static Material getMaterialById(String id) {
        return MATERIALS.get(id);
    }

    public static int getMaxStackSizeById(String id) {
        Material material = getMaterialById(id);
        if (material != null) {
            return material.getMaxStackSize();
        }
        return MAX_STACK_SIZES.getOrDefault(id, 64);
    }

}
