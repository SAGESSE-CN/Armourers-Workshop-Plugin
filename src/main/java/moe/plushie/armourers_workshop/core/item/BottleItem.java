package moe.plushie.armourers_workshop.core.item;

import moe.plushie.armourers_workshop.core.skin.color.PaintColor;
import moe.plushie.armourers_workshop.utils.OptionalCompoundTag;
import net.cocoonmc.core.item.Item;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

public class BottleItem extends Item {

    public BottleItem(Properties properties) {
        super(properties);
    }


    @Nullable
    public static PaintColor getColor(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        if (tag == null) {
            return null;
        }
        OptionalCompoundTag tag1 = new OptionalCompoundTag(tag);
        return tag1.getOptionalPaintColor("Color", null);
    }

    public static void setColor(ItemStack itemStack, PaintColor paintColor) {
        OptionalCompoundTag tag = new OptionalCompoundTag(itemStack.getOrCreateTag());
        tag.putOptionalPaintColor("Color", paintColor, null);
    }
}
