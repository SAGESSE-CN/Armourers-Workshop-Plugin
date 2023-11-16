package moe.plushie.armourers_workshop.init.platform;

import moe.plushie.armourers_workshop.api.WorldAccessor;
import moe.plushie.armourers_workshop.api.registry.IRegistryKey;
import moe.plushie.armourers_workshop.core.menu.ContainerMenu;
import moe.plushie.armourers_workshop.init.ModPermissions;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.BlockEntity;
import net.cocoonmc.core.inventory.MenuType;
import net.cocoonmc.core.world.InteractionResult;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.Player;

public class MenuManager {

    public static <T extends ContainerMenu, V> InteractionResult openMenu(IRegistryKey<MenuType<T>> menuType, Player player, V value) {
        T menu = menuType.get().createMenu(player, value);
        menu.openMenu();
        return InteractionResult.SUCCESS;
    }

    public static <C extends ContainerMenu> InteractionResult openMenu(IRegistryKey<MenuType<C>> menuType, BlockEntity blockEntity, Player player) {
        // we assume it is a valid block entity.
        if (blockEntity != null && blockEntity.getLevel() != null) {
            return openMenu(menuType, blockEntity.getLevel(), blockEntity.getBlockPos(), player);
        }
        return InteractionResult.FAIL;

    }

    public static <C extends ContainerMenu> InteractionResult openMenu(IRegistryKey<MenuType<C>> menuType, Level level, BlockPos blockPos, Player player) {
        // the player must have sufficient permissions to open the GUI.
        // note: only check in the server side.
        if (!ModPermissions.OPEN.accept(menuType, level, blockPos, player)) {
            return InteractionResult.FAIL;
        }
        return openMenu(menuType, player, WorldAccessor.of(level, blockPos));
    }
}
