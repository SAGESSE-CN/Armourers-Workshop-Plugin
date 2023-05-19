package moe.plushie.armourers_workshop.plugin.helper;

import net.minecraftforge.fml.loading.FMLLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

public class ConfigHelper implements IMixinConfigPlugin {

    public String supportedVersion = "";
    public String nextSupportedVersion = "";

    private String minecraftVersion;

    private boolean isChecked = false;
    private boolean isValid = true;

    @Override
    public void onLoad(String s) {
        if (supportedVersion.isEmpty()) {
            isChecked = true;
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String s, String s1) {
        return isEnabled();
    }

    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }

    private boolean isEnabled() {
        getMinecraftVersion();
        if (isChecked) {
            return isValid;
        }
        try {
            int min = parseInt(supportedVersion, 10000);
            int max = parseInt(nextSupportedVersion, 99999);
            int cur = parseInt(getRefMapperConfig(), 10000);
            isValid = cur >= min && cur <= max;
        } catch (Exception ignored) {
            // ignored all exception.
        }
        isChecked = true;
        return isValid;
    }

    private String getMinecraftVersion() {
        if (minecraftVersion != null) {
            return minecraftVersion;
        }
        String mcVersion = lookupMinecraftVersion();
        String[] comps = mcVersion.split("\\.");
        String major = getVersion(comps, 0, 0);
        String minor = getVersion(comps, 1, 2);
        String patch = getVersion(comps, 2, 2);
        minecraftVersion = major + minor + patch;
        return minecraftVersion;
    }

    private String getVersion(String[] verse, int index, int limit) {
        StringBuilder value = new StringBuilder();
        if (index < verse.length) {
            value.append(verse[index]);
        }
        while (value.length() < limit) {
            value.insert(0, "0");
        }
        return value.toString();
    }

    private String lookupMinecraftVersion() {
        Class<?> clazz = FMLLoader.class;
        try {
            // only in 1.18+
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getName().equals("versionInfo")) {
                    Object value = method.invoke(clazz);
                    Method method1 = value.getClass().getMethod("mcVersion");
                    return (String) method1.invoke(value);
                }
            }
            // only in 1.16
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getName().equals("mcVersion")) {
                    field.setAccessible(true);
                    return (String) field.get(clazz);
                }
            }
        } catch (Exception e) {
            // ignore
            e.printStackTrace();
        }
        return "1.0.0";
    }

    private int parseInt(String value, int defaultValue) {
        if (value.isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (Exception ignored) {
            return defaultValue;
        }
    }
}
