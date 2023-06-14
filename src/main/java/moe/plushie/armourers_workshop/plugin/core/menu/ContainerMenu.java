package moe.plushie.armourers_workshop.plugin.core.menu;

import moe.plushie.armourers_workshop.customapi.CustomMenu;
import moe.plushie.armourers_workshop.plugin.init.ModLog;
import org.bukkit.entity.Player;

public abstract class ContainerMenu extends CustomMenu {

    private int menuId;
    private final String registryName;

    public ContainerMenu(String registryName, Player player, int size) {
        super(player, size, registryName);
        this.registryName = registryName;
    }

    public String getRegistryName() {
        return registryName;
    }

    @Override
    public void handleOpenWindowPacket(int windowId) {
        // we need send custom event.
        this.menuId = windowId;
    }

    public int getMenuId() {
        return menuId;
    }
}
