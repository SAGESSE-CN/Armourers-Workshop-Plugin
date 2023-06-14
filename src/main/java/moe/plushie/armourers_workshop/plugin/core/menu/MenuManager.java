package moe.plushie.armourers_workshop.plugin.core.menu;

import moe.plushie.armourers_workshop.plugin.api.FriendlyByteBuf;
import moe.plushie.armourers_workshop.plugin.api.TextComponent;
import moe.plushie.armourers_workshop.plugin.core.network.FMLOpenContainer;
import moe.plushie.armourers_workshop.plugin.core.network.NetworkManager;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class MenuManager {

    public static void openMenu(ContainerMenu menu, Player player) {
        openMenu(menu, player, menu::serialize);
    }

    public static void openMenu(ContainerMenu menu, Player player, Consumer<FriendlyByteBuf> consumer) {
        String id = menu.getRegistryName();
        FriendlyByteBuf buf = new FriendlyByteBuf();
        TextComponent title = TextComponent.translatable("inventory." + id.replace(':', '.'));
        consumer.accept(buf);
        menu.open();
        NetworkManager.sendTo(new FMLOpenContainer(id, menu.getMenuId(), title, buf), player);
    }
}
