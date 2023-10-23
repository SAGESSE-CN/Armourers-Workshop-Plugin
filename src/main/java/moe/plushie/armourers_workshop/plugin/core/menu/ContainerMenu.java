package moe.plushie.armourers_workshop.plugin.core.menu;

import moe.plushie.armourers_workshop.customapi.CustomMenu;
import moe.plushie.armourers_workshop.plugin.api.Component;
import moe.plushie.armourers_workshop.plugin.api.FriendlyByteBuf;
import moe.plushie.armourers_workshop.plugin.api.MenuType;
import moe.plushie.armourers_workshop.plugin.core.network.FMLOpenContainer;
import moe.plushie.armourers_workshop.plugin.core.network.NetworkManager;
import org.bukkit.entity.Player;

public abstract class ContainerMenu extends CustomMenu {

    protected final Player player;
    protected final MenuType<?> menuType;

    public ContainerMenu(MenuType<?> menuType, Player player, int slotSize) {
        super(player, slotSize, menuType.getRegistryName());
        this.player = player;
        this.menuType = menuType;
    }

    @Override
    public void handleOpenWindowPacket(int windowId) {
        // we need send custom event.
        String id = getType().getRegistryName();
        FriendlyByteBuf buf = new FriendlyByteBuf();
        serialize(buf);
        Component title = Component.translatable("inventory." + id.replace(':', '.'));
        NetworkManager.sendTo(new FMLOpenContainer(id, windowId, title, buf), player);
    }

    public void serialize(FriendlyByteBuf buffer) {
        // nop
    }

    public MenuType<?> getType() {
        return menuType;
    }
}
