package moe.plushie.armourers_workshop.plugin.core.skin;

import moe.plushie.armourers_workshop.plugin.api.skin.ISkinType;
import moe.plushie.armourers_workshop.plugin.core.skin.color.ColorScheme;
import moe.plushie.armourers_workshop.plugin.init.ModItems;
import moe.plushie.armourers_workshop.plugin.utils.DataSerializers;
import moe.plushie.armourers_workshop.plugin.utils.FastCache;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.nbt.CompoundTag;

public class SkinDescriptor {

    public static final SkinDescriptor EMPTY = new SkinDescriptor("", SkinTypes.UNKNOWN);

    String identifier;
    ISkinType type;
    ColorScheme colorScheme = ColorScheme.EMPTY;

    public SkinDescriptor(String identifier, ISkinType type) {
        this.identifier = identifier;
        this.type = type;
    }

    public SkinDescriptor(SkinDescriptor descriptor, ColorScheme colorScheme) {
        this.identifier = descriptor.getIdentifier();
        this.type = descriptor.getType();
        this.colorScheme = colorScheme;
    }

    public SkinDescriptor(CompoundTag tag) {
        this.identifier = tag.getString("Identifier");
        this.type = SkinTypes.byName(tag.getString("SkinType"));
        this.colorScheme = new ColorScheme(tag.getCompound("SkinDyes"));
    }

    public static SkinDescriptor of(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return EMPTY;
        }
        return FastCache.ITEM_TO_SKIN_DESCRIPTOR.computeIfAbsent(itemStack, it -> {
            CompoundTag nbt = itemStack.getTag();
            if (nbt == null || !nbt.contains("ArmourersWorkshop")) {
                return EMPTY;
            }
            return DataSerializers.getSkinDescriptor(nbt, "ArmourersWorkshop", EMPTY);
        });
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ISkinType getType() {
        return type;
    }

    public ColorScheme getColorScheme() {
        return colorScheme;
    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = CompoundTag.newInstance();
        nbt.putString("SkinType", type.getRegistryName().toString());
        nbt.putString("Identifier", identifier);
        return nbt;
    }

    public ItemStack asItemStack() {
        ItemStack itemStack = new ItemStack(ModItems.SKIN);
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.put("ArmourersWorkshop", serializeNBT());
        return itemStack;
    }

}
