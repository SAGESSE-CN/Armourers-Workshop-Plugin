package moe.plushie.armourers_workshop.plugin.helper;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.jetbrains.annotations.Nullable;

public class GameProfileHelper {

    @Nullable
    public static String getTextureFromTag(@Nullable CompoundTag tag) {
        if (tag == null || !tag.contains("SkullOwner", 10)) {
            return null;
        }
        CompoundTag gameProfile = tag.getCompound("SkullOwner");
        if (gameProfile.contains("Name", 8)) {
            return null; // is a normal block.
        }
        ListTag textures = gameProfile.getCompound("Properties").getList("textures", 10);
        if (textures.isEmpty()) {
            return null;
        }
        return ((CompoundTag)textures.get(0)).getString("Value");
    }

    @Nullable
    public static String getTextureFromProfile(@Nullable GameProfile gameProfile) {
        if (gameProfile == null) {
            return null;
        }
        if (gameProfile.getName() != null) {
            return null; // is a normal block.
        }
        Property property = Iterables.getFirst(gameProfile.getProperties().get("textures"), null);
        if (property == null) {
            return null;
        }
        return property.getValue();
    }
}
