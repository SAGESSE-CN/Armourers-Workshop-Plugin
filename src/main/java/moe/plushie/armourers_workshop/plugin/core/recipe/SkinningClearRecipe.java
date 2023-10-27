package moe.plushie.armourers_workshop.plugin.core.recipe;

import moe.plushie.armourers_workshop.plugin.core.item.SkinItem;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinDescriptor;
import moe.plushie.armourers_workshop.plugin.init.ModItems;
import net.cocoonmc.core.item.ItemStack;

public class SkinningClearRecipe extends SkinningRecipe {

    public SkinningClearRecipe() {
        super(null);
    }

    @Override
    protected ItemStack build(ItemStack targetStack, ItemStack skinStack) {
        ItemStack newItemStack = skinStack.copyWithCount(1);
        SkinItem.setSkin(newItemStack, SkinDescriptor.EMPTY);
        return newItemStack;
    }

    @Override
    protected boolean isValidTarget(ItemStack targetStack) {
        return targetStack.is(ModItems.SOAP);
    }

    @Override
    protected boolean isValidSkin(ItemStack itemStack) {
        return !SkinDescriptor.of(itemStack).isEmpty();
    }
}
