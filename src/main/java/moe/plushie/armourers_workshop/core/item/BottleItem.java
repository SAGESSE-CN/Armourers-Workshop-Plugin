package moe.plushie.armourers_workshop.core.item;

import moe.plushie.armourers_workshop.core.skin.color.PaintColor;
import moe.plushie.armourers_workshop.utils.OptionalCompoundTag;
import net.cocoonmc.core.item.Item;
import net.cocoonmc.core.item.ItemStack;

public class BottleItem extends Item {

    public BottleItem(Properties properties) {
        super(properties);
    }

    public static void setColor(ItemStack itemStack, PaintColor paintColor) {
        itemStack.getOrCreateTag();
        OptionalCompoundTag tag = new OptionalCompoundTag(itemStack.getOrCreateTag());
        tag.putOptionalPaintColor("Color", paintColor, null);
    }
}
