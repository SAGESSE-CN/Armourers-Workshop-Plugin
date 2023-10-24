package moe.plushie.armourers_workshop.plugin.api;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Item {

    private ResourceLocation key;

    private final Properties properties;

    public Item(Properties properties) {
        this.properties = properties;
    }

    public InteractionResultHolder<ItemStack> use(ItemStack itemStack, Player player, EquipmentSlot hand) {
        return InteractionResultHolder.pass(itemStack);
    }

    /**
     * This is called when the item is used, before the block is activated.
     *
     * @return Return PASS to allow vanilla handling, any other to skip normal code.
     */
    public InteractionResult useOnFirst(ItemStack stack, UseOnContext context) {
        return InteractionResult.PASS;
    }

    public InteractionResult useOn(UseOnContext context) {
        return InteractionResult.PASS;
    }

    /**
     * Called when the player Left Clicks (attacks) an entity. Processed before
     * damage is done, if return value is true further processing is canceled and
     * the entity is not attacked.
     *
     * @param stack  The Item being used
     * @param player The player that is attacking
     * @param entity The entity being attacked
     * @return True to cancel the rest of the interaction.
     */
    public InteractionResult attackLivingEntity(ItemStack stack, Player player, Entity entity) {
        return InteractionResult.PASS;
    }

    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity entity, EquipmentSlot hand) {
        return InteractionResult.PASS;
    }

    /**
     * Should this item, when held, allow sneak-clicks to pass through to the
     * underlying block?
     *
     * @param stack  The Item being used
     * @param player The Player that is wielding the item
     * @param world  The world
     * @param pos    Block position in level
     */
    public boolean doesSneakBypassUse(ItemStack stack, Player player, World world, BlockPos pos) {
        return false;
    }

    public int getMaxStackSize() {
        return properties.maxStackSize;
    }

    public void setKey(ResourceLocation key) {
        this.key = key;
    }

    public ResourceLocation getKey() {
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
