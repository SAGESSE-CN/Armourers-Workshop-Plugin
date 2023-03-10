package moe.plushie.armourers_workshop.plugin.core.menu;

import moe.plushie.armourers_workshop.plugin.api.FriendlyByteBuf;
import moe.plushie.armourers_workshop.plugin.api.Menu;
import moe.plushie.armourers_workshop.plugin.api.TextComponent;
import moe.plushie.armourers_workshop.plugin.network.FMLOpenContainer;
import moe.plushie.armourers_workshop.plugin.network.NetworkManager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.function.Consumer;

public class MenuManager {

    private static final HashMap<Integer, Menu> MENUS = new HashMap<>();

    public static void openMenu(Menu menu, Player player, Consumer<FriendlyByteBuf> consumer) {
        int menuId = MENUS.size();
        String id = menu.getRegistryName();
        FriendlyByteBuf buf = new FriendlyByteBuf();
        TextComponent title = TextComponent.translatable("inventory." + id.replace(':', '.'));
        consumer.accept(buf);
        NetworkManager.sendTo(new FMLOpenContainer(id, menuId, title, buf), player);
        MENUS.put(menuId, menu);
    }

    public static Menu getMenu(int menuId) {
        return MENUS.get(menuId);
    }
}
