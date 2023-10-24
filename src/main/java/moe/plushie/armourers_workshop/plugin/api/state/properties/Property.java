package moe.plushie.armourers_workshop.plugin.api.state.properties;

import java.util.Collection;
import java.util.Optional;

public abstract class Property<T extends Comparable<T>> {

    private final String name;
    private final Class<T> clazz;

    protected Property(String name, Class<T> clazz) {
        this.clazz = clazz;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public abstract String getName(T var1);

    public abstract Optional<T> getValue(String var1);

    public Class<T> getValueClass() {
        return this.clazz;
    }

    public abstract Collection<T> getPossibleValues();
}

