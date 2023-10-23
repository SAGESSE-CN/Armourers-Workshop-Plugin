package moe.plushie.armourers_workshop.plugin.api;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class Component {

    private final BaseComponent contents;

    public Component(BaseComponent contents) {
        this.contents = contents;
    }

    public static Component literal(String text) {
        return new Component(new TextComponent(text));
    }

    public static Component translatable(String key, Object... objects) {
        Object[] objects1 = new Object[objects.length];
        for (int i = 0; i < objects.length; ++i) {
            Object o = objects[i];
            if (o instanceof Component) {
                o = ((Component) o).contents;
            }
            objects1[i] = o;
        }
        TranslatableComponent component = new TranslatableComponent(key, objects1);
        return new Component(component);
    }

    public static Component deserialize(String json) {
        BaseComponent[] components = ComponentSerializer.parse(json);
        if (components != null && components.length != 0) {
            return new Component(components[0]);
        }
        return new Component(new TextComponent(""));
    }

    public String serialize() {
        return ComponentSerializer.toString(contents);
    }

    public BaseComponent getContents() {
        return contents;
    }
}
