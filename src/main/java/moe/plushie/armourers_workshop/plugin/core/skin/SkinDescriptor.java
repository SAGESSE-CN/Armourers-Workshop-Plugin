package moe.plushie.armourers_workshop.plugin.core.skin;

import moe.plushie.armourers_workshop.plugin.api.ItemStack;
import net.querz.nbt.tag.CompoundTag;

public class SkinDescriptor {

    public static final SkinDescriptor EMPTY = new SkinDescriptor("", "armourers:unknown");

    String id;
    String type;

    public SkinDescriptor(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public ItemStack asItemStack() {
        ItemStack itemStack = new ItemStack("armourers_workshop:skin");
        CompoundTag tag = itemStack.getTag();
        CompoundTag skinNBT = new CompoundTag();
        skinNBT.putString("SkinType", type);
        skinNBT.putString("Identifier", id);
        tag.put("ArmourersWorkshop", skinNBT);
        return itemStack;
    }

}
