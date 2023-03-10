package moe.plushie.armourers_workshop.plugin.utils;

import moe.plushie.armourers_workshop.plugin.api.ItemStack;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinDescriptor;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinWardrobe;
import org.bukkit.entity.Entity;

import java.util.WeakHashMap;
import java.util.function.Function;

public class FastCache<K, V> {

    public static final FastCache<Entity, SkinWardrobe> ENTITY_TO_SKIN_WARDROBE = new FastCache<>();
    public static final FastCache<ItemStack, SkinDescriptor> ITEM_TO_SKIN_DESCRIPTOR = new FastCache<>();

    public static final FastCache<ItemStack, org.bukkit.inventory.ItemStack> ITEM_TO_BUKKIT_ITEM = new FastCache<>();
    public static final FastCache<org.bukkit.inventory.ItemStack, ItemStack> BUKKIT_ITEM_TO_ITEM = new FastCache<>();


    private final WeakHashMap<K, V> values = new WeakHashMap<>();


    public V get(K key) {
        return values.get(key);
    }

    public V getOrDefault(K key, V defaultValue) {
        return values.getOrDefault(key, defaultValue);
    }

    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return values.computeIfAbsent(key, mappingFunction);
    }

}
