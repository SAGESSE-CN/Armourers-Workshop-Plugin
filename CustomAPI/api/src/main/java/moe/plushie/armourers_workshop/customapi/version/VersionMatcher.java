package moe.plushie.armourers_workshop.customapi.version;


import moe.plushie.armourers_workshop.customapi.CustomAPI;
import org.bukkit.Bukkit;

/**
 * Matches the server's NMS version to its {@link CustomAPI}
 */
public class VersionMatcher {

    /**
     * Matches the server version to it's {@link CustomAPI}
     *
     * @return The {@link CustomAPI} for this server
     * @throws IllegalStateException If the version wrapper failed to be instantiated or is unable to be found
     */
    public CustomAPI match() {
        final String serverVersion = Bukkit.getServer()
                .getClass()
                .getPackage()
                .getName()
                .split("\\.")[3]
                .substring(1);
        try {
            return (CustomAPI) Class.forName(getClass().getPackage().getName() + ".Wrapper" + serverVersion)
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException(
                    "customapi does not support server version \"" + serverVersion + "\"", exception);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(
                    "Failed to instantiate version wrapper for version " + serverVersion, exception);
        }
    }
}
