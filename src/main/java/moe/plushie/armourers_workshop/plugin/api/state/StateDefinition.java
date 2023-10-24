package moe.plushie.armourers_workshop.plugin.api.state;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import moe.plushie.armourers_workshop.plugin.api.state.properties.Property;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class StateDefinition<O, S extends StateHolder<O, S>> {

    static final Pattern NAME_PATTERN = Pattern.compile("^[a-z0-9_]+$");

    private final O owner;
    private final ImmutableSortedMap<String, Property<?>> propertiesByName;
    private final ImmutableList<S> states;

    protected StateDefinition(Function<O, S> function, O object, Factory<O, S> factory, Map<String, Property<?>> map) {
        this.owner = object;
        this.propertiesByName = ImmutableSortedMap.copyOf(map);
        LinkedHashMap<Map<Property<?>, Comparable<?>>, S> stateMap = Maps.newLinkedHashMap();
        ArrayList<S> stateList = new ArrayList<>();
        Stream<List<Pair<Property<?>, Comparable<?>>>> stream = Stream.of(Collections.emptyList());
        for (Property<?> property : propertiesByName.values()) {
            stream = stream.flatMap(list -> property.getPossibleValues().stream().map(comparable -> {
                ArrayList<Pair<Property<?>, Comparable<?>>> list2 = Lists.newArrayList(list);
                list2.add(Pair.of(property, comparable));
                return list2;
            }));
        }
        stream.forEach(list2 -> {
            ImmutableMap<Property<?>, Comparable<?>> key = list2.stream().collect(ImmutableMap.toImmutableMap(Pair::getFirst, Pair::getSecond));
            S state = factory.create(object, key);
            stateMap.put(key, state);
            stateList.add(state);
        });
        stateList.forEach(it -> it.populateNeighbours(stateMap));
        this.states = ImmutableList.copyOf(stateList);
    }

    public S any() {
        return this.states.get(0);
    }

    public interface Factory<O, S> {
        S create(O var1, ImmutableMap<Property<?>, Comparable<?>> var2);
    }

    public static class Pair<F, S> {

        private final F first;
        private final S second;

        public Pair(final F first, final S second) {
            this.first = first;
            this.second = second;
        }

        public static <F, S> Pair<F, S> of(final F first, final S second) {
            return new Pair<>(first, second);
        }

        public F getFirst() {
            return first;
        }

        public S getSecond() {
            return second;
        }
    }

    public static class Builder<O, S extends StateHolder<O, S>> {

        private final O owner;
        private final Map<String, Property<?>> properties = Maps.newHashMap();

        public Builder(O object) {
            this.owner = object;
        }

        public Builder<O, S> add(Property<?>... propertys) {
            for (Property<?> property : propertys) {
                this.validateProperty(property);
                this.properties.put(property.getName(), property);
            }
            return this;
        }

        private <T extends Comparable<T>> void validateProperty(Property<T> property) {
            String string = property.getName();
            if (!NAME_PATTERN.matcher(string).matches()) {
                throw new IllegalArgumentException(this.owner + " has invalidly named property: " + string);
            }
            Collection<T> values = property.getPossibleValues();
            if (values.size() <= 1) {
                throw new IllegalArgumentException(this.owner + " attempted use property " + string + " with <= 1 possible values");
            }
            for (T value : values) {
                String string2 = property.getName(value);
                if (NAME_PATTERN.matcher(string2).matches()) continue;
                throw new IllegalArgumentException(owner + " has property: " + string + " with invalidly named value: " + string2);
            }
            if (this.properties.containsKey(string)) {
                throw new IllegalArgumentException(owner + " has duplicate property: " + string);
            }
        }

        public StateDefinition<O, S> create(Function<O, S> function, Factory<O, S> factory) {
            return new StateDefinition<>(function, owner, factory, properties);
        }
    }
}

