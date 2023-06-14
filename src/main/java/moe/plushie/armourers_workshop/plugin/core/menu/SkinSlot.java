package moe.plushie.armourers_workshop.plugin.core.menu;

import moe.plushie.armourers_workshop.customapi.CustomSlot;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinSlotType;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinWardrobe;
import moe.plushie.armourers_workshop.plugin.init.ModLog;
import moe.plushie.armourers_workshop.plugin.utils.BukkitUtils;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Collections;

public class SkinSlot extends CustomSlot {

    private final SkinWardrobe wardrobe;
    private final SkinSlotType slotType;
    private final Collection<SkinSlotType> slotTypes;
    private final int slot;

    public SkinSlot(Inventory inventory, SkinWardrobe wardrobe, SkinSlotType slotType, int slot) {
        super(inventory, slot, 0, 0);
        this.wardrobe = wardrobe;
        this.slotType = slotType;
        this.slot = slot;
        this.slotTypes = Collections.singleton(slotType);
    }

    @Override
    public ItemStack getItem() {
        return BukkitUtils.unwrap(wardrobe.getItem(slotType, slot));
    }

    @Override
    public boolean hasItem() {
        return !wardrobe.getItem(slotType, slot).isEmpty();
    }

    @Override
    public void set(ItemStack itemStack) {
        wardrobe.setItem(slotType, slot, BukkitUtils.wrap(itemStack));
        setChanged();
    }

    @Override
    public ItemStack remove(int size) {
        return BukkitUtils.unwrap(wardrobe.removeItem(slotType, slot, size));
    }

    @Override
    public void setChanged() {
        ModLog.debug("setChanged: {}", this);
        wardrobe.save();
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        // when slot type is not provide, we consider it is an unrestricted slot.
        if (!slotTypes.isEmpty() && !slotTypes.contains(SkinSlotType.of(BukkitUtils.wrap(itemStack)))) {
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
