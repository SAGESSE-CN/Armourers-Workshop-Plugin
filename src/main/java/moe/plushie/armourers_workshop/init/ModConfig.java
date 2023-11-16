package moe.plushie.armourers_workshop.init;

import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.core.world.entity.Player;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class ModConfig {

    public static class Common {

        // General
        public static int maxUndos = 100;
        public static int blockTaskRate = 10; // 10 blocks/t
        public static boolean lockDyesOnSkins = false;
        public static boolean instancedDyeTable = false;
        public static boolean enableProtocolCheck = true;
        public static int serverSkinSendRate = 4000;
        public static boolean enableServerCompressesSkins = true;
        public static int enableEmbeddedSkinRenderer = 0;
        public static int enableFirstPersonSkinRenderer = 0;
        public static boolean enableMatchingByItemId = true;

        // Wardrobe
        public static boolean wardrobeAllowOpening = true;
        public static boolean showWardrobeSkins = true;
        public static boolean showWardrobeOutfits = true;
        public static boolean showWardrobeDisplaySettings = true;
        public static boolean showWardrobeColorSettings = true;
        public static boolean showWardrobeDyeSetting = true;
        public static boolean showWardrobeContributorSetting = true;

        public static int prefersWardrobePlayerSlots = 3;
        public static int prefersWardrobeMobSlots = 3;
        public static int prefersWardrobeDropOnDeath = 0;

        // Library
        public static boolean extractOfficialSkins;
        public static boolean allowLibraryPreviews = true;
        public static boolean allowDownloadingSkins = false;
        public static boolean allowUploadingSkins = true;
        public static boolean allowLibraryRemoteManage = false;

        // Recipes
        public static boolean disableRecipes = false;
        public static boolean disableDollRecipe = false;
        public static boolean disableSkinningRecipes = false;
        public static boolean hideDollFromCreativeTabs = true;
        public static boolean hideGiantFromCreativeTabs = true;
        public static boolean enableRecoveringSkins = false;

        // Holiday events
        public static boolean disableAllHolidayEvents = false;

        // Entity skins
        public static int enitiySpawnWithSkinsChance = 75;
        public static int entityDropSkinChance = 10;
        public static String enitiySpawnSkinTargetPath = "/";

        // Cache
        public static int skinCacheExpireTime = 600;
        public static int skinCacheMaxSize = 2000;

        // Global Skin Library
        public static ArrayList<String> customSkinServerURLs = new ArrayList<>();

        // Overrides
        public static ArrayList<String> overrides = new ArrayList<>();
        public static ArrayList<String> disableMatchingItems = new ArrayList<>();

        public static boolean isGlobalSkinServer() {
            // if use specify a skin server, it a private skin server.
            return customSkinServerURLs.isEmpty();
        }

        public static boolean canOpenWardrobe(Entity target, Player operator) {
            if (!wardrobeAllowOpening) {
                return false;
            }
            if (operator.isCreative()) {
                return true;
            }
            // No wardrobe tabs are active.
            return showWardrobeSkins || showWardrobeOutfits || showWardrobeDisplaySettings || showWardrobeColorSettings || showWardrobeDyeSetting;
        }
    }
}
