package moe.plushie.armourers_workshop.plugin.api.state.properties;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EnumProperty<T extends Enum<T>> extends Property<T> {

    private final ImmutableSet<T> values;
    private final HashMap<String, T> names = new HashMap<>();

    protected EnumProperty(String name, Class<T> clazz, Collection<T> values) {
        super(name, clazz);
        this.values = ImmutableSet.copyOf(values);
        for (T value : values) {
            String valueName = value.name().toLowerCase();
            if (this.names.containsKey(valueName)) {
                throw new IllegalArgumentException("Multiple values have the same name '" + valueName + "'");
            }
            this.names.put(valueName, value);
        }
    }

    public static <T extends Enum<T>> EnumProperty<T> create(String name, Class<T> clazz) {
        return EnumProperty.create(name, clazz, (T enum_) -> true);
    }

    public static <T extends Enum<T>> EnumProperty<T> create(String name, Class<T> clazz, Predicate<T> predicate) {
        return new EnumProperty<>(name, clazz, Arrays.stream(clazz.getEnumConstants()).filter(predicate).collect(Collectors.toList()));
    }

    public static <T extends Enum<T>> EnumProperty<T> create(String name, Class<T> clazz, T ... enums) {
        return EnumProperty.create(name, clazz, Lists.newArrayList(enums));
    }

    public static <T extends Enum<T>> EnumProperty<T> create(String name, Class<T> clazz, Collection<T> collection) {
        return new EnumProperty<T>(name, clazz, collection);
    }

    @Override
    public String getName(T value) {
        return value.name().toLowerCase();
    }

    @Override
    public Optional<T> getValue(String name) {
        return Optional.ofNullable(names.get(name));
    }

    @Override
    public Collection<T> getPossibleValues() {
        return this.values;
    }
}
