package moe.plushie.armourers_workshop.plugin.init;

import moe.plushie.armourers_workshop.plugin.api.ItemOverrideType;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinSlotType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class ModConfig {

    private static final HashMap<String, Object> VALUES = new Builder().build();


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
        public static ArrayList<String> customGlobalSkinLibraryURLs = new ArrayList<>();
        public static boolean enablePrivateGlobalSkinLibrary = false;

        // Overrides
        public static ArrayList<String> overrides = new ArrayList<>();
        public static ArrayList<String> disableMatchingItems = new ArrayList<>();
    }


    public static void init() {
        // TODO: IMPL
    }


    public static HashMap<String, Object> snapshot() {
        return VALUES;
    }

    public static class Builder {

        String root = "";
        HashMap<String, Object> values = new HashMap<>();

        private void defineCategory(String name, String description, Runnable runnable) {
            String oldRoot = root;
            root = root + name + ".";
            runnable.run();
            root = oldRoot;
        }

        private void define(String path, boolean defaultValue, String... description) {
            put(path, defaultValue);
        }

        private void defineInRange(String path, int defaultValue, int min, int max, String... description) {
            put(path, defaultValue);
        }

        private void defineInRange(String path, double defaultValue, double min, double max, String... description) {
            put(path, defaultValue);
        }

        private <T> void defineList(String path, List<? extends T> defaultValue, String... description) {
            defineList(path, defaultValue, v -> true, description);
        }

        private <T> void defineList(String path, List<? extends T> defaultValue, Predicate<Object> elementValidator, String... description) {
            put(path, defaultValue);
        }

        private void put(String path, Object value) {
            values.put(root + path, value);
        }


        public HashMap<String, Object> build() {
            defineCategory("general", "General settings.", () -> {
                defineInRange("maxUndos", 100, 0, 1000, "Max number of undos a player has for block painting.");
                defineInRange("blockTaskRate", 10, 1, 1000, "Max number of processing blocks in per tick.");
                define("lockDyesOnSkins", false, "When enabled players will not be able to remove dyes from skins in the dye table.");
                define("instancedDyeTable", false, "If true the dye table will be instanced for each player. Items will be dropped when the table is closed.");
                define("enableProtocolCheck", true, "If enabled the server will check the client protocol version in the login.", "Highly recommended unless the server does not support handshake.");
                defineInRange("serverModelSendRate", 4000, 0, 8000, "The maximum number of skins the server is allow to send every minute.", "Less that 1 equals unlimited. (not recommended may cause bandwidth and cpu spikes on the server)");
                define("serverCompressesSkins", true, "If enabled the server will compress skins before sending them to clients.", "Highly recommended unless the server has a very slow CPU.");
                defineInRange("enableEmbeddedSkinRenderer", 0, 0, 2, "Using embedded skin renderer to replace the original item renderer.", "0 = use client config", "1 = always disable", "2 = always enable");
                defineInRange("enableFirstPersonSkinRenderer", 0, 0, 2, "Using skin renderer to replace the original first person hands renderer.", "0 = use client config", "1 = always disable", "2 = always enable");

//                if (!LibModInfo.MOD_VERSION.startsWith("@VER")) {
//                    lastVersion = config.getString("lastVersion", CATEGORY_GENERAL, "0.0",
//                            "Used by the mod to check if it has been updated.");
//                }
            });
            defineCategory("wardrobe", "Setting for the players wardrobe.", () -> {
                define("allowOpening", true, "Allow the player to open the wardrobe GUI.");
                define("enableSkinTab", true, "Enable the wardrobe skins tab.");
                define("enableOutfitTab", true, "Enable the wardrobe outfits tab.");
                define("enableDisplayTab", true, "Enable the wardrobe display settings tab.");
                define("enableColourTab", true, "Enable the wardrobe colour settings tab.");
                define("enableDyeTab", true, "Enable the wardrobe dyes tab.");
                defineInRange("mobStartingSlots", 3, 1, SkinSlotType.getMaxSlotSize(), "Number of slot columns the mob starts with for skins.");
                defineInRange("playerStartingSlots", 3, 1, SkinSlotType.getMaxSlotSize(), "Number of slot columns the player starts with for skins.");
                defineInRange("playerDropSkinsOnDeath", 0, 0, 2, "Should skins be dropped on player death.", "0 = use keep inventory rule", "1 = never drop", "2 = always drop");
            });
            defineCategory("library", "Setting for the library blocks.", () -> {
                define("allowDownloadingSkins", false, "Allows clients to save skins from a server to their local computer using the library.");
                define("allowUploadingSkins", true, "Allows clients to load skins from their local computer onto the server using the library.");
                define("extractOfficialSkins", true, "Allow the mod to extract the official skins that come with the mod into the library folder.");
                define("allowPreviewSkins", true, "Shows model previews in the library.", "Causes a lot of extra load on servers.", "Best to turn off on high population servers");
                define("allowManageSkins", false, "Allows clients to manage skins of the server computer library.", "Required permission level 5 or higher.");

                defineList("customGlobalServerURLs", new ArrayList<String>(), "We priority use https for the access token APIs.");
                define("privateGlobalServer", false, "For the private global servers, will have special handling for caching.");
            });
            defineCategory("recipe", "Setting for mod recipes.", () -> {
                define("disableRecipes", false, "Disable vanilla recipes. Use if you want to manually add recipes for a mod pack.");
                define("disableDollRecipe", false, "Disable hidden in world doll recipe.");
                define("disableSkinningRecipes", false, "Disable skinning table recipes.");
                define("hideDollFromCreativeTabs", true, "Hides the doll block from the creative tab and NEI.");
                define("hideGiantFromCreativeTabs", true, "Hides the giant block from the creative tab and NEI.");
                define("enableRecoveringSkins", false, "Enable copying the skin off an item in the skinning table");
            });

            defineCategory("holiday-events", "Enable/disable holiday events.", () -> {
                define("disableAllHolidayEvents", false, "Setting to true will disable all holiday events. What's wrong with you!");
//                SimpleDateFormat sdf = new SimpleDateFormat("MM:dd:HH", Locale.ENGLISH);
//                for (Holiday holiday : ModHolidays.getHolidays()) {
//                    boolean holidayEnabled = builder.define("holiday-" + holiday.getName() + "-enabled", true,
//                            "Enable holiday.");
//
//                    Calendar startDate = holiday.getStartDate();
//                    Calendar endDate = holiday.getEndDate();
//
//                    String dates = config.getString("holiday-" + holiday.getName() + "-range",
//                            sdf.format(startDate.getTime()) + "-" + sdf.format(endDate.getTime()),
//                            "Holiday date range. Format (Start Date-End Date) (MONTH:DAY:HOUR-MONTH:DAY:HOUR)");
//
//                    String startDateStr = sdf.format(startDate.getTime());
//                    String endDateStr = sdf.format(endDate.getTime());
//
//                    if (dates.contains("-")) {
//                        String[] split = dates.split("-");
//                        startDateStr = split[0];
//                        endDateStr = split[1];
//                    }
//
//                    try {
//                        Date date = sdf.parse(startDateStr);
//                        startDate.setTime(date);
//                        startDate.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        Date date = sdf.parse(endDateStr);
//                        endDate.setTime(date);
//                        endDate.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//                    holiday.setEnabled(holidayEnabled);
//                    holiday.setStartDate(startDate);
//                    holiday.setEndDate(endDate);
//                }
            });

            defineCategory("entity-skins", "Control how/if entities spawm with skin on them.", () -> {
                defineInRange("enitiySpawnWithSkinsChance", 0, 0, 100, "Percentage change that entities will spawn with skins equipped.");
                defineInRange("entityDropSkinChance", 0, 0, 100, "Percentage change that entities will drop equipped skins when killed.");
//            enitiySpawnSkinTargetPath = "/" + config.getString("enitiySpawnSkinTargetPath", "",
//                    "Target library path for skin spawned on entities.\n"
//                            + "Examples: 'official/' for only skins in the official folder or 'downloads/' for skins in the downloads folder.\n"
//                            + "Leave black for all skins.");
            });

            defineCategory("cache", "Change (memory use/IO access) ratio by category setting in this category.", () -> {
                defineInRange("expireTime", 6000, 0, 36000, "How long in seconds the server will keep skins in it's cache.", "Default 600 seconds is 10 minutes.", "Setting to 0 turns off this option.");
                defineInRange("maxSize", 2000, 0, 10000, "Max size the skin cache can reach before skins are removed.", "Setting to 0 turns off this option.");
            });

            //            config.setCategoryComment(CATEGORY_COMPATIBILITY, "Allows auto item skinning for supported mod to be enable/disable.");
//            for (ModAddon modAddon : ModAddonManager.getLoadedAddons()) {
//                if (modAddon.hasItemOverrides()) {
//                    boolean itemSkinningSupport = config.getBoolean(
//                            String.format("enable-%s-compat", modAddon.getModId()),
//                            CATEGORY_COMPATIBILITY, true,
//                            String.format("Enable auto item support for %s.", modAddon.getModName()));
//                    modAddon.setItemSkinningSupport(itemSkinningSupport);
//                }
//            }
            defineCategory("overrides", "Custom list of items that can be skinned.", () -> {
                Predicate<Object> elementValidator = item -> Arrays.stream(ItemOverrideType.values()).anyMatch(t -> ((String) item).startsWith(t.getName()));
                defineList("itemOverrides", new ArrayList<String>(), elementValidator, "Format [\"override type:mod id:item name\"]", "Valid override types are: sword, shield, bow, pickaxe, axe, shovel, hoe and item", "example [\"sword:minecraft:iron_sword\",\"sword:minecraft:gold_sword\"]");

                define("enableMatchingByItemId", true, "Tries to automatically assign the correct type of skin type without config and tag.");
                defineList("matchingBlacklistByItemId", new ArrayList<String>(), "If the matching system wrong, you can add the item id here to this ignore it.");
            });

            return values;
        }

    }
}
