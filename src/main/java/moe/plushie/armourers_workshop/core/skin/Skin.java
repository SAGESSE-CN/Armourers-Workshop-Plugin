package moe.plushie.armourers_workshop.core.skin;

import moe.plushie.armourers_workshop.api.skin.ISkinType;

public class Skin {

    public String path;

    private ISkinType type;
    private SkinProperties properties;

    private byte[] bytes;

    public Skin(ISkinType type, SkinProperties properties, byte[] bytes) {
        this.type = type;
        this.properties = properties;
        this.bytes = bytes;
    }

    public ISkinType getType() {
        return type;
    }

    public SkinProperties getProperties() {
        return properties;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
