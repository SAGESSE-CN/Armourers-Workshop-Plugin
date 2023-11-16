package moe.plushie.armourers_workshop.core.permission;

import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.core.world.entity.Player;
import org.jetbrains.annotations.Nullable;

public class TargetPermissionContext extends PlayerPermissionContext {

    public final Entity target;

    public TargetPermissionContext(Player ep, @Nullable Entity entity) {
        super(ep);
        this.target = entity;
    }
}
