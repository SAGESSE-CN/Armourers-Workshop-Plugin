package moe.plushie.armourers_workshop.library.data;


import moe.plushie.armourers_workshop.api.library.ISkinLibrary;
import moe.plushie.armourers_workshop.api.library.ISkinLibraryListener;
import moe.plushie.armourers_workshop.core.data.DataDomain;
import moe.plushie.armourers_workshop.core.data.LocalDataService;
import moe.plushie.armourers_workshop.core.network.NetworkManager;
import moe.plushie.armourers_workshop.init.ModConfig;
import moe.plushie.armourers_workshop.init.ModLog;
import moe.plushie.armourers_workshop.init.platform.EnvironmentManager;
import moe.plushie.armourers_workshop.library.network.UpdateLibraryFilesPacket;
import moe.plushie.armourers_workshop.utils.Constants;
import net.cocoonmc.core.world.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public abstract class SkinLibraryManager implements ISkinLibraryListener {

    protected final ArrayList<ISkinLibraryListener> listeners = new ArrayList<>();

    public static Server getServer() {
        return Server.INSTANCE;
    }

    public abstract void start();

    public abstract void stop();

    public void addListener(ISkinLibraryListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(ISkinLibraryListener listener) {
        this.listeners.remove(listener);
    }

    public boolean shouldDownloadFile(Player player) {
        return true;
    }

    public boolean shouldUploadFile(Player player) {
        return true;
    }

    @Override
    public void libraryDidReload(ISkinLibrary library) {
        listeners.forEach(listener -> listener.libraryDidReload(library));
    }

    @Override
    public void libraryDidChanges(ISkinLibrary library, ISkinLibrary.Difference difference) {
        listeners.forEach(listener -> listener.libraryDidChanges(library, difference));
    }

    public static class Server extends SkinLibraryManager {

        private static final Server INSTANCE = new Server();

        private final SkinLibrary skinLibrary;
        private final ArrayList<SkinLibraryFile> publicFiles = new ArrayList<>();
        private final HashMap<String, ArrayList<SkinLibraryFile>> privateFiles = new HashMap<>();
        private final HashSet<String> syncedPlayers = new HashSet<>();

        private int version = 0;
        private boolean isReady = false;

        public Server() {
            this.skinLibrary = new SkinLibrary(DataDomain.DEDICATED_SERVER, EnvironmentManager.getSkinLibraryDirectory());
            this.skinLibrary.addListener(this);
        }

        @Override
        public void start() {
            this.skinLibrary.markBaseDir();
            this.skinLibrary.reload();
        }

        @Override
        public void stop() {
            this.skinLibrary.reset();
            this.publicFiles.clear();
            this.privateFiles.clear();
            this.syncedPlayers.clear();
            this.version = 0;
            this.isReady = false;
        }

        public void remove(Player player) {
            ModLog.debug("remove synced player {}", player.getStringUUID());
            this.syncedPlayers.remove(player.getStringUUID());
        }

        @Override
        public void libraryDidReload(ISkinLibrary library) {
            // analyze all files
            ArrayList<SkinLibraryFile> publicFiles = new ArrayList<>();
            HashMap<String, ArrayList<SkinLibraryFile>> privateFiles = new HashMap<>();
            for (SkinLibraryFile file : skinLibrary.getFiles()) {
                String path = file.getPath();
                if (path.startsWith(Constants.PRIVATE)) {
                    int index = path.indexOf('/', Constants.PRIVATE.length() + 1);
                    if (index >= 0) {
                        String key = path.substring(0, index);
                        privateFiles.computeIfAbsent(key, k -> new ArrayList<>()).add(file);
                    }
                } else {
                    publicFiles.add(file);
                }
            }
            // when the first load action, we must notify the user the skin library is reloaded.
            if (this.isReady && this.publicFiles.equals(publicFiles) && this.privateFiles.equals(privateFiles)) {
                return;
            }
            this.isReady = true;
            this.syncedPlayers.clear();
            this.publicFiles.clear();
            this.publicFiles.addAll(publicFiles);
            this.privateFiles.clear();
            this.privateFiles.putAll(privateFiles);
            this.version += 1;
            super.libraryDidReload(library);
        }

        public void sendTo(Player player) {
            if (!isReady) {
                return;
            }
            String uuid = player.getStringUUID();
            if (syncedPlayers.contains(uuid)) {
                return;
            }
            syncedPlayers.add(uuid);
            String key = Constants.PRIVATE + "/" + uuid;
            String name = player.getDisplayName();
            ArrayList<SkinLibraryFile> privateFiles = this.privateFiles.getOrDefault(key, new ArrayList<>());
            UpdateLibraryFilesPacket packet = new UpdateLibraryFilesPacket(publicFiles, privateFiles);
            NetworkManager.sendTo(packet, player);
            ModLog.debug("syncing library files {}/{} to '{}'.", publicFiles.size(), privateFiles.size(), name);
        }

        public SkinLibrary getLibrary() {
            return skinLibrary;
        }

        @Override
        public boolean shouldDownloadFile(Player player) {
//            if (!ModPermissions.SKIN_LIBRARY_SKIN_DOWNLOAD.accept(player)) {
//                return false;
//            }
            return ModConfig.Common.allowDownloadingSkins;
        }

        @Override
        public boolean shouldUploadFile(Player player) {
//            if (!ModPermissions.SKIN_LIBRARY_SKIN_UPLOAD.accept(player)) {
//                return false;
//            }
            return ModConfig.Common.allowUploadingSkins;
        }

        public boolean shouldModifierFile(Player player) {
            // super op can manage the public folder.
//            return ModConfig.Common.allowLibraryRemoteManage && player.hasPermissions(5);
            return false;
        }

        public LocalDataService getDatabaseLibrary() {
            return LocalDataService.getInstance();
        }

        public int getVersion() {
            return version;
        }

        public boolean isReady() {
            return isReady;
        }
    }
}
