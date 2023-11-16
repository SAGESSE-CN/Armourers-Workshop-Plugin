package moe.plushie.armourers_workshop.utils;

import com.google.common.collect.Lists;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.Block;
import moe.plushie.armourers_workshop.api.config.IConfigBuilder;
import moe.plushie.armourers_workshop.api.config.IConfigSpec;
import moe.plushie.armourers_workshop.api.config.IConfigValue;
import moe.plushie.armourers_workshop.init.ModConstants;
import moe.plushie.armourers_workshop.init.ModLog;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SimpleConfig implements IConfigSpec, IConfigBuilder {

    private String root = "";
    private GroupEntry rootGroup;

    private final String type;
    private final YamlDocument config;
    private final AtomicInteger changess = new AtomicInteger(0);
    private final ArrayList<Entry<?>> entries = new ArrayList<>();
    private final ArrayList<Runnable> listeners = new ArrayList<>();
    private final LinkedHashMap<String, Entry<Object>> values = new LinkedHashMap<>();

    public SimpleConfig(File configDir, String type) {
        this.type = type;
        try {
            this.config = YamlDocument.create(new File(configDir, String.format("%s-%s.yml", ModConstants.MOD_ID, type)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public IConfigValue<Boolean> define(String path, boolean defaultValue, String... description) {
        return put(new Entry<>(convertTo(path), defaultValue, Lists.newArrayList(description)));
    }

    @Override
    public IConfigValue<Integer> defineInRange(String path, int defaultValue, int minValue, int maxValue, String... description) {
        return put(new RangeEntry<>(convertTo(path), defaultValue, minValue, maxValue, Lists.newArrayList(description)));
    }

    @Override
    public IConfigValue<Double> defineInRange(String path, double defaultValue, double minValue, double maxValue, String... description) {
        return put(new RangeEntry<>(convertTo(path), defaultValue, minValue, maxValue, Lists.newArrayList(description)));
    }

    @Override
    public <T> IConfigValue<List<? extends T>> defineList(String path, List<? extends T> defaultValue, String... description) {
        return put(new ListEntry<>(convertTo(path), defaultValue, it -> true, Lists.newArrayList(description)));
    }

    @Override
    public <T> IConfigValue<List<? extends T>> defineList(String path, List<? extends T> defaultValue, Predicate<Object> elementValidator, String... description) {
        return put(new ListEntry<>(convertTo(path), defaultValue, it -> true, Lists.newArrayList(description)));
    }

    @Override
    public void defineCategory(String name, String description, Runnable runnable) {
        String oldRoot = root;
        GroupEntry oldGroup = rootGroup;
        GroupEntry newGroup = new GroupEntry(convertTo(name), Lists.newArrayList(description));
        entries.add(newGroup);
        root = root + name + ".";
        rootGroup = newGroup;
        rootGroup.create();
        runnable.run();
        rootGroup = oldGroup;
        root = oldRoot;
    }

    @Override
    public IConfigSpec build() {
        // if changed in the build stage, save all.
        if (this.changess.get() != 0) {
            this.save();
            this.changess.set(0);
        }
        return this;
    }

    @Override
    public IConfigBuilder builder() {
        return this;
    }

    @Override
    public Map<String, Object> snapshot() {
        HashMap<String, Object> fields = new HashMap<>();
        values.forEach((key, value) -> {
            if (value.getter != null) {
                fields.put(key, value.getter.get());
            }
        });
        return fields;
    }

    @Override
    public void apply(Map<String, Object> snapshot) {
        ModLog.debug("apply {} snapshot from server", type);
        snapshot.forEach((key, object) -> {
            Entry<Object> value = values.get(key);
            if (value != null && value.setter != null) {
                value.setter.accept(object);
            }
        });
        this.setChanged();
    }

    @Override
    public void reload() {
        ModLog.debug("apply {} changes from spec", type);
        // reload from config file.
        try {
            this.config.reload();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.values.forEach((key, value) -> {
            if (value.setter != null) {
                value.setter.accept(value.read());
            }
        });
        this.setChanged();
    }

    @Override
    public void save() {
        ModLog.debug("save {} changes into spec", type);
        this.values.forEach((key, value) -> {
            if (value.getter != null) {
                value.write(value.getter.get());
            }
        });
        // write to config file.
        try {
            this.config.save();
            this.setChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notify(Runnable action) {
        this.listeners.add(action);
    }

    private void setChanged() {
        // when the config did changes, we need notify to all listeners.
        this.listeners.forEach(Runnable::run);
    }

    private String convertTo(String name) {
        return root + name;
    }

    private Collection<String> appendRange(Collection<String> description, Object minValue, Object maxValue) {
        ArrayList<String> list = new ArrayList<>(description);
        list.add("Range: " + minValue + " ~ " + maxValue);
        return list;
    }

    @SuppressWarnings("unchecked")
    private <T> Entry<T> put(Entry<T> value) {
        value.create();
        values.put(value.path, (Entry<Object>) value);
        entries.add(value);
        return value;
    }


    public class Entry<T> implements IConfigValue<T> {

        protected Consumer<T> setter;
        protected Supplier<T> getter;

        protected final String path;
        protected final T defaultValue;
        protected final Collection<String> description;

        protected Entry(String path, T defaultValue, Collection<String> description) {
            this.path = path;
            this.defaultValue = defaultValue;
            this.description = description;
        }

        @Override
        public T read() {
            // read from file
            Object value = config.get(path);
            if (value != null) {
                // noinspection unchecked
                return (T) value;
            }
            return defaultValue;
        }

        @Override
        public void write(T value) {
            config.set(path, value);
            changess.incrementAndGet();
        }

        @Override
        public void bind(Consumer<T> setter, Supplier<T> getter) {
            this.setter = setter;
            this.getter = getter;
        }

        protected void create() {
            Block<?> block = config.getBlock(path);
            if (block != null) {
                return;
            }
            config.set(path, defaultValue);
            block = config.getBlock(path);
            if (block == null) {
                return;
            }
            block.setComments(new ArrayList<>(description));
            changess.incrementAndGet();
        }
    }

    public class RangeEntry<T extends Comparable<? super T>> extends Entry<T> {

        private final T minValue;
        private final T maxValue;

        private RangeEntry(String path, T defaultValue, T minValue, T maxValue, Collection<String> description) {
            super(path, defaultValue, appendRange(description, minValue, maxValue));
            this.minValue = minValue;
            this.maxValue = maxValue;
        }

        @Override
        public T read() {
            return clamp(super.read());
        }

        @Override
        public void write(T value) {
            super.write(clamp(value));
        }

        private T clamp(T value) {
            if (minValue.compareTo(value) > 0) {
                return minValue;
            }
            if (maxValue.compareTo(value) < 0) {
                return maxValue;
            }
            return value;
        }
    }

    public class ListEntry<T> extends Entry<List<? extends T>> {

        private ListEntry(String path, List<? extends T> defaultValue, Predicate<? extends T> elementValidator, Collection<String> description) {
            super(path, defaultValue, description);
        }
    }

    public class GroupEntry extends Entry<Void> {

        private GroupEntry(String path, Collection<String> description) {
            super(path, null, description);
        }

        @Override
        public void create() {
            Block<?> section = config.getSection(path);
            if (section != null) {
                return;
            }
            section = config.createSection(path);
            if (section == null) {
                return;
            }
            section.setComments(new ArrayList<>(description));
            changess.incrementAndGet();
        }
    }
}
