package moe.plushie.armourers_workshop.plugin.api;

public class ResourceLocation {

    protected final String namespace;
    protected final String path;


    public ResourceLocation(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return namespace + ":" + path;
    }
}
