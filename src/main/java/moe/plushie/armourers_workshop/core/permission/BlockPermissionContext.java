package moe.plushie.armourers_workshop.core.permission;

import com.google.common.base.Preconditions;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.Direction;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.world.entity.Player;
import org.jetbrains.annotations.Nullable;

public class BlockPermissionContext extends PlayerPermissionContext {

    public final BlockPos blockPos;
    public final BlockState blockState;
    public final Direction facing;

    public BlockPermissionContext(Player ep, BlockPos pos, @Nullable BlockState state, @Nullable Direction f) {
        super(ep);
        this.blockPos = Preconditions.checkNotNull(pos, "BlockPos can't be null in BlockPosContext!");
        this.blockState = state;
        this.facing = f;
    }
}
