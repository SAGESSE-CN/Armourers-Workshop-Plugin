package moe.plushie.armourers_workshop.plugin.core.menu;

import moe.plushie.armourers_workshop.plugin.api.WorldAccessor;
import moe.plushie.armourers_workshop.plugin.core.recipe.SkinningRecipes;
import net.cocoonmc.core.inventory.MenuType;
import net.cocoonmc.core.inventory.Slot;
import net.cocoonmc.core.item.ItemStack;
import org.bukkit.entity.Player;

public class SkinningTableMenu extends BlockContainerMenu {

    public SkinningTableMenu(MenuType<?> menuType, Player player, WorldAccessor accessor) {
        super(menuType, player, accessor);
        this.addInputSlot(getInventory(), 0, 37, 22);
        this.addInputSlot(getInventory(), 1, 37, 58);
        this.addOutputSlot(getInventory(), 2, 119, 40);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.outputSlots.forEach(it -> it.setItemNoUpdate(ItemStack.EMPTY));
        this.clearContainer(player, getInventory());
    }

    @Override
    protected void onInputSlotChange(Slot slot) {
        onCraftSlotChanges();
    }

    @Override
    protected void onOutputSlotChange(Slot slot) {
        if (slot.hasItem()) {
            return;
        }
        SkinningRecipes.onCraft(inputSlots);
        onCraftSlotChanges();
    }

    protected void onCraftSlotChanges() {
        outputSlots.get(0).setItemNoUpdate(SkinningRecipes.getRecipeOutput(inputSlots));
    }

    @Override
    public int getContainerSize() {
        return 3;
    }
}
