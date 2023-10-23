package moe.plushie.armourers_workshop.plugin.api;

import org.bukkit.entity.Player;

public class MenuType<T> {

    private final String registryName;
    private final Factory<T, ?> factory;

    public MenuType(String registryName, Factory<T, ?> factory) {
        this.registryName = registryName;
        this.factory = factory;
    }

    public <V> T createMenu(Player player, V hostObject) {
        // noinspection unchecked
        Factory<T, V> factory1 = (Factory<T, V>) factory;
        return factory1.createMenu(this, player, hostObject);
    }

    public String getRegistryName() {
        return registryName;
    }

    public interface Factory<T, V> {

        T createMenu(MenuType<?> menuType, Player player, V hostObject);
    }

}
