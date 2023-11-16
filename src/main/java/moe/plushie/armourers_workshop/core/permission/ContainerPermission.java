package moe.plushie.armourers_workshop.core.permission;

import moe.plushie.armourers_workshop.api.permission.IPermissionNode;
import moe.plushie.armourers_workshop.api.registry.IRegistryKey;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.inventory.Menu;
import net.cocoonmc.core.inventory.MenuType;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.core.world.entity.Player;

import java.util.function.Consumer;

public class ContainerPermission extends Permission {

    public ContainerPermission(String name, Consumer<Consumer<IRegistryKey<?>>> each) {
        super(name);
        each.accept(this::add);
    }

    public <T extends Menu> boolean accept(IRegistryKey<MenuType<T>> type, Entity target, Player player) {
        IPermissionNode node = get(type.getRegistryName());
        return eval(node, player, new TargetPermissionContext(player, target));
    }

    public <T extends Menu> boolean accept(IRegistryKey<MenuType<T>> type, Level level, BlockPos pos, Player player) {
        IPermissionNode node = get(type.getRegistryName());
        return eval(node, player, new BlockPermissionContext(player, pos, level.getBlockState(pos), null));
    }
}
