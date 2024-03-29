package moe.plushie.armourers_workshop.core.texture;

import moe.plushie.armourers_workshop.init.ModItems;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class PlayerTextureDescriptor {

    public static final PlayerTextureDescriptor EMPTY = new PlayerTextureDescriptor();

//    private final static Cache<ItemStack, PlayerTextureDescriptor> DESCRIPTOR_CACHES = CacheBuilder.newBuilder()
//            .maximumSize(8)
//            .expireAfterAccess(15, TimeUnit.SECONDS)
//            .build();

    private Source source;
    private String url;
//    private GameProfile profile;

    public PlayerTextureDescriptor() {
        this.source = Source.NONE;
    }

    public PlayerTextureDescriptor(String url) {
        this.source = Source.URL;
        this.url = url;
    }

//    public PlayerTextureDescriptor(GameProfile profile) {
//        this.source = Source.USER;
//        this.profile = profile;
//    }

    public PlayerTextureDescriptor(@Nullable CompoundTag nbt) {
//        if (nbt != null && nbt.contains(Constants.Key.TEXTURE_URL, Constants.TagFlags.STRING)) {
//            this.source = Source.URL;
//            this.url = nbt.getString(Constants.Key.TEXTURE_URL);
//        }
//        if (nbt != null && nbt.contains(Constants.Key.TEXTURE_PROFILE, Constants.TagFlags.COMPOUND)) {
//            this.source = Source.USER;
//            this.profile = nbt.getOptionalGameProfile(Constants.Key.TEXTURE_PROFILE, null);
//        }
//        if (this.url == null && this.profile == null) {
//            this.source = Source.NONE;
//        }
    }

    public static PlayerTextureDescriptor by(String userName) {
        return EMPTY;
    }

    public static PlayerTextureDescriptor of(ItemStack itemStack) {
        if (!itemStack.is(ModItems.MANNEQUIN.get())) {
            return EMPTY;
        }
//        CompoundTag entityTag = itemStack.getTagElement(Constants.Key.ENTITY);
//        if (entityTag == null || !entityTag.contains(Constants.Key.ENTITY_TEXTURE, Constants.TagFlags.COMPOUND)) {
//            return EMPTY;
//        }
//        PlayerTextureDescriptor descriptor = DESCRIPTOR_CACHES.getIfPresent(itemStack);
//        if (descriptor != null) {
//            return descriptor;
//        }
//        descriptor = new PlayerTextureDescriptor(entityTag.getCompound(Constants.Key.ENTITY_TEXTURE));
//        DESCRIPTOR_CACHES.put(itemStack, descriptor);
//        return descriptor;
        return EMPTY;
    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = CompoundTag.newInstance();
        if (url != null) {
//            nbt.putString(Constants.Key.TEXTURE_URL, url);
        }
//        if (profile != null) {
//            nbt.putOptionalGameProfile(Constants.Key.TEXTURE_PROFILE, profile, null);
//        }
//        return nbt;
        return nbt;
    }

    public boolean isEmpty() {
        return source == Source.NONE;
    }

    @Nullable
    public String getURL() {
        return url;
    }

    @Nullable
    public String getName() {
//        if (profile != null) {
//            return profile.getName();
//        }
        return null;
    }

//    @Nullable
//    public GameProfile getProfile() {
//        return profile;
//    }

    public Source getSource() {
        return source;
    }

    public String getValue() {
        if (source == Source.URL) {
            return getURL();
        }
        if (source == Source.USER) {
            return getName();
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerTextureDescriptor that = (PlayerTextureDescriptor) o;
        return source == that.source && Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, getValue());
    }

    public enum Source {
        NONE,
        USER,
        URL
    }
}
