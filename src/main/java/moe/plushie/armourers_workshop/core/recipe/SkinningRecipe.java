package moe.plushie.armourers_workshop.core.recipe;


import moe.plushie.armourers_workshop.api.skin.ISkinType;
import moe.plushie.armourers_workshop.core.item.SkinItem;
import moe.plushie.armourers_workshop.core.skin.SkinDescriptor;
import moe.plushie.armourers_workshop.init.ModItems;
import net.cocoonmc.core.inventory.Slot;
import net.cocoonmc.core.item.ItemStack;

public abstract class SkinningRecipe {

    protected ISkinType skinType;

    public SkinningRecipe(ISkinType skinType) {
        this.skinType = skinType;
    }

    public void apply(Iterable<Slot> slots) {
        ItemStack skinStack = ItemStack.EMPTY;
        ItemStack targetStack = ItemStack.EMPTY;

        for (Slot slot : slots) {
            ItemStack itemStack = slot.getItem();
            if (itemStack.isEmpty()) {
                continue;
            }
            if (isValidSkin(itemStack)) {
                skinStack = itemStack;
                continue;
            }
            if (isValidTarget(itemStack)) {
                targetStack = itemStack;
                continue;
            }
            return;
        }

        if (targetStack.isEmpty() || skinStack.isEmpty()) {
            return;
        }

        shrink(targetStack, skinStack);
    }

    public ItemStack test(Iterable<Slot> slots) {
        ItemStack skinStack = ItemStack.EMPTY;
        ItemStack targetStack = ItemStack.EMPTY;

        for (Slot slot : slots) {
            ItemStack itemStack = slot.getItem();
            if (itemStack.isEmpty()) {
                continue;
            }
            if (isValidSkin(itemStack)) {
                skinStack = itemStack;
                continue;
            }
            if (isValidTarget(itemStack)) {
                targetStack = itemStack;
                continue;
            }
            return ItemStack.EMPTY;
        }

        if (targetStack.isEmpty() || skinStack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        return build(targetStack, skinStack);
    }

    protected void shrink(ItemStack targetStack, ItemStack skinStack) {
        targetStack.shrink(1);
        skinStack.shrink(1);
    }

    protected ItemStack build(ItemStack targetStack, ItemStack skinStack) {
        ItemStack newItemStack = targetStack.copyWithCount(1);
        SkinItem.setSkin(newItemStack, skinStack);
        return newItemStack;
    }

    protected boolean isValidSkin(ItemStack itemStack) {
        return itemStack.is(ModItems.SKIN.get()) && SkinDescriptor.of(itemStack).getType() == skinType;
    }

    protected boolean isValidTarget(ItemStack itemStack) {
        return !itemStack.is(ModItems.SKIN.get());
    }
}
