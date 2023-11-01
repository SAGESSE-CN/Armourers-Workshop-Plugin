package moe.plushie.armourers_workshop.core.block;

import moe.plushie.armourers_workshop.api.WorldAccessor;
import moe.plushie.armourers_workshop.init.ModMenuTypes;
import moe.plushie.armourers_workshop.init.platform.MenuManager;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.core.world.InteractionResult;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.Player;

public class GlobalSkinLibraryBlock extends HorizontalDirectionalBlock {

    public GlobalSkinLibraryBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand) {
        return MenuManager.openMenu(ModMenuTypes.SKIN_LIBRARY_GLOBAL, player, WorldAccessor.of(level, blockPos));
    }
}
