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

public class ColorMixerMenu extends BlockContainerMenu {

    private final ColorMixerBlockEntity blockEntity;

    public ColorMixerMenu(MenuType<?> menuType, Player player, WorldAccessor accessor) {
        super(menuType, player, accessor);
        this.blockEntity = (ColorMixerBlockEntity) accessor.getBlockEntity();
        this.addInputSlot(getInventory(), 0, 83, 101, PlaceFilter.only(ModItems.BOTTLE), null);
        this.addOutputSlot(getInventory(), 1, 134, 101);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.clearContainer(player, getInventory());
    }

    @Override
    protected void onInputSlotChange(Slot slot) {
        crafting();
    }

    @Override
    protected void onOutputSlotChange(Slot slot) {
        crafting();
    }

    private void crafting() {
        ItemStack inputStack = inputSlots.get(0).getItem();
        if (inputStack.isEmpty()) {
            return;
        }
        if (outputSlots.get(0).hasItem()) {
            return;
        }
        ItemStack newItemStack = inputStack.copy();
        BottleItem.setColor(newItemStack, blockEntity.getColor());
        outputSlots.get(0).setItem(newItemStack);
        inputSlots.get(0).setItem(ItemStack.EMPTY);
    }

    @Override
    protected Container createInventoryContainer() {
        return new SimpleContainer(2);
    }
}
