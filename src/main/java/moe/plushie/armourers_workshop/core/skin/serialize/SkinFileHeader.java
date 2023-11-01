package moe.plushie.armourers_workshop.core.skin.serialize;

import moe.plushie.armourers_workshop.api.skin.ISkinFileHeader;
import moe.plushie.armourers_workshop.api.skin.ISkinType;
import moe.plushie.armourers_workshop.core.skin.SkinProperties;

public class SkinFileHeader implements ISkinFileHeader {

    private final int version;
    private final ISkinType type;
    private final SkinProperties properties;

    private int lastModified = 0;

    public SkinFileHeader(int version, ISkinType type, SkinProperties properties) {
        this.version = version;
        this.type = type;
        this.properties = properties;
    }

    public static SkinFileHeader of(int version, ISkinType type, SkinProperties properties) {
        return new SkinFileHeader(version, type, properties);
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public ISkinType getType() {
        return type;
    }

    @Override
    public SkinProperties getProperties() {
        return properties;
    }

    public void setLastModified(int lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public int getLastModified() {
        return lastModified;
    }
}
