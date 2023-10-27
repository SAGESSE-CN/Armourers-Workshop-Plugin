package moe.plushie.armourers_workshop.plugin.api.skin;

import net.cocoonmc.core.item.ItemStack;

public interface ISkinToolType extends ISkinEquipmentType {

    /**
     * Does tool type contain the specified item?
     */
    boolean contains(ItemStack itemStack);
}
