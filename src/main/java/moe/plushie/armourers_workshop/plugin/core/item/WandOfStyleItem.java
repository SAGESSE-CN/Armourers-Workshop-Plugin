package moe.plushie.armourers_workshop.plugin.core.item;

import moe.plushie.armourers_workshop.plugin.api.InteractionResult;
import moe.plushie.armourers_workshop.plugin.api.Item;
import moe.plushie.armourers_workshop.plugin.api.ItemStack;
import moe.plushie.armourers_workshop.plugin.core.menu.MenuManager;
import moe.plushie.armourers_workshop.plugin.core.menu.SkinWardrobeMenu;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinWardrobe;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

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
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity entity, EquipmentSlot hand) {
        openGUI(player, entity);
        return InteractionResult.SUCCESS;
    }

    private void openGUI(Player player, Entity entity) {
        SkinWardrobe wardrobe = SkinWardrobe.of(entity);
        if (wardrobe != null && wardrobe.isEditable(player)) {
            SkinWardrobeMenu menu = new SkinWardrobeMenu(wardrobe, player);
            MenuManager.openMenu(menu, player);
        }
    }
}
