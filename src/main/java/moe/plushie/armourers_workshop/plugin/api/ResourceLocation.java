package moe.plushie.armourers_workshop.plugin.api;

import org.bukkit.NamespacedKey;

import java.util.Objects;

public class ResourceLocation {

    protected final String namespace;
    protected final String path;

    public ResourceLocation(NamespacedKey key) {
        this(key.getNamespace(), key.getKey());
    }

    public ResourceLocation(String id) {
        this(NamespacedKey.fromString(id, null));
    }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResourceLocation)) return false;
        ResourceLocation that = (ResourceLocation) o;
        return Objects.equals(namespace, that.namespace) && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, path);
    }

    @Override
    public String toString() {
        return namespace + ":" + path;
    }
}
