package moe.plushie.armourers_workshop.init;

import moe.plushie.armourers_workshop.ArmourersWorkshopImpl;

import java.util.logging.Logger;

public class ModLog {

    private static final Logger LOGGER = ArmourersWorkshopImpl.INSTANCE.getLogger();

    public static void debug(String message, Object... params) {
        //LOGGER.finer(_format(message, params));
        LOGGER.info(_format(message, params));
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
