package moe.plushie.armourers_workshop.core.item;

import moe.plushie.armourers_workshop.core.skin.SkinWardrobe;
import moe.plushie.armourers_workshop.init.ModMenuTypes;
import moe.plushie.armourers_workshop.init.platform.MenuManager;
import net.cocoonmc.core.item.Item;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.core.world.InteractionResult;
import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.core.world.entity.LivingEntity;
import net.cocoonmc.core.world.entity.Player;

public class WandOfStyleItem extends Item {

    public WandOfStyleItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult attackLivingEntity(ItemStack itemStack, Player player, Entity entity) {
        openGUI(player, entity);
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity entity, InteractionHand hand) {
        openGUI(player, entity);
        return InteractionResult.SUCCESS;
    }

    private void openGUI(Player player, Entity entity) {
        SkinWardrobe wardrobe = SkinWardrobe.of(entity);
        if (wardrobe != null && wardrobe.isEditable(player)) {
            MenuManager.openMenu(ModMenuTypes.WARDROBE, player, wardrobe);
        }
    }
}
