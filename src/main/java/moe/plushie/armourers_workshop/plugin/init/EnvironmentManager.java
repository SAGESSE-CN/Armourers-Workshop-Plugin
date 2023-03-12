package moe.plushie.armourers_workshop.plugin.init;

import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;

public class EnvironmentManager {

    public static File getRootDirectory() {
        return new File("armourers_workshop");
    }

    public static File getSkinLibraryDirectory() {
        return new File(getRootDirectory(), "skin-library");
    }

    public static File getSkinCacheDirectory() {
        return new File(getRootDirectory(), "skin-cache");
    }

    public static File getSkinDatabaseDirectory() {
        World world = Bukkit.getServer().getWorlds().get(0);
        return new File(world.getWorldFolder(), "skin-database");
    }
}
