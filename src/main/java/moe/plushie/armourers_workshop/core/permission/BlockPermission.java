package moe.plushie.armourers_workshop.core.permission;


import moe.plushie.armourers_workshop.api.permission.IPermissionNode;
import moe.plushie.armourers_workshop.api.registry.IRegistryKey;
import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.block.BlockEntity;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.world.entity.Player;

import java.util.Arrays;

public class BlockPermission extends Permission {

    @SafeVarargs
    public BlockPermission(String name, IRegistryKey<Block>... blocks) {
        super(name);
        Arrays.stream(blocks).forEach(this::add);
    }

    public boolean accept(Player player) {
        if (player == null) {
            return true;
        }
        return getNodes().stream().allMatch(node -> eval(node, player, new PlayerPermissionContext(player)));
    }

    public boolean accept(BlockEntity blockEntity, Player player) {
        BlockState state = blockEntity.getBlockState();
        IPermissionNode node = get(state.getBlock().getRegistryName());
        return eval(node, player, new BlockPermissionContext(player, blockEntity.getBlockPos(), state, null));
    }
}
