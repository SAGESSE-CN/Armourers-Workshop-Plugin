package moe.plushie.armourers_workshop.plugin.core.skin.color;

import com.google.common.collect.Iterables;
import moe.plushie.armourers_workshop.plugin.core.skin.painting.SkinPaintType;
import moe.plushie.armourers_workshop.plugin.core.skin.painting.SkinPaintTypes;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@SuppressWarnings("unused")
public class ColorScheme {

    public final static ColorScheme EMPTY = new ColorScheme();

    private final HashMap<SkinPaintType, PaintColor> colors = new HashMap<>();
    private HashMap<SkinPaintType, PaintColor> resolvedColors;

    private ColorScheme reference;
    private ResourceLocation texture;

    private int hashCode;

    public ColorScheme() {
    }

    public ColorScheme(CompoundTag nbt) {
        if (nbt == null) {
            return;
        }
        for (String key : nbt.getAllKeys()) {
            SkinPaintType paintType = SkinPaintTypes.byName(key);
            if (paintType != SkinPaintTypes.NONE && nbt.contains(key)) {
                colors.put(paintType, PaintColor.of(nbt.getInt(key)));
            }
        }
    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = CompoundTag.newInstance();
        colors.forEach((paintType, paintColor) -> nbt.putInt(paintType.getRegistryName().toString(), paintColor.getRawValue()));
        return nbt;
    }

    public ColorScheme copy() {
        ColorScheme scheme = new ColorScheme();
        scheme.colors.putAll(colors);
        scheme.reference = reference;
        scheme.texture = texture;
        return scheme;
    }

    public boolean isEmpty() {
        if (this == EMPTY) {
            return true;
        }
        if (reference != null && !reference.isEmpty()) {
            return false;
        }
        if (texture != null) {
            return false;
        }
        return colors.isEmpty();
    }

    @Nullable
    public PaintColor getColor(SkinPaintType paintType) {
        PaintColor color = colors.get(paintType);
        if (color != null) {
            return color;
        }
        if (reference != null) {
            return reference.getColor(paintType);
        }
        return null;
    }

    public void setColor(SkinPaintType paintType, PaintColor color) {
        colors.put(paintType, color);
        resolvedColors = null;
        hashCode = 0;
    }

    public PaintColor getResolvedColor(SkinPaintType paintType) {
        if (resolvedColors == null) {
            resolvedColors = getResolvedColors();
        }
        return resolvedColors.get(paintType);
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public void setTexture(ResourceLocation texture) {
        this.texture = texture;
    }

    public ColorScheme getReference() {
        if (reference != null) {
            return reference;
        }
        return ColorScheme.EMPTY;
    }

    public void setReference(ColorScheme reference) {
        // referring empty scheme not have any effect.
        if (reference != null && reference.isEmpty()) {
            reference = null;
        }
        if (!Objects.equals(this.reference, reference)) {
            this.reference = reference;
            this.resolvedColors = null;
            this.hashCode = 0;
        }
    }

    private HashMap<SkinPaintType, PaintColor> getResolvedColors() {
        HashMap<SkinPaintType, PaintColor> resolvedColors = new HashMap<>();
        HashMap<SkinPaintType, ArrayList<SkinPaintType>> dependencies = new HashMap<>();
        // build all reference dependencies
        if (reference != null) {
            resolvedColors.putAll(reference.getResolvedColors());
        }
        // build all item dependencies
        Iterables.concat(colors.entrySet(), getReference().colors.entrySet()).forEach(e -> {
            SkinPaintType paintType = e.getKey();
            PaintColor color = e.getValue();
            if (color.getPaintType().getDyeType() != null) {
                dependencies.computeIfAbsent(color.getPaintType(), k -> new ArrayList<>()).add(paintType);
            } else {
                resolvedColors.put(paintType, color);
            }
        });
        if (resolvedColors.isEmpty()) {
            return resolvedColors;
        }
        // merge all items whens dependencies
        dependencies.forEach((key, value) -> Iterables.tryFind(dependencies.values(), v -> v.contains(key)).toJavaUtil().ifPresent(target -> {
            if (target != value) {
                target.addAll(value);
            }
            value.clear(); // clear to prevent infinite loop occurs
        }));
        dependencies.forEach((key, value) -> value.forEach(paintType -> resolvedColors.put(paintType, resolvedColors.get(key))));
        return resolvedColors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (o.hashCode() != this.hashCode()) return false;
        ColorScheme scheme = (ColorScheme) o;
        return colors.equals(scheme.colors) && Objects.equals(texture, scheme.texture) && Objects.equals(reference, scheme.reference);
    }

    @Override
    public int hashCode() {
        if (hashCode == 0) {
            hashCode = Objects.hash(colors, texture, reference);
            if (hashCode == 0) {
                hashCode = ~hashCode;
            }
        }
        return hashCode;
    }

    @Override
    public String toString() {
        return "[" + getResolvedColors() + "]";
    }
}
