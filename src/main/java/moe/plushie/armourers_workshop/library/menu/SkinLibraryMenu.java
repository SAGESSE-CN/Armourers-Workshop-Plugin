package moe.plushie.armourers_workshop.library.menu;

import moe.plushie.armourers_workshop.api.WorldAccessor;
import moe.plushie.armourers_workshop.core.item.SkinItem;
import moe.plushie.armourers_workshop.core.menu.UpdatableContainerMenu;
import moe.plushie.armourers_workshop.core.skin.SkinDescriptor;
import moe.plushie.armourers_workshop.init.ModItems;
import moe.plushie.armourers_workshop.library.blockentity.SkinLibraryBlockEntity;
import moe.plushie.armourers_workshop.library.data.SkinLibraryManager;
import net.cocoonmc.core.inventory.MenuType;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.world.entity.Player;

public class SkinLibraryMenu extends UpdatableContainerMenu<SkinLibraryBlockEntity> {

    private int libraryVersion = -1;

    public SkinLibraryMenu(MenuType<?> menuType, Player player, WorldAccessor accessor) {
        super(menuType, player, accessor);
        this.addInputSlot(getInventory(), 0, 0, 0, PlaceFilter.allowsSkinOrTemplate(), null);
        this.addOutputSlot(getInventory(), 1, 0, 0);
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        SkinLibraryManager.Server server = SkinLibraryManager.getServer();
        if (libraryVersion != server.getVersion()) {
            server.sendTo(player);
            libraryVersion = server.getVersion();
        }
    }

    public boolean shouldSaveStack() {
        return outputSlots.get(0).getItem().isEmpty();
    }

    public boolean shouldLoadStack() {
        ItemStack outputStack = outputSlots.get(0).getItem();
        ItemStack inputStack = inputSlots.get(0).getItem();
        return outputStack.isEmpty() && !inputStack.isEmpty() && inputStack.is(ModItems.SKIN_TEMPLATE.get());
    }

    public void crafting(SkinDescriptor descriptor) {
        boolean consume = true;
        ItemStack itemStack = inputSlots.get(0).getItem();
        ItemStack newItemStack = itemStack.copy();
        if (descriptor != null) {
            // only consumes the template
            newItemStack = SkinItem.replaceSkin(newItemStack, descriptor);
            consume = itemStack.is(ModItems.SKIN_TEMPLATE.get());
        }
        outputSlots.get(0).setItem(newItemStack);
        if (consume) {
            itemStack.shrink(1);
        }
    }
}
