package moe.plushie.armourers_workshop.plugin.api.skin;

import org.bukkit.inventory.EquipmentSlot;

public interface ISkinArmorType extends ISkinEquipmentType {

    EquipmentSlot getSlotType();
}
