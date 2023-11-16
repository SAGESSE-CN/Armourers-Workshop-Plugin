package moe.plushie.armourers_workshop.api.permission;


import net.cocoonmc.core.network.Component;
import net.cocoonmc.core.resources.ResourceLocation;
import net.cocoonmc.core.world.entity.Player;

import java.util.UUID;

public interface IPermissionNode {

    Component getName();

    Component getDescription();

    ResourceLocation getRegistryName();

    boolean resolve(UUID profile, IPermissionContext context);

    default boolean resolve(Player player, IPermissionContext context) {
        return resolve(player.getUUID(), context);
    }
}
