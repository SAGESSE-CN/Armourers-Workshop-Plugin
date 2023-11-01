package moe.plushie.armourers_workshop.core.item;

import moe.plushie.armourers_workshop.core.skin.SkinDescriptor;
import moe.plushie.armourers_workshop.init.ModItems;
import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.item.BlockItem;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.nbt.CompoundTag;

public class SkinItem extends BlockItem {

    public SkinItem(Block block, Properties properties) {
        super(block, properties);
    }

    public static void setSkin(ItemStack targetStack, ItemStack sourceStack) {
        CompoundTag sourceNBT = null;
        if (!sourceStack.isEmpty()) {
            sourceNBT = sourceStack.getTagElement("ArmourersWorkshop");
        }
        if (sourceNBT != null && sourceNBT.size() != 0) {
            targetStack.addTagElement("ArmourersWorkshop", sourceNBT.copy());
        } else {
            targetStack.removeTagKey("ArmourersWorkshop");
        }
    }

    public static void setSkin(ItemStack targetStack, SkinDescriptor descriptor) {
        if (descriptor.isEmpty()) {
            targetStack.removeTagKey("ArmourersWorkshop");
        } else {
            targetStack.addTagElement("ArmourersWorkshop", descriptor.serializeNBT());
        }
    }

    public static ItemStack replaceSkin(ItemStack targetStack, SkinDescriptor descriptor) {
        if (targetStack.isEmpty() || targetStack.is(ModItems.SKIN_TEMPLATE)) {
            return descriptor.asItemStack();
        }
        setSkin(targetStack, descriptor);
        return targetStack;
    }
}
