package moe.plushie.armourers_workshop.init;

import moe.plushie.armourers_workshop.api.config.IConfigBuilder;
import moe.plushie.armourers_workshop.api.config.IConfigSpec;
import moe.plushie.armourers_workshop.core.network.NetworkManager;
import moe.plushie.armourers_workshop.core.network.UpdateContextPacket;
import moe.plushie.armourers_workshop.core.skin.SkinSlotType;
import moe.plushie.armourers_workshop.init.platform.EnvironmentManager;
import moe.plushie.armourers_workshop.utils.SimpleConfig;

import java.util.ArrayList;

public class ModConfigSpec {

    private static boolean IS_INITED = false;
    public static final IConfigSpec COMMON = createSpecBuilder().build();

    public static abstract class Common extends ModConfig.Common implements IConfigBuilder {

        public Common() {
            defineCategory("general", "General settings.", () -> {
                defineInRange("maxUndos", 100, 0, 1000, "Max number of undos a player has for block painting.").bind(v -> maxUndos = v, () -> maxUndos);
                defineInRange("blockTaskRate", 10, 1, 1000, "Max number of processing blocks in per tick.").bind(v -> blockTaskRate = v, () -> blockTaskRate);
                define("lockDyesOnSkins", false, "When enabled players will not be able to remove dyes from skins in the dye table.").bind(v -> lockDyesOnSkins = v, () -> lockDyesOnSkins);
                define("instancedDyeTable", false, "If true the dye table will be instanced for each player. Items will be dropped when the table is closed.").bind(v -> instancedDyeTable = v, () -> instancedDyeTable);
                define("enableProtocolCheck", true, "If enabled the server will check the client protocol version in the login.", "Highly recommended unless the server does not support handshake.").bind(v -> enableProtocolCheck = v, () -> enableProtocolCheck);
                defineInRange("serverModelSendRate", 4000, 0, 8000, "The maximum number of skins the server is allow to send every minute.", "Less that 1 equals unlimited. (not recommended may cause bandwidth and cpu spikes on the server)").bind(n -> serverSkinSendRate = n, () -> serverSkinSendRate);
                define("serverCompressesSkins", true, "If enabled the server will compress skins before sending them to clients.", "Highly recommended unless the server has a very slow CPU.").bind(v -> enableServerCompressesSkins = v, () -> enableServerCompressesSkins);
                defineInRange("enableEmbeddedSkinRenderer", 0, 0, 2, "Using embedded skin renderer to replace the original item renderer.", "0 = use client config", "1 = always disable", "2 = always enable").bind(v -> enableEmbeddedSkinRenderer = v, () -> enableEmbeddedSkinRenderer);
                defineInRange("enableFirstPersonSkinRenderer", 0, 0, 2, "Using skin renderer to replace the original first person hands renderer.", "0 = use client config", "1 = always disable", "2 = always enable").bind(v -> enableFirstPersonSkinRenderer = v, () -> enableFirstPersonSkinRenderer);

//                if (!LibModInfo.MOD_VERSION.startsWith("@VER")) {
//                    lastVersion = config.getString("lastVersion", CATEGORY_GENERAL, "0.0",
//                            "Used by the mod to check if it has been updated.");
//                }
            });
            defineCategory("wardrobe", "Setting for the players wardrobe.", () -> {
                define("allowOpening", true, "Allow the player to open the wardrobe GUI.").bind(v -> wardrobeAllowOpening = v, () -> wardrobeAllowOpening);
                define("enableSkinTab", true, "Enable the wardrobe skins tab.").bind(v -> showWardrobeSkins = v, () -> showWardrobeSkins);
                define("enableOutfitTab", true, "Enable the wardrobe outfits tab.").bind(v -> showWardrobeOutfits = v, () -> showWardrobeOutfits);
                define("enableDisplayTab", true, "Enable the wardrobe display settings tab.").bind(v -> showWardrobeDisplaySettings = v, () -> showWardrobeDisplaySettings);
                define("enableColourTab", true, "Enable the wardrobe colour settings tab.").bind(v -> showWardrobeColorSettings = v, () -> showWardrobeColorSettings);
                define("enableDyeTab", true, "Enable the wardrobe dyes tab.").bind(v -> showWardrobeDyeSetting = v, () -> showWardrobeDyeSetting);
                defineInRange("mobStartingSlots", 3, 1, SkinSlotType.getMaxSlotSize(), "Number of slot columns the mob starts with for skins.").bind(v -> prefersWardrobeMobSlots = v, () -> prefersWardrobeMobSlots);
                defineInRange("playerStartingSlots", 3, 1, SkinSlotType.getMaxSlotSize(), "Number of slot columns the player starts with for skins.").bind(v -> prefersWardrobePlayerSlots = v, () -> prefersWardrobePlayerSlots);
                defineInRange("playerDropSkinsOnDeath", 0, 0, 2, "Should skins be dropped on player death.", "0 = use keep inventory rule", "1 = never drop", "2 = always drop").bind(v -> prefersWardrobeDropOnDeath = v, () -> prefersWardrobeDropOnDeath);
            });
            defineCategory("library", "Setting for the library blocks.", () -> {
                define("allowDownloadingSkins", false, "Allows clients to save skins from a server to their local computer using the library.").bind(v -> allowDownloadingSkins = v, () -> allowDownloadingSkins);
                define("allowUploadingSkins", true, "Allows clients to load skins from their local computer onto the server using the library.").bind(v -> allowUploadingSkins = v, () -> allowUploadingSkins);
                define("extractOfficialSkins", true, "Allow the mod to extract the official skins that come with the mod into the library folder.").bind(v -> extractOfficialSkins = v, () -> extractOfficialSkins);
                define("allowPreviewSkins", true, "Shows model previews in the library.", "Causes a lot of extra load on servers.", "Best to turn off on high population servers").bind(n -> allowLibraryPreviews = n, () -> allowLibraryPreviews);
                define("allowManageSkins", false, "Allows clients to manage skins of the server computer library.", "Required permission level 5 or higher.").bind(v -> allowLibraryRemoteManage = v, () -> allowLibraryRemoteManage);

                defineList("skinServerURLs", new ArrayList<String>(), "We priority use https for the access token APIs.").bind(v -> customSkinServerURLs = new ArrayList<>(v), () -> new ArrayList<>(customSkinServerURLs));
            });

            defineCategory("holiday-events", "Enable/disable holiday events.", () -> {
                define("disableAllHolidayEvents", false, "Setting to true will disable all holiday events. What's wrong with you!").bind(v -> disableAllHolidayEvents = v, () -> disableAllHolidayEvents);
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

            defineCategory("cache", "Change (memory use/IO access) ratio by category setting in this category.", () -> {
                defineInRange("expireTime", 6000, 0, 36000, "How long in seconds the server will keep skins in it's cache.", "Default 600 seconds is 10 minutes.", "Setting to 0 turns off this option.").bind(v -> skinCacheExpireTime = v, () -> skinCacheExpireTime);
                defineInRange("maxSize", 2000, 0, 10000, "Max size the skin cache can reach before skins are removed.", "Setting to 0 turns off this option.").bind(v -> skinCacheMaxSize = v, () -> skinCacheMaxSize);
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
                defineList("itemOverrides", new ArrayList<String>(), "Format [\"override type:mod id:item name\"]", "Valid override types are: sword, shield, bow, pickaxe, axe, shovel, hoe and item", "example [\"sword:minecraft:iron_sword\",\"sword:minecraft:gold_sword\"]").bind(n -> overrides = new ArrayList<>(n), () -> new ArrayList<>(overrides));

                define("enableMatchingByItemId", true, "Tries to automatically assign the correct type of skin type without config and tag.").bind(v -> enableMatchingByItemId = v, () -> enableMatchingByItemId);
                defineList("matchingBlacklistByItemId", new ArrayList<String>(), "If the matching system wrong, you can add the item id here to this ignore it.").bind(v -> disableMatchingItems = new ArrayList<>(v), () -> new ArrayList<>(disableMatchingItems));
            });
        }
    }

    public static void init() {
        if (IS_INITED) {
            COMMON.reload();
            return;
        }
        IS_INITED = true;
        COMMON.notify(() -> {
            // send config changes to all player.
            ModLog.debug("send config changes to all players");
            NetworkManager.sendToAll(new UpdateContextPacket());
        });
    }


    private static Common createSpecBuilder() {
        SimpleConfig config = new SimpleConfig(EnvironmentManager.getConfigDirectory(), "common");
        return new Common() {
            @Override
            public IConfigBuilder builder() {
                return config;
            }
        };
    }
}
