package moe.plushie.armourers_workshop.plugin.core.block;

import moe.plushie.armourers_workshop.plugin.api.Block;
import moe.plushie.armourers_workshop.plugin.api.BlockPos;
import moe.plushie.armourers_workshop.plugin.api.BlockState;
import moe.plushie.armourers_workshop.plugin.api.BlockStateProperties;
import moe.plushie.armourers_workshop.plugin.api.InteractionResult;
import moe.plushie.armourers_workshop.plugin.api.state.StateDefinition;
import moe.plushie.armourers_workshop.plugin.api.state.properties.BooleanProperty;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

public class HologramProjectorBlock extends AttachedDirectionalBlock {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public HologramProjectorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, World world, BlockPos blockPos, Player player, EquipmentSlot hand) {
        return super.use(blockState, world, blockPos, player, hand);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FACE, LIT);
    }
}
