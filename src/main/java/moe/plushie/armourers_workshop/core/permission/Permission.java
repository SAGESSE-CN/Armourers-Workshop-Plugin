package moe.plushie.armourers_workshop.core.permission;


import moe.plushie.armourers_workshop.api.permission.IPermissionContext;
import moe.plushie.armourers_workshop.api.permission.IPermissionNode;
import moe.plushie.armourers_workshop.api.registry.IRegistryKey;
import moe.plushie.armourers_workshop.init.ModConstants;
import moe.plushie.armourers_workshop.init.ModLog;
import net.cocoonmc.core.network.Component;
import net.cocoonmc.core.resources.ResourceLocation;
import net.cocoonmc.core.world.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public abstract class Permission {

    protected final String name;
    protected final HashMap<ResourceLocation, IPermissionNode> nodes = new HashMap<>();

    public Permission(String name) {
        this.name = name;
    }

    protected void add(IRegistryKey<?> object) {
        ResourceLocation registryName = object.getRegistryName();
        IPermissionNode node = makeNode(registryName.getPath() + "." + name);
        nodes.put(registryName, node);
    }

    protected IPermissionNode get(ResourceLocation registryName) {
        return nodes.get(registryName);
    }

    protected boolean eval(IPermissionNode node, Player player, @Nullable PlayerPermissionContext context) {
        return node.resolve(player, context);
    }

    private IPermissionNode makeNode(String path) {
        ResourceLocation registryName = ModConstants.key(path);
        String key = registryName.getNamespace() + "." + registryName.getPath();
        ModLog.debug("Registering Permission '{}'", registryName);
        org.bukkit.permissions.Permission perm = new org.bukkit.permissions.Permission(key, org.bukkit.permissions.PermissionDefault.TRUE);
        return new IPermissionNode() {

            @Override
            public Component getName() {
                return Component.translatable("permission." + key);
            }

            @Override
            public Component getDescription() {
                return Component.translatable("permission." + key + ".desc");
            }

            @Override
            public ResourceLocation getRegistryName() {
                return registryName;
            }

            @Override
            public boolean resolve(UUID profile, IPermissionContext context) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean resolve(Player player, IPermissionContext context) {
                return player.asBukkit().hasPermission(perm);
            }
        };
    }

    public String getName() {
        return name;
    }

    public Collection<IPermissionNode> getNodes() {
        return nodes.values();
    }
}

