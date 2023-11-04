package moe.plushie.armourers_workshop.core.recipe;


import moe.plushie.armourers_workshop.api.skin.ISkinArmorType;
import moe.plushie.armourers_workshop.api.skin.ISkinType;
import net.cocoonmc.core.item.ItemStack;
import org.bukkit.inventory.EquipmentSlot;

public class SkinningArmourRecipe extends SkinningRecipe {

    private EquipmentSlot slotType;

    public SkinningArmourRecipe(ISkinType skinType) {
        super(skinType);
        if (skinType instanceof ISkinArmorType) {
            slotType = ((ISkinArmorType) skinType).getSlotType();
        }
    }

    @Override
    protected boolean isValidTarget(ItemStack itemStack) {
        if (slotType != null) {
            return itemStack.getItem().getEquipmentSlot() == slotType;
        }
        return false;
    }
}
