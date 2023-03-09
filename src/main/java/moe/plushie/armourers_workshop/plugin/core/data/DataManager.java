package moe.plushie.armourers_workshop.plugin.core.data;

import moe.plushie.armourers_workshop.plugin.init.ModContext;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;

public class DataManager {

    private static DataManager INSTANCE;

    private final File rootPath;
    private final File dbPath;
    private final File wardrobePath;

    private DataManager(File rootPath) {
        this.rootPath = rootPath;
        this.dbPath = new File(rootPath, "skin-database");
        this.wardrobePath = new File(dbPath, "wardrobe");
        // ..
        ModContext.init(new File(rootPath, "data/ArmourersWorkshop.dat"));
    }

    public static DataManager getInstance() {
        return INSTANCE;
    }

    public static void start() {
        if (INSTANCE == null) {
            World world = Bukkit.getServer().getWorlds().get(0);
            INSTANCE = new DataManager(new File(world.getName()));
        }
    }

    public static void stop() {
        if (INSTANCE != null) {
            INSTANCE = null;
        }
    }

}
