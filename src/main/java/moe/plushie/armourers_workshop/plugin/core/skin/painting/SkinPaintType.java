package moe.plushie.armourers_workshop.plugin.core.skin.painting;


import moe.plushie.armourers_workshop.plugin.core.skin.SkinDyeType;
import moe.plushie.armourers_workshop.plugin.core.skin.color.TextureKey;
import net.cocoonmc.core.resources.ResourceLocation;

public class SkinPaintType {

    private static final TextureKey DEFAULT_TEXTURE = new TextureKey(0, 0, 1, 1, 256, 256);

    private final int id;
    private final int index;

    private SkinDyeType dyeType;
    private ResourceLocation registryName;
    private TextureKey texture = DEFAULT_TEXTURE;

    public SkinPaintType(int index, int id) {
        this.id = id;
        this.index = index;
    }

    public ResourceLocation getRegistryName() {
        return registryName;
    }

    public void setRegistryName(ResourceLocation registryName) {
        this.registryName = registryName;
    }

    public SkinPaintType setTexture(float u, float v) {
        this.texture = new TextureKey(u, v, 1, 1, 256, 256);
        return this;
    }

    public TextureKey getTexture() {
        return this.texture;
    }

    public SkinDyeType getDyeType() {
        return dyeType;
    }

    public SkinPaintType setDyeType(SkinDyeType dyeType) {
        this.dyeType = dyeType;
        return this;
    }

    public int getId() {
        return id;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SkinPaintType other = (SkinPaintType) obj;
        return id == other.id;
    }

    @Override
    public String toString() {
        return registryName.toString();
    }
}
