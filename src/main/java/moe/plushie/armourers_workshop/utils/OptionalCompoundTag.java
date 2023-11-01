package moe.plushie.armourers_workshop.utils;

import moe.plushie.armourers_workshop.core.skin.color.PaintColor;
import net.cocoonmc.core.math.Vector3f;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.nbt.FloatTag;
import net.cocoonmc.core.nbt.ListTag;

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
}
