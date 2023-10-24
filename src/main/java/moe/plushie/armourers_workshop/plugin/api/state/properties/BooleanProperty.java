package moe.plushie.armourers_workshop.plugin.api.state.properties;

import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.Optional;

public class BooleanProperty extends Property<Boolean> {

    private final ImmutableSet<Boolean> values = ImmutableSet.of(Boolean.valueOf(true), Boolean.valueOf(false));

    protected BooleanProperty(String string) {
        super(string, Boolean.class);
    }

    public static BooleanProperty create(String string) {
        return new BooleanProperty(string);
    }

    @Override
    public String getName(Boolean var1) {
        return var1.toString();
    }

    @Override
    public Optional<Boolean> getValue(String string) {
        if ("true".equals(string) || "false".equals(string)) {
            return Optional.of(Boolean.valueOf(string));
        }
        return Optional.empty();
    }

    @Override
    public Collection<Boolean> getPossibleValues() {
        return this.values;
    }
}
