package moe.plushie.armourers_workshop.plugin.core.skin;

import moe.plushie.armourers_workshop.plugin.api.IDataInputStream;
import moe.plushie.armourers_workshop.plugin.api.IDataOutputStream;
import moe.plushie.armourers_workshop.plugin.utils.ObjectUtils;
import net.querz.nbt.tag.ByteTag;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.DoubleTag;
import net.querz.nbt.tag.FloatTag;
import net.querz.nbt.tag.IntTag;
import net.querz.nbt.tag.StringTag;
import net.querz.nbt.tag.Tag;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SkinProperties {

    public static final SkinProperties EMPTY = new SkinProperties();

    protected LinkedHashMap<String, Object> properties;

    public SkinProperties() {
        properties = new LinkedHashMap<>();
    }

    public SkinProperties(SkinProperties skinProps) {
        properties = new LinkedHashMap<>(skinProps.properties);
    }

    public static SkinProperties create() {
        return new SkinProperties();
    }

    public static SkinProperties create(SkinProperties properties) {
        return new SkinProperties(properties);
    }

    public static SkinProperties create(SkinProperties properties, int skinIndex) {
        return new Stub(properties, skinIndex);
    }

    public boolean isEmpty() {
        return properties.isEmpty();
    }

    public <T> T get(SkinProperty<T> property) {
        Object value = properties.getOrDefault(property.getKey(), property.getDefaultValue());
        return ObjectUtils.unsafeCast(value);
    }

    public <T> void put(SkinProperty<T> property, T value) {
        if (Objects.equals(value, property.getDefaultValue())) {
            properties.remove(property.getKey());
        } else {
            properties.put(property.getKey(), value);
        }
    }

    public <T> void remove(SkinProperty<T> property) {
        properties.remove(property.getKey());
    }

    public <T> boolean containsKey(SkinProperty<T> property) {
        return properties.containsKey(property.getKey());
    }

    public <T> boolean containsValue(SkinProperty<T> property) {
        return properties.containsValue(property.getKey());
    }

    public void put(String key, Object value) {
        if (value == null) {
            properties.remove(key);
        } else {
            properties.put(key, value);
        }
    }

    public Set<Map.Entry<String, Object>> entrySet() {
        return properties.entrySet();
    }

    public ArrayList<String> getPropertiesList() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < properties.size(); i++) {
            String key = (String) properties.keySet().toArray()[i];
            list.add(key + ":" + properties.get(key));
        }
        return list;
    }

    public void copyFrom(SkinProperties properties) {
        this.properties = new LinkedHashMap<>(properties.properties);
    }

    public void writeToStream(IDataOutputStream stream) throws IOException {
        stream.writeInt(properties.size());
        for (int i = 0; i < properties.size(); i++) {
            String key = (String) properties.keySet().toArray()[i];
            Object value = properties.get(key);
            stream.writeString(key);
            if (value instanceof String) {
                stream.writeByte(DataTypes.STRING.ordinal());
                stream.writeString((String) value);
            }
            if (value instanceof Integer) {
                stream.writeByte(DataTypes.INT.ordinal());
                stream.writeInt((Integer) value);
            }
            if (value instanceof Double) {
                stream.writeByte(DataTypes.DOUBLE.ordinal());
                stream.writeDouble((Double) value);
            }
            if (value instanceof Boolean) {
                stream.writeByte(DataTypes.BOOLEAN.ordinal());
                stream.writeBoolean((Boolean) value);
            }
        }
    }

    public void readFromStream(IDataInputStream stream) throws IOException {
        int count = stream.readInt();
        for (int i = 0; i < count; i++) {
            String key = stream.readString();
            int byteType = stream.readByte();
            DataTypes type = DataTypes.byId(byteType);
            if (type == null) {
                throw new IOException("Error loading skin properties " + byteType);
            }

            Object value = null;
            switch (type) {
                case STRING:
                    value = stream.readString();
                    break;
                case INT:
                    value = stream.readInt();
                    break;
                case DOUBLE:
                    value = stream.readDouble();
                    break;
                case BOOLEAN:
                    value = stream.readBoolean();
                    break;
            }
            properties.put(key, value);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SkinProperties)) return false;
        SkinProperties that = (SkinProperties) o;
        return properties.equals(that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(properties);
    }

    @Override
    public String toString() {
        return "SkinProperties [properties=" + properties + "]";
    }

    public void readFromNBT(CompoundTag nbt) {
        for (String key : nbt.keySet()) {
            Tag<?> value = nbt.get(key);
            if (value instanceof StringTag) {
                properties.put(key, ((StringTag) value).getValue());
            } else if (value instanceof IntTag) {
                properties.put(key, ((IntTag) value).asInt());
            } else if (value instanceof FloatTag) {
                properties.put(key, ((FloatTag) value).asFloat());
            } else if (value instanceof DoubleTag) {
                properties.put(key, ((DoubleTag) value).asDouble());
            } else if (value instanceof ByteTag) {
                properties.put(key, ((ByteTag) value).asByte() != 0);
            }
        }
    }

    public void writeToNBT(CompoundTag nbt) {
        properties.forEach((key, value) -> {
            if (value instanceof String) {
                nbt.putString(key, (String) value);
            } else if (value instanceof Integer) {
                nbt.putInt(key, (int) value);
            } else if (value instanceof Float) {
                nbt.putDouble(key, (float) value);
            } else if (value instanceof Double) {
                nbt.putDouble(key, (double) value);
            } else if (value instanceof Boolean) {
                nbt.putBoolean(key, (boolean) value);
            }
        });
    }

    public enum DataTypes {
        STRING, INT, DOUBLE, BOOLEAN;

        @Nullable
        public static DataTypes byId(int id) {
            if (id >= 0 & id < DataTypes.values().length) {
                return DataTypes.values()[id];
            }
            return null;
        }
    }

    public static class Stub extends SkinProperties {
        private final int index;

        public Stub(SkinProperties paranet, int index) {
            super();
            this.properties = paranet.properties;
            this.index = index;
        }

        @Override
        public <T> void put(SkinProperty<T> property, T value) {
            String indexedKey = getResolvedKey(property);
            if (indexedKey != null) {
                properties.put(indexedKey, value);
            } else {
                properties.put(property.getKey(), value);
            }
        }

        @Override
        public <T> void remove(SkinProperty<T> property) {
            String indexedKey = getResolvedKey(property);
            if (indexedKey != null) {
                properties.remove(indexedKey);
            } else {
                properties.remove(property.getKey());
            }
        }

        @Override
        public <T> T get(SkinProperty<T> property) {
            String indexedKey = getResolvedKey(property);
            Object value;
            if (indexedKey != null && properties.containsKey(indexedKey)) {
                value = properties.getOrDefault(indexedKey, property.getDefaultValue());
            } else {
                value = properties.getOrDefault(property.getKey(), property.getDefaultValue());
            }
            return ObjectUtils.unsafeCast(value);
        }

        @Override
        public <T> boolean containsKey(SkinProperty<T> property) {
            String indexedKey = getResolvedKey(property);
            if (indexedKey != null && properties.containsKey(indexedKey)) {
                return true;
            }
            return properties.containsKey(property.getKey());
        }

        @Override
        public <T> boolean containsValue(SkinProperty<T> property) {
            String indexedKey = getResolvedKey(property);
            if (indexedKey != null && properties.containsValue(indexedKey)) {
                return true;
            }
            return properties.containsValue(property.getKey());
        }

        @Nullable
        private <T> String getResolvedKey(SkinProperty<T> property) {
            if (property != null) {
                if (property.isMultipleKey()) {
                    return property.getKey() + index;
                }
            }
            return null;
        }
    }
}
