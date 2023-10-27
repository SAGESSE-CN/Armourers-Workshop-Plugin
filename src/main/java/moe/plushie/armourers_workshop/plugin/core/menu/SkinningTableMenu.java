package moe.plushie.armourers_workshop.plugin.core.menu;

import moe.plushie.armourers_workshop.plugin.api.WorldAccessor;
import moe.plushie.armourers_workshop.plugin.core.recipe.SkinningRecipes;
import net.cocoonmc.core.inventory.MenuType;
import net.cocoonmc.core.inventory.Slot;
import net.cocoonmc.core.item.ItemStack;
import org.bukkit.entity.Player;

public class SkinningTableMenu extends BlockContainerMenu {

    private ItemStack output = ItemStack.EMPTY;

    public SkinningTableMenu(MenuType<?> menuType, Player player, WorldAccessor accessor) {
        super(menuType, player, accessor);
        this.addInputSlot(inventory, 0, 37, 22);
        this.addInputSlot(inventory, 1, 37, 58);
        this.addOutputSlot(inventory, 2, 119, 40);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.clearContainer(player, inventory);
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
    public int getSlotSize() {
        return 3;
    }
}
