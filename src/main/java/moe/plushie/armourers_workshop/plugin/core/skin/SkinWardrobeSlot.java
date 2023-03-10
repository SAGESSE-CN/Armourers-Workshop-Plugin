package moe.plushie.armourers_workshop.plugin.core.skin;

import moe.plushie.armourers_workshop.plugin.api.ItemStack;
import moe.plushie.armourers_workshop.plugin.api.Slot;

public class SkinWardrobeSlot implements Slot {

    private final SkinWardrobe wardrobe;
    private final SkinSlotType slotType;
    private final int slot;

    public SkinWardrobeSlot(SkinWardrobe wardrobe, SkinSlotType slotType, int slot) {
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
}
