package moe.plushie.armourers_workshop.plugin.core.block;

import moe.plushie.armourers_workshop.plugin.api.WorldAccessor;
import moe.plushie.armourers_workshop.plugin.init.ModMenuTypes;
import moe.plushie.armourers_workshop.plugin.init.platform.MenuManager;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.block.BlockStateProperties;
import net.cocoonmc.core.block.state.StateDefinition;
import net.cocoonmc.core.block.state.properties.BooleanProperty;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.core.world.InteractionResult;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class HologramProjectorBlock extends AttachedDirectionalBlock {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public HologramProjectorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, World world, BlockPos blockPos, Player player, InteractionHand hand) {
        return MenuManager.openMenu(ModMenuTypes.HOLOGRAM_PROJECTOR, player, WorldAccessor.of(world, blockPos));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FACE, LIT);
    }
}
