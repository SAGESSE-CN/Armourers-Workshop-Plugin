package moe.plushie.armourers_workshop.core.item;

import moe.plushie.armourers_workshop.core.data.SkinBlockPlaceContext;
import moe.plushie.armourers_workshop.core.skin.SkinDescriptor;
import moe.plushie.armourers_workshop.core.skin.SkinSlotType;
import moe.plushie.armourers_workshop.core.skin.SkinTypes;
import moe.plushie.armourers_workshop.core.skin.SkinWardrobe;
import moe.plushie.armourers_workshop.init.ModItems;
import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.item.BlockItem;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.item.context.BlockPlaceContext;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.core.world.InteractionResult;
import net.cocoonmc.core.world.InteractionResultHolder;
import net.cocoonmc.core.world.entity.Player;

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

    @Override
    public InteractionResultHolder<ItemStack> use(ItemStack itemStack, Player player, InteractionHand hand) {
        SkinDescriptor descriptor = SkinDescriptor.of(itemStack);
        SkinSlotType slotType = SkinSlotType.of(descriptor.getType());
        if (descriptor.isEmpty() || slotType == null) {
            return InteractionResultHolder.pass(itemStack);
        }
        SkinWardrobe wardrobe = SkinWardrobe.of(player);
        if (wardrobe == null || !wardrobe.isEditable(player)) {
            return InteractionResultHolder.pass(itemStack);
        }
        int slot = wardrobe.getFreeSlot(slotType);
        if (!wardrobe.getItem(slotType, slot).isEmpty()) {
            return InteractionResultHolder.pass(itemStack);
        }
        wardrobe.setItem(slotType, slot, itemStack.copy());
        wardrobe.broadcast();
        itemStack.shrink(1);
        return InteractionResultHolder.consume(itemStack.copy());
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        ItemStack itemStack = context.getItemInHand();
        SkinDescriptor descriptor = SkinDescriptor.of(itemStack);
        if (descriptor.getType() != SkinTypes.BLOCK) {
            return InteractionResult.PASS;
        }
        // we need expand the context info.
        return super.place(new SkinBlockPlaceContext(context));
    }
}
