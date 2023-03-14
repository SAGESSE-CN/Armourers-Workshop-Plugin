package moe.plushie.armourers_workshop.plugin.core.data;

import moe.plushie.armourers_workshop.plugin.init.platform.EnvironmentManager;
import moe.plushie.armourers_workshop.plugin.init.ModContext;
import moe.plushie.armourers_workshop.plugin.init.ModLog;
import moe.plushie.armourers_workshop.plugin.utils.SkinFileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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

    public InputStream loadSkinData(String identifier) throws IOException {
        ModLog.debug("'{}' => get skin input stream from data manager", identifier);
        if (DataDomain.isDatabase(identifier)) {
            String path = DataDomain.getPath(identifier);
            return LocalDataService.getInstance().getFile(path);
        } else {
            String path = SkinFileUtils.normalize(DataDomain.getPath(identifier));
            return loadStreamFromPath(path);
        }
    }

//    public void loadSkinData(String identifier, IResultHandler<InputStream> handler) {
//        executor.submit(() -> {
//            try {
//                handler.accept(loadSkinData(identifier));
//            } catch (Exception exception) {
//                handler.reject(exception);
//            }
//        });
//    }

    private InputStream loadStreamFromPath(String identifier) throws IOException {
        File file = new File(EnvironmentManager.getSkinLibraryDirectory(), identifier);
        if (file.exists()) {
            return new FileInputStream(file);
        }
        file = new File(EnvironmentManager.getSkinLibraryDirectory(), identifier + ".armour");
        if (file.exists()) {
            return new FileInputStream(file);
        }
        throw new FileNotFoundException(identifier);
    }

//    private File getSkinCacheFile(String identifier) {
//        File rootPath = getSkinCacheDirectory();
//        if (rootPath != null) {
//            String namespace = DataDomain.getNamespace(identifier);
//            String path = DataDomain.getPath(identifier);
//            return new File(rootPath, namespace + "/" + ModContext.md5(path) + ".dat");
//        }
//        return null;
//    }
//
//    private File getSkinCacheDirectory() {
//        UUID t0 = ModContext.t0();
//        if (t0 != null) {
//            return new File(AWCore.getSkinCacheDirectory(), t0.toString());
//        }
//        return null;
//    }
}
