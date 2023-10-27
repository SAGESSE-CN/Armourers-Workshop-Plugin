package moe.plushie.armourers_workshop.plugin.core.menu;

import moe.plushie.armourers_workshop.plugin.core.skin.SkinSlotType;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinWardrobe;
import net.cocoonmc.core.inventory.Slot;
import net.cocoonmc.core.item.ItemStack;
import org.bukkit.inventory.Inventory;

import java.util.Collection;
import java.util.Collections;

public class SkinDataSlot extends Slot {

    private final SkinWardrobe wardrobe;
    private final SkinSlotType slotType;
    private final Collection<SkinSlotType> slotTypes;

    private ItemStack lastItem;

    public SkinDataSlot(Inventory inventory, SkinWardrobe wardrobe, SkinSlotType slotType, int slot) {
        super(inventory, slot, 0, 0);
        this.wardrobe = wardrobe;
        this.slotType = slotType;
        this.slotTypes = Collections.singleton(slotType);
    }

    @Override
    public ItemStack getItem() {
        if (lastItem == null) {
            lastItem = wardrobe.getItem(slotType, slot);
        }
        return lastItem;
    }

    @Override
    public boolean hasItem() {
        return getItem().isEmpty();
    }

    @Override
    public void setItem(ItemStack itemStack) {
        wardrobe.setItem(slotType, slot, itemStack);
        setChanged();
    }

    @Override
    public ItemStack removeItem(int size) {
        return wardrobe.removeItem(slotType, slot, size);
    }

    @Override
    public void setChanged() {
        wardrobe.save();
        lastItem = null;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        // when slot type is not provide, we consider it is an unrestricted slot.
        if (!slotTypes.isEmpty() && !slotTypes.contains(SkinSlotType.of(itemStack))) {
            return false;
        }
        // TODO
//        return container.canPlaceItem(index, itemStack);
        return true;
    }

    public Collection<SkinSlotType> getSlotTypes() {
        return slotTypes;
    }
}
