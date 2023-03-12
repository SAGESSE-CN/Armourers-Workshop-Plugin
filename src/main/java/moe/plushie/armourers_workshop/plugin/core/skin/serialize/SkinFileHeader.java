package moe.plushie.armourers_workshop.plugin.core.skin.serialize;

import moe.plushie.armourers_workshop.plugin.api.skin.ISkinType;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinProperties;

public class SkinFileHeader {

    private final int version;
    private final ISkinType type;
    private final SkinProperties properties;

    private int lastModified = 0;

    public SkinFileHeader(int version, ISkinType type, SkinProperties properties) {
        this.version = version;
        this.type = type;
        this.properties = properties;
    }

    public int getVersion() {
        return version;
    }

    public ISkinType getType() {
        return type;
    }

    public SkinProperties getProperties() {
        return properties;
    }

    public void setLastModified(int lastModified) {
        this.lastModified = lastModified;
    }

    public int getLastModified() {
        return lastModified;
    }
}
