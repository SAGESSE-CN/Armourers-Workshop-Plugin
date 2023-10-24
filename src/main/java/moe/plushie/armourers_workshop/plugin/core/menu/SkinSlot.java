package moe.plushie.armourers_workshop.plugin.core.menu;

import moe.plushie.armourers_workshop.customapi.CustomSlot;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinSlotType;
import moe.plushie.armourers_workshop.plugin.utils.BukkitUtils;
import moe.plushie.armourers_workshop.plugin.utils.ObjectUtils;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class SkinSlot extends CustomSlot {

    protected final Collection<SkinSlotType> slotTypes;

    public SkinSlot(Inventory inventory, int index, int x, int y, SkinSlotType... slotTypes) {
        super(inventory, index, x, y);
        this.slotTypes = ObjectUtils.map(slotTypes);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        // when slot type is not provide, we consider it is an unrestricted slot.
        if (!slotTypes.isEmpty() && !slotTypes.contains(SkinSlotType.of(BukkitUtils.wrap(itemStack)))) {
            return false;
        }
//        return container.canPlaceItem(index, itemStack);
        return false;
    }

    public Collection<SkinSlotType> getSlotTypes() {
        return slotTypes;
    }
}
