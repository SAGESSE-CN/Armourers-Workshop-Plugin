package moe.plushie.armourers_workshop.plugin.api;

public class TextComponent {

    public final String value;

    public TextComponent(String value) {
        this.value = value;
    }

    public static TextComponent literal(String text) {
        return new TextComponent(text);
    }

    public static TextComponent translatable(String key) {
        // "{\"translate\":\"inventory.armourers_workshop.wardrobe\"}";
        return new TextComponent("{\"translate\":\"" + key + "\"}");
    }

}
