package moe.plushie.armourers_workshop.utils;

import moe.plushie.armourers_workshop.core.skin.SkinDescriptor;
import moe.plushie.armourers_workshop.core.skin.SkinOptions;
import moe.plushie.armourers_workshop.core.skin.SkinProperties;
import moe.plushie.armourers_workshop.core.skin.color.ColorScheme;
import moe.plushie.armourers_workshop.core.skin.color.PaintColor;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.math.Vector3f;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.nbt.FloatTag;
import net.cocoonmc.core.nbt.IntTag;
import net.cocoonmc.core.nbt.ListTag;

import java.util.ArrayList;
import java.util.Collection;

public class OptionalCompoundTag {

    final CompoundTag tag;

    public OptionalCompoundTag(CompoundTag tag) {
        this.tag = tag;
    }

    public boolean getOptionalBoolean(String key, boolean defaultValue) {
        if (tag.contains(key, 1)) {
            return tag.getBoolean(key);
        }
        return defaultValue;
    }

    public void putOptionalBoolean(String key, boolean value, boolean defaultValue) {
        if (_shouldPutValue(tag, key, value, defaultValue)) {
            tag.putBoolean(key, value);
        }
    }

    public int getOptionalInt(String key, int defaultValue) {
        if (tag.contains(key, 3)) {
            return tag.getInt(key);
        }
        return defaultValue;
    }

    public void putOptionalInt(String key, int value, int defaultValue) {
        if (_shouldPutValue(tag, key, value, defaultValue)) {
            tag.putInt(key, value);
        }
    }

    public float getOptionalFloat(String key, float defaultValue) {
        if (tag.contains(key, 5)) {
            return tag.getFloat(key);
        }
        return defaultValue;
    }

    public void putOptionalFloat(String key, float value, float defaultValue) {
        if (_shouldPutValue(tag, key, value, defaultValue)) {
            tag.putFloat(key, value);
        }
    }

    public String getOptionalString(String key, String defaultValue) {
        if (tag.contains(key, 8)) {
            return tag.getString(key);
        }
        return defaultValue;
    }

    public void putOptionalString(String key, String value, String defaultValue) {
        if (_shouldPutValue(tag, key, value, defaultValue)) {
            tag.putString(key, value);
        }
    }

    public Vector3f getOptionalVector3f(String key, Vector3f defaultValue) {
        ListTag listNBT = tag.getList(key, 5);
        if (listNBT.size() >= 3) {
            return new Vector3f(listNBT.getFloat(0), listNBT.getFloat(1), listNBT.getFloat(2));
        }
        return defaultValue;
    }

    public void putOptionalVector3f(String key, Vector3f value, Vector3f defaultValue) {
        if (_shouldPutValue(tag, key, value, defaultValue)) {
            ListTag tags = ListTag.newInstance();
            tags.add(FloatTag.valueOf(value.getX()));
            tags.add(FloatTag.valueOf(value.getY()));
            tags.add(FloatTag.valueOf(value.getZ()));
            tag.put(key, tags);
        }
    }

    public BlockPos getOptionalBlockPos(String key, BlockPos defaultValue) {
        if (tag.contains(key, 4)) {
            return BlockPos.of(tag.getLong(key));
        }
        return defaultValue;
    }

    public void putOptionalBlockPos(String key, BlockPos value, BlockPos defaultValue) {
        if (_shouldPutValue(tag, key, value, defaultValue)) {
            tag.putLong(key, value.asLong());
        }
    }

    public Collection<BlockPos> getOptionalBlockPosArray(String key) {
        ArrayList<BlockPos> elements = new ArrayList<>();
        if (tag.contains(key, 12)) {
            for (long value : tag.getLongArray(key)) {
                elements.add(BlockPos.of(value));
            }
        }
        return elements;
    }

    public void putOptionalBlockPosArray(String key, Collection<BlockPos> elements) {
        if (_shouldPutValueArray(tag, key, elements)) {
            ArrayList<Long> list = new ArrayList<>(elements.size());
            for (BlockPos pos : elements) {
                list.add(pos.asLong());
            }
            tag.putLongArray(key, list);
        }
    }

    public Rectangle3i getOptionalRectangle3i(String key, Rectangle3i defaultValue) {
        ListTag listTag = tag.getList(key, 3);
        if (listTag.size() >= 6) {
            return new Rectangle3i(listTag.getInt(0), listTag.getInt(1), listTag.getInt(2), listTag.getInt(3), listTag.getInt(4), listTag.getInt(5));
        }
        return defaultValue;
    }

    public void putOptionalRectangle3i(String key, Rectangle3i value, Rectangle3i defaultValue) {
        if (_shouldPutValue(tag, key, value, defaultValue)) {
            ListTag tags = ListTag.newInstance();
            tags.add(IntTag.valueOf(value.getX()));
            tags.add(IntTag.valueOf(value.getY()));
            tags.add(IntTag.valueOf(value.getZ()));
            tags.add(IntTag.valueOf(value.getWidth()));
            tags.add(IntTag.valueOf(value.getHeight()));
            tags.add(IntTag.valueOf(value.getDepth()));
            tag.put(key, tags);
        }
    }


    public PaintColor getOptionalPaintColor(String key, PaintColor defaultValue) {
        if (tag != null && tag.contains(key, 3)) {
            return PaintColor.of(tag.getInt(key));
        }
        return defaultValue;
    }

    public void putOptionalPaintColor(String key, PaintColor value, PaintColor defaultValue) {
        if (_shouldPutValue(tag, key, value, defaultValue)) {
            tag.putInt(key, value.getRawValue());
        }
    }

    public ColorScheme getOptionalColorScheme(String key, ColorScheme defaultValue) {
        if (tag.contains(key, 10)) {
            return new ColorScheme(tag.getCompound(key));
        }
        return defaultValue;
    }

    public void putOptionalColorScheme(String key, ColorScheme value, ColorScheme defaultValue) {
        if (_shouldPutValue(tag, key, value, defaultValue)) {
            tag.put(key, value.serializeNBT());
        }
    }

    public SkinDescriptor getOptionalSkinDescriptor(String key) {
        CompoundTag parsedTag = _parseCompoundTag(tag, key);
        if (parsedTag != null && !parsedTag.isEmpty()) {
            return new SkinDescriptor(parsedTag);
        }
        return SkinDescriptor.EMPTY;
    }

    public void putOptionalSkinDescriptor(String key, SkinDescriptor value) {
        if (_shouldPutValue(tag, key, value, SkinDescriptor.EMPTY)) {
            tag.put(key, value.serializeNBT());
        }
    }

    public SkinProperties getOptionalSkinProperties(String key) {
        SkinProperties properties = new SkinProperties();
        if (tag.contains(key, 10)) {
            properties.readFromNBT(tag.getCompound(key));
        }
        return properties;
    }

    public void putOptionalSkinProperties(String key, SkinProperties properties) {
        if (_shouldPutValue(tag, key, properties, SkinProperties.EMPTY)) {
            CompoundTag propertiesTag = CompoundTag.newInstance();
            properties.writeToNBT(propertiesTag);
            tag.put(key, propertiesTag);
        }
    }


    public SkinOptions getOptionalSkinOptions(String key, SkinOptions defaultValue) {
        if (tag.contains(key, 10)) {
            CompoundTag nbt1 = tag.getCompound(key);
            if (!nbt1.isEmpty()) {
                return new SkinOptions(nbt1);
            }
        }
        return defaultValue;
    }

    public void putOptionalSkinOptions(String key, SkinOptions value, SkinOptions defaultValue) {
        if (_shouldPutValue(tag, key, value, defaultValue)) {
            tag.put(key, value.serializeNBT());
        }
    }


    private static <T> boolean _shouldPutValue(CompoundTag tag, String key, T value, T defaultValue) {
        if (tag == null || key == null) {
            return false;
        }
        if (value == null || value.equals(defaultValue)) {
            tag.remove(key);
            return false;
        }
        return true;
    }

    private static <T> boolean _shouldPutValueArray(CompoundTag tag, String key, Collection<T> value) {
        if (tag == null || key == null) {
            return false;
        }
        if (value == null || value.isEmpty()) {
            tag.remove(key);
            return false;
        }
        return true;
    }

    private static CompoundTag _parseCompoundTag(CompoundTag tag, String key) {
        if (tag.contains(key, 10)) {
            return tag.getCompound(key);
        }
        if (tag.contains(key, 8)) {
            return SkinFileUtils.readNBT(tag.getString(key));
        }
        return null;
    }
}
