package moe.plushie.armourers_workshop.core.skin;

import moe.plushie.armourers_workshop.api.skin.ISkinType;
import moe.plushie.armourers_workshop.core.skin.color.ColorScheme;
import moe.plushie.armourers_workshop.init.ModItems;
import moe.plushie.armourers_workshop.utils.CacheKeys;
import moe.plushie.armourers_workshop.utils.DataSerializers;
import moe.plushie.armourers_workshop.utils.OptionalCompoundTag;
import net.cocoonmc.Cocoon;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.nbt.CompoundTag;

public class SkinDescriptor {

    public static final SkinDescriptor EMPTY = new SkinDescriptor("", SkinTypes.UNKNOWN);

    String identifier;
    ISkinType type;
    SkinOptions options = SkinOptions.DEFAULT;
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
        OptionalCompoundTag otag = new OptionalCompoundTag(tag);
        this.options = otag.getOptionalSkinOptions("SkinOptions", SkinOptions.DEFAULT);
        this.colorScheme = otag.getOptionalColorScheme("SkinDyes", ColorScheme.EMPTY);
    }

    public static SkinDescriptor of(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return EMPTY;
        }
        return Cocoon.API.CACHE.computeIfAbsent(itemStack, CacheKeys.SKIN_KEY, it -> {
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
        OptionalCompoundTag otag = new OptionalCompoundTag(nbt);
        otag.putOptionalSkinOptions("SkinOptions", options, SkinOptions.DEFAULT);
        otag.putOptionalColorScheme("SkinDyes", colorScheme, ColorScheme.EMPTY);
        return nbt;
    }

    public ItemStack asItemStack() {
        ItemStack itemStack = new ItemStack(ModItems.SKIN.get());
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.put("ArmourersWorkshop", serializeNBT());
        return itemStack;
    }

}
