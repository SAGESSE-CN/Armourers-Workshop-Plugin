package moe.plushie.armourers_workshop.core.skin;

import moe.plushie.armourers_workshop.init.ModConfig;
import moe.plushie.armourers_workshop.utils.OptionalCompoundTag;
import net.cocoonmc.core.nbt.CompoundTag;

import java.util.Objects;
import java.util.function.BooleanSupplier;

public class SkinOptions {

    public static SkinOptions DEFAULT = new SkinOptions();

    private int tooltipFlags = 0;
    private int enableEmbeddedItemRenderer = 0;

    public SkinOptions() {
    }

    public SkinOptions(CompoundTag tag) {
        this.tooltipFlags = tag.getInt("TooltipFlags");
        this.enableEmbeddedItemRenderer = tag.getInt("EmbeddedItemRenderer");
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = CompoundTag.newInstance();
        OptionalCompoundTag otag = new OptionalCompoundTag(tag);
        otag.putOptionalInt("TooltipFlags", tooltipFlags, 0);
        otag.putOptionalInt("EmbeddedItemRenderer", enableEmbeddedItemRenderer, 0);
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SkinOptions)) return false;
        SkinOptions that = (SkinOptions) o;
        return tooltipFlags == that.tooltipFlags && enableEmbeddedItemRenderer == that.enableEmbeddedItemRenderer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tooltipFlags, enableEmbeddedItemRenderer);
    }
}
