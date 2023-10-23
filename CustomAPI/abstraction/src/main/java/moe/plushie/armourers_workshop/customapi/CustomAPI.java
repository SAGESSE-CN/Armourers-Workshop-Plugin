package moe.plushie.armourers_workshop.customapi;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.Map;
import java.util.function.Function;

public interface CustomAPI {

    CustomSlotImpl createCustomSlot(CustomSlotProvider impl, Inventory inventory, int index, int x, int y);

    CustomMenuImpl createCustomMenu(CustomMenuProvider impl, Player player, InventoryHolder holder, int size, String title);

    CustomBlockImpl createCustomBlock();

    
    class Proxy<T> extends ItemStack {

        final T value;
        final ItemStack origin;

        Proxy(ItemStack origin, T value) {
            this.value = value;
            this.origin = origin;
        }

        public static <T> Proxy<T> of(T value, Function<T, ItemStack> provider) {
            return new Proxy<>(provider.apply(value), value);
        }

        public static <T> T of(ItemStack itemStack, Function<ItemStack, T> provider) {
            if (itemStack instanceof Proxy<?>) {
                return ((Proxy<T>) itemStack).value;
            }
            return provider.apply(itemStack);
        }

        @Override
        public Material getType() {
            return origin.getType();
        }

        @Override
        public void setType(Material type) {
            origin.setType(type);
        }

        @Override
        public int getAmount() {
            return origin.getAmount();
        }

        @Override
        public void setAmount(int amount) {
            origin.setAmount(amount);
        }

        @Override
        public MaterialData getData() {
            return origin.getData();
        }

        @Override
        public void setData(MaterialData data) {
            origin.setData(data);
        }

        @Override
        public void setDurability(short durability) {
            origin.setDurability(durability);
        }

        @Override
        public short getDurability() {
            return origin.getDurability();
        }

        @Override
        public int getMaxStackSize() {
            return origin.getMaxStackSize();
        }

        @Override
        public String toString() {
            return origin.toString();
        }

        @Override
        public boolean equals(Object obj) {
            return origin.equals(obj);
        }

        @Override
        public boolean isSimilar(ItemStack stack) {
            return origin.isSimilar(stack);
        }

        @Override
        public ItemStack clone() {
            return origin.clone();
        }

        @Override
        public int hashCode() {
            return origin.hashCode();
        }

        @Override
        public boolean containsEnchantment(Enchantment ench) {
            return origin.containsEnchantment(ench);
        }

        @Override
        public int getEnchantmentLevel(Enchantment ench) {
            return origin.getEnchantmentLevel(ench);
        }

        @Override
        public Map<Enchantment, Integer> getEnchantments() {
            return origin.getEnchantments();
        }

        @Override
        public void addEnchantments(Map<Enchantment, Integer> enchantments) {
            origin.addEnchantments(enchantments);
        }

        @Override
        public void addEnchantment(Enchantment ench, int level) {
            origin.addEnchantment(ench, level);
        }

        @Override
        public void addUnsafeEnchantments(Map<Enchantment, Integer> enchantments) {
            origin.addUnsafeEnchantments(enchantments);
        }

        @Override
        public void addUnsafeEnchantment(Enchantment ench, int level) {
            origin.addUnsafeEnchantment(ench, level);
        }

        @Override
        public int removeEnchantment(Enchantment ench) {
            return origin.removeEnchantment(ench);
        }

        @Override
        public Map<String, Object> serialize() {
            return origin.serialize();
        }

        @Override
        public ItemMeta getItemMeta() {
            return origin.getItemMeta();
        }

        @Override
        public boolean hasItemMeta() {
            return origin.hasItemMeta();
        }

        @Override
        public boolean setItemMeta(ItemMeta itemMeta) {
            return origin.setItemMeta(itemMeta);
        }
    }
}
