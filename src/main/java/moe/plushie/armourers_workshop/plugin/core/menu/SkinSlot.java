package moe.plushie.armourers_workshop.plugin.core.menu;

import moe.plushie.armourers_workshop.plugin.api.ItemStack;
import moe.plushie.armourers_workshop.plugin.api.Slot;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinSlotType;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinWardrobe;

import java.util.Collection;
import java.util.Collections;

public class SkinSlot implements Slot {

    private int index = 0;

    private final SkinWardrobe wardrobe;
    private final SkinSlotType slotType;
    private final Collection<SkinSlotType> slotTypes;
    private final int slot;

    public SkinSlot(SkinWardrobe wardrobe, SkinSlotType slotType, int slot) {
        this.wardrobe = wardrobe;
        this.slotType = slotType;
        this.slot = slot;
        this.slotTypes = Collections.singleton(slotType);
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public ItemStack getItem() {
        return wardrobe.getItem(slotType, slot);
    }

    @Override
    public boolean hasItem() {
        return !wardrobe.getItem(slotType, slot).isEmpty();
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void setItem(ItemStack itemStack) {
        wardrobe.setItem(slotType, slot, itemStack);
        setChanged();
    }

    @Override
    public void setChanged() {
        wardrobe.save();
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
