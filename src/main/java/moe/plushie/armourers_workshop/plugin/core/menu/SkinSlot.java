package moe.plushie.armourers_workshop.plugin.core.menu;

import moe.plushie.armourers_workshop.plugin.api.ItemStack;
import moe.plushie.armourers_workshop.plugin.api.Slot;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinSlotType;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinWardrobe;

public class SkinSlot implements Slot {

    private final SkinWardrobe wardrobe;
    private final SkinSlotType slotType;
    private final int slot;

    public SkinSlot(SkinWardrobe wardrobe, SkinSlotType slotType, int slot) {
        this.wardrobe = wardrobe;
        this.slotType = slotType;
        this.slot = slot;
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
    public void set(ItemStack itemStack) {
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
        if (!slotType.equals(SkinSlotType.of(itemStack))) {
            return false;
        }
        //return container.canPlaceItem(index, itemStack);
        return true;
    }
}
