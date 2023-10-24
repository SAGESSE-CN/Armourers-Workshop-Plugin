package moe.plushie.armourers_workshop.plugin.core.block;

import moe.plushie.armourers_workshop.plugin.api.BlockPos;
import moe.plushie.armourers_workshop.plugin.api.BlockState;
import moe.plushie.armourers_workshop.plugin.api.InteractionResult;
import moe.plushie.armourers_workshop.plugin.api.WorldAccessor;
import moe.plushie.armourers_workshop.plugin.init.ModMenuTypes;
import moe.plushie.armourers_workshop.plugin.init.platform.MenuManager;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

public class DyeTableBlock extends HorizontalDirectionalBlock {

    public DyeTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, World world, BlockPos blockPos, Player player, EquipmentSlot hand) {
        return MenuManager.openMenu(ModMenuTypes.DYE_TABLE, player, WorldAccessor.of(world, blockPos));
    }
}
