package moe.plushie.armourers_workshop.plugin.core.item;

import moe.plushie.armourers_workshop.plugin.core.skin.SkinWardrobe;
import moe.plushie.armourers_workshop.plugin.init.ModMenuTypes;
import moe.plushie.armourers_workshop.plugin.init.platform.MenuManager;
import net.cocoonmc.core.item.Item;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.core.world.InteractionResult;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

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
