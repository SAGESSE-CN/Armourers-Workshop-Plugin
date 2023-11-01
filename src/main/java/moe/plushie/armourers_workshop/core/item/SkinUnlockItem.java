package moe.plushie.armourers_workshop.core.item;

import moe.plushie.armourers_workshop.api.skin.ISkinType;
import moe.plushie.armourers_workshop.core.skin.SkinSlotType;
import moe.plushie.armourers_workshop.core.skin.SkinWardrobe;
import net.cocoonmc.core.item.Item;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.network.Component;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.core.world.InteractionResultHolder;
import net.cocoonmc.core.world.entity.Player;

public class SkinUnlockItem extends Item {

    private final SkinSlotType slotType;

    public SkinUnlockItem(SkinSlotType slotType, Properties properties) {
        super(properties);
        this.slotType = slotType;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(ItemStack itemStack, Player player, InteractionHand hand) {
        ISkinType skinType = slotType.getSkinType();
        SkinWardrobe wardrobe = SkinWardrobe.of(player);
        if (wardrobe == null || skinType == null) {
            return InteractionResultHolder.fail(itemStack);
        }
        Component skinName = Component.translatable("skinType.armourers_workshop." + skinType.getRegistryName().getPath());
        if (wardrobe.getUnlockedSize(slotType) >= slotType.getMaxSize()) {
            player.sendMessage(Component.translatable("chat.armourers_workshop.slotUnlockedFailed", skinName));
            return InteractionResultHolder.fail(itemStack);
        }
        itemStack.shrink(1);
        int count = wardrobe.getUnlockedSize(slotType) + 1;
        wardrobe.setUnlockedSize(slotType, count);
        wardrobe.broadcast();
        player.sendMessage(Component.translatable("chat.armourers_workshop.slotUnlocked", skinName, Integer.toString(count)));
        return InteractionResultHolder.success(itemStack);
    }
}
