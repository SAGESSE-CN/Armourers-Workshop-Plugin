package moe.plushie.armourers_workshop.plugin.core.item;

import moe.plushie.armourers_workshop.plugin.api.InteractionResultHolder;
import moe.plushie.armourers_workshop.plugin.api.Item;
import moe.plushie.armourers_workshop.plugin.api.ItemStack;
import moe.plushie.armourers_workshop.plugin.api.skin.ISkinType;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinSlotType;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinWardrobe;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

public class SkinUnlockItem extends Item {

    private final SkinSlotType slotType;

    public SkinUnlockItem(SkinSlotType slotType, Properties properties) {
        super(properties);
        this.slotType = slotType;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(ItemStack itemStack, Player player, EquipmentSlot hand) {
        ISkinType skinType = slotType.getSkinType();
        SkinWardrobe wardrobe = SkinWardrobe.of(player);
        if (wardrobe == null || skinType == null) {
            return InteractionResultHolder.fail(itemStack);
        }
//        Component skinName = TranslateUtils.Name.of(skinType);
        if (wardrobe.getUnlockedSize(slotType) >= slotType.getMaxSize()) {
//            player.sendSystemMessage(Component.translatable("chat.armourers_workshop.slotUnlockedFailed", skinName));
            return InteractionResultHolder.fail(itemStack);
        }
        itemStack.shrink(1);
        int count = wardrobe.getUnlockedSize(slotType) + 1;
        wardrobe.setUnlockedSize(slotType, count);
        wardrobe.broadcast();
//        player.sendSystemMessage(Component.translatable("chat.armourers_workshop.slotUnlocked", skinName, Integer.toString(count)));
        return InteractionResultHolder.success(itemStack);
    }
}
