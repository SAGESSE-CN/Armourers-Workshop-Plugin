package moe.plushie.armourers_workshop.plugin.core.block;

import moe.plushie.armourers_workshop.plugin.api.BlockPos;
import moe.plushie.armourers_workshop.plugin.api.BlockState;
import moe.plushie.armourers_workshop.plugin.api.InteractionResult;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

public class GlobalSkinLibraryBlock extends HorizontalDirectionalBlock {

    public GlobalSkinLibraryBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, World world, BlockPos blockPos, Player player, EquipmentSlot hand) {
        return super.use(blockState, world, blockPos, player, hand);
    }
}
