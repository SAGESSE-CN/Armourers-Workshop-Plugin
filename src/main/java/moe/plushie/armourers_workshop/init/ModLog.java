package moe.plushie.armourers_workshop.init;

import moe.plushie.armourers_workshop.ArmourersWorkshopPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ModLog {

    private static final Logger LOGGER = getLogger();

    private static Logger getLogger() {
        Logger logger = ArmourersWorkshopPlugin.INSTANCE.getLogger();
        logger.setLevel(Level.ALL);
        return logger;
    }

    public static void debug(String message, Object... params) {
        LOGGER.config(_format(message, params));
    }

    public static void info(String message, Object... params) {
        LOGGER.info(_format(message, params));
    }

    public static void error(String message, Object... params) {
        LOGGER.severe(_format(message, params));
    }

    public static void warn(String message, Object... params) {
        LOGGER.warning(_format(message, params));
    }

    private static String _format(String message, Object... params) {
        return String.format(message.replace("{}", "%s"), params);
    }
}
