package moe.plushie.armourers_workshop.plugin.api;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Item {

    private NamespacedKey key;

    private final Properties properties;

    public Item(Properties properties) {
        this.properties = properties;
    }

    public InteractionResultHolder<ItemStack> use(ItemStack itemStack, Player player, EquipmentSlot hand) {
        // Pass to vanilla handler.
        if (properties.material != null) {
            return InteractionResultHolder.pass(itemStack);
        }
        return InteractionResultHolder.fail(itemStack);
    }

    public InteractionResult useOn(UseOnContext useOnContext) {
        // Pass to vanilla handler.
        if (properties.material != null) {
            return InteractionResult.PASS;
        }
        return InteractionResult.SUCCESS;
    }

    public InteractionResult attackLivingEntity(ItemStack itemStack, Player player, Entity entity) {
        // Pass to vanilla handler.
        if (properties.material != null) {
            return InteractionResult.PASS;
        }
        return InteractionResult.SUCCESS;
    }

    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity entity, EquipmentSlot hand) {
        // Pass to vanilla handler.
        if (properties.material != null) {
            return InteractionResult.PASS;
        }
        return InteractionResult.SUCCESS;
    }

    public int getMaxStackSize() {
        return properties.maxStackSize;
    }

    public void setKey(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }

    @Nullable
    public Material asMaterial() {
        return properties.material;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return Objects.equals(key, item.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    public static class Properties {

        int maxStackSize = 64;
        int maxDamage = 1;

        Material material;

        public Properties stacksTo(int i) {
            this.maxStackSize = i;
            return this;
        }

        public Properties durability(int i) {
            this.maxDamage = i;
            this.maxStackSize = 1;
            return this;
        }

        public Properties material(Material material) {
            this.material = material;
            if (material != null) {
                this.maxStackSize = material.getMaxStackSize();
                this.maxDamage = material.getMaxDurability();
            }
            return this;
        }
    }
}
