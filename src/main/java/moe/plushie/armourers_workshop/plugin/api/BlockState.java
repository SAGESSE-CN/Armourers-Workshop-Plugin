package moe.plushie.armourers_workshop.plugin.api;

import com.google.common.collect.ImmutableMap;
import moe.plushie.armourers_workshop.plugin.api.state.StateHolder;
import moe.plushie.armourers_workshop.plugin.api.state.properties.Property;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

public class BlockState extends StateHolder<Block, BlockState> {

    public BlockState(Block block, ImmutableMap<Property<?>, Comparable<?>> values) {
        super(block, values);
    }

    public InteractionResult use(World world, BlockPos blockPos, Player player, EquipmentSlot hand) {
        return getBlock().use(this, world, blockPos, player, hand);
    }

    public InteractionResult attack(World world, BlockPos blockPos, Player player) {
        return getBlock().attack(this, world, blockPos, player);
    }

    public boolean canSurvive(World world, BlockPos blockPos) {
        return getBlock().canSurvive(this, world, blockPos);
    }


    public Block getBlock() {
        return owner;
    }

}
