package moe.plushie.armourers_workshop.plugin.core.block;

import moe.plushie.armourers_workshop.plugin.api.WorldAccessor;
import moe.plushie.armourers_workshop.plugin.init.ModMenuTypes;
import moe.plushie.armourers_workshop.plugin.init.platform.MenuManager;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.core.world.InteractionResult;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ColorMixerBlock extends HorizontalDirectionalBlock {

    public ColorMixerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, World world, BlockPos blockPos, Player player, InteractionHand hand) {
        return MenuManager.openMenu(ModMenuTypes.COLOR_MIXER, player, WorldAccessor.of(world, blockPos));
    }
}
