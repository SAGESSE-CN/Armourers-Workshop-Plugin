package moe.plushie.armourers_workshop.plugin.core.recipe;


import moe.plushie.armourers_workshop.plugin.api.skin.ISkinToolType;
import moe.plushie.armourers_workshop.plugin.api.skin.ISkinType;
import net.cocoonmc.core.item.ItemStack;

public class SkinningItemRecipe extends SkinningRecipe {

    private ISkinToolType toolType;

    public SkinningItemRecipe(ISkinType skinType) {
        super(skinType);
        if (skinType instanceof ISkinToolType) {
            this.toolType = (ISkinToolType) skinType;
        }
    }

    @Override
    protected boolean isValidTarget(ItemStack itemStack) {
        if (toolType != null) {
            return toolType.contains(itemStack);
        }
        return super.isValidTarget(itemStack);
    }
}
