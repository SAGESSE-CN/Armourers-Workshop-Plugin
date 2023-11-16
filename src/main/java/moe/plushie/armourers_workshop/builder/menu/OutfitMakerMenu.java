package moe.plushie.armourers_workshop.builder.menu;

import moe.plushie.armourers_workshop.api.WorldAccessor;
import moe.plushie.armourers_workshop.builder.blockentity.ColorMixerBlockEntity;
import moe.plushie.armourers_workshop.core.item.BottleItem;
import moe.plushie.armourers_workshop.core.menu.BlockContainerMenu;
import moe.plushie.armourers_workshop.init.ModItems;
import net.cocoonmc.core.inventory.Container;
import net.cocoonmc.core.inventory.MenuType;
import net.cocoonmc.core.inventory.SimpleContainer;
import net.cocoonmc.core.inventory.Slot;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.world.entity.Player;

public class OutfitMakerMenu extends BlockContainerMenu {

    public OutfitMakerMenu(MenuType<?> menuType, Player player, WorldAccessor accessor) {
        super(menuType, player, accessor);
    }

    @Override
    protected Container createInventoryContainer() {
        return new SimpleContainer(2);
    }
}
