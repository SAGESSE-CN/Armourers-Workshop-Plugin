package moe.plushie.armourers_workshop.plugin.api;

import moe.plushie.armourers_workshop.plugin.api.state.properties.BooleanProperty;
import moe.plushie.armourers_workshop.plugin.api.state.properties.EnumProperty;
import org.bukkit.block.BlockFace;

public class BlockStateProperties {

    public static final BooleanProperty LIT = BooleanProperty.create("lit");

    //public static final EnumProperty<BlockFace> FACING = DirectionProperty.create("facing", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);

    public static final EnumProperty<AttachFace> ATTACH_FACE = EnumProperty.create("face", AttachFace.class);

    public static final EnumProperty<BlockFace> HORIZONTAL_FACING = EnumProperty.create("facing", BlockFace.class, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);
}
