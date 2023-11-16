package moe.plushie.armourers_workshop.core.block;

import moe.plushie.armourers_workshop.core.blockentity.SkinnableBlockEntity;
import moe.plushie.armourers_workshop.core.blockentity.UpdatableContainerBlockEntity;
import moe.plushie.armourers_workshop.core.data.SkinBlockPlaceContext;
import moe.plushie.armourers_workshop.core.entity.SeatEntity;
import moe.plushie.armourers_workshop.core.skin.SkinDescriptor;
import moe.plushie.armourers_workshop.init.ModBlockEntityTypes;
import moe.plushie.armourers_workshop.init.ModItems;
import moe.plushie.armourers_workshop.init.ModMenuTypes;
import moe.plushie.armourers_workshop.init.ModPermissions;
import moe.plushie.armourers_workshop.init.platform.MenuManager;
import moe.plushie.armourers_workshop.utils.ObjectUtils;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.Direction;
import net.cocoonmc.core.block.Block;
import net.cocoonmc.core.block.BlockEntity;
import net.cocoonmc.core.block.BlockEntitySupplier;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.block.BlockStateProperties;
import net.cocoonmc.core.block.Blocks;
import net.cocoonmc.core.block.state.StateDefinition;
import net.cocoonmc.core.block.state.properties.AttachFace;
import net.cocoonmc.core.block.state.properties.BedPart;
import net.cocoonmc.core.block.state.properties.BooleanProperty;
import net.cocoonmc.core.block.state.properties.EnumProperty;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.item.context.BlockPlaceContext;
import net.cocoonmc.core.math.VoxelShape;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.core.world.InteractionResult;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.LivingEntity;
import net.cocoonmc.core.world.entity.Player;
import net.cocoonmc.core.world.loot.LootContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SkinnableBlock extends AttachedDirectionalBlock implements BlockEntitySupplier {

    public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED;

    public SkinnableBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(FACE, AttachFace.WALL)
                .setValue(LIT, false)
                .setValue(PART, BedPart.HEAD)
                .setValue(OCCUPIED, false));
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand) {
        SkinnableBlockEntity blockEntity = getBlockEntity(level, blockPos);
        if (blockEntity == null) {
            return InteractionResult.FAIL;
        }
//        if (blockEntity.isLinked()) {
//            BlockPos linkedPos = blockEntity.getLinkedBlockPos();
//            BlockState linkedState = level.getBlockState(linkedPos);
//            return linkedState.getBlock().use(linkedState, level, linkedPos, player, hand, traceResult);
//        }
        if (blockEntity.isBed() && !player.isSecondaryUseActive()) {
            if (ModPermissions.SKINNABLE_SLEEP.accept(blockEntity, player)) {
//                return Blocks.RED_BED.use(blockState, level, blockEntity.getBedPos(), player, hand, traceResult);
            }
        }
        if (blockEntity.isSeat() && !player.isSecondaryUseActive()) {
            if (ModPermissions.SKINNABLE_SIT.accept(blockEntity, player)) {
//                if (level.isClientSide()) {
//                    return InteractionResult.CONSUME;
//                }
//                Vector3d seatPos = blockEntity.getSeatPos().add(0.5f, 0.5f, 0.5f);
//                SeatEntity seatEntity = getSeatEntity((ServerLevel) level, blockEntity.getParentPos(), seatPos);
//                if (seatEntity == null) {
//                    return InteractionResult.FAIL; // it is using
//                }
//                player.startRiding(seatEntity, true);
//                return InteractionResult.SUCCESS;
            }
        }
        if (blockEntity.isInventory()) {
            return MenuManager.openMenu(ModMenuTypes.SKINNABLE, level.getBlockEntity(blockPos), player);
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, BlockPlaceContext context) {
        SkinBlockPlaceContext context1 = ObjectUtils.safeCast(context, SkinBlockPlaceContext.class);
        if (context1 == null) {
            return;
        }
        // add all part into level
        context1.getParts().forEach(it -> {
            BlockPos target = blockPos.offset(it.getOffset());
            level.setBlock(target, blockState, 11);
            SkinnableBlockEntity blockEntity = ObjectUtils.safeCast(level.getBlockEntity(target), SkinnableBlockEntity.class);
            if (blockEntity != null) {
                blockEntity.readFromNBT(it.getEntityTag());
                blockEntity.updateBlockStates();
            }
        });
    }

    @Override
    public void onRemove(Level level, BlockPos blockPos, BlockState oldBlockState, BlockState newBlockState, boolean bl) {
        // update the block state also calls `onRemove`.
        if (!newBlockState.is(oldBlockState.getBlock())) {
            this.brokenByAnything(level, blockPos, oldBlockState, null);
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState, LootContext context) {
        List<ItemStack> results = super.getDrops(blockState, context);
        SkinnableBlockEntity blockEntity = ObjectUtils.safeCast(context.blockEntity, SkinnableBlockEntity.class);
        if (blockEntity == null || results.isEmpty()) {
            return results;
        }
        ArrayList<ItemStack> fixedResults = new ArrayList<>(results.size());
        for (ItemStack itemStack : results) {
            // we will add an invalid skin item from loot table at data pack,
            // so we need fix the skin info in the drop event.
            if (itemStack.is(ModItems.SKIN.get()) && SkinDescriptor.of(itemStack).isEmpty()) {
                // when not found any dropped stack,
                // we must be remove invalid skin item.
                itemStack = blockEntity.getDropped();
                if (itemStack == null) {
                    continue;
                }
            }
            fixedResults.add(itemStack);
        }
        return fixedResults;
    }

    @Override
    public VoxelShape getCollisionShape(Level level, BlockPos blockPos, BlockState blockState) {
        SkinnableBlockEntity blockEntity = getBlockEntity(level, blockPos);
        if (blockEntity != null) {
            return blockEntity.getShape();
        }
        return VoxelShape.EMPTY;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FACE, LIT, PART, OCCUPIED);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntityTypes.SKINNABLE.get().create(pos, state);
    }

    @Override
    public boolean isLadder(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity) {
        SkinnableBlockEntity blockEntity = getBlockEntity(level, pos);
        if (blockEntity != null) {
            return blockEntity.isLadder();
        }
        return false;
    }

    public void forEach(Level level, BlockPos pos, Consumer<BlockPos> consumer) {
        SkinnableBlockEntity blockEntity = getParentBlockEntity(level, pos);
        if (blockEntity == null) {
            return;
        }
        BlockPos parentPos = blockEntity.getBlockPos();
        for (BlockPos offset : blockEntity.getRefers()) {
            BlockPos targetPos = parentPos.offset(offset);
            if (!targetPos.equals(pos)) {
                consumer.accept(targetPos);
            }
        }
    }

    public void brokenByAnything(Level level, BlockPos blockPos, BlockState blockState, @Nullable Player player) {
        if (dropItems(level, blockPos, player)) {
            killSeatEntities(level, blockPos);
            forEach(level, blockPos, target -> level.setBlock(target, Blocks.AIR.defaultBlockState(), 35));
        }
    }

    public void killSeatEntities(Level level, BlockPos blockPos) {
//        SkinnableBlockEntity blockEntity = getParentBlockEntity(level, blockPos);
//        if (blockEntity != null) {
//            Vector3d seatPos = blockEntity.getSeatPos().add(0.5f, 0.5f, 0.5f);
//            killSeatEntity(level, blockEntity.getParentPos(), seatPos);
//        }
    }

    public boolean dropItems(Level level, BlockPos blockPos, @Nullable Player player) {
        SkinnableBlockEntity blockEntity = getBlockEntity(level, blockPos);
        SkinnableBlockEntity parentBlockEntity = getParentBlockEntity(level, blockPos);
        if (blockEntity == null || parentBlockEntity == null || parentBlockEntity.isDropped()) {
            return false;
        }
        // anyway, we only drop all items once.
        ItemStack droppedStack = parentBlockEntity.getDescriptor().asItemStack();
        blockEntity.setDropped(droppedStack); // mark the attacked block
        parentBlockEntity.setDropped(droppedStack);
        if (parentBlockEntity.isInventory()) {
            UpdatableContainerBlockEntity.dropContainerIfNeeded(level, blockPos, parentBlockEntity);
        }
        return true;
    }

    private SkinnableBlockEntity getBlockEntity(Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof SkinnableBlockEntity) {
            return (SkinnableBlockEntity) blockEntity;
        }
        return null;
    }

    private SkinnableBlockEntity getParentBlockEntity(Level level, BlockPos blockPos) {
        SkinnableBlockEntity blockEntity = getBlockEntity(level, blockPos);
        if (blockEntity != null) {
            return blockEntity.getParent();
        }
        return null;
    }
}
