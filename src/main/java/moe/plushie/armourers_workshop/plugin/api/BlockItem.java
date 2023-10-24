package moe.plushie.armourers_workshop.plugin.api;

import moe.plushie.armourers_workshop.plugin.utils.BukkitUtils;
import org.jetbrains.annotations.Nullable;

public class BlockItem extends Item {

    private final Block block;

    public BlockItem(Block block, Properties properties) {
        super(properties);
        this.block = block;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
//        InteractionResult interactionresult = this.place(new BlockPlaceContext(arg));
//        if (!interactionresult.consumesAction() && this.isEdible()) {
//            InteractionResult interactionresult1 = this.use(arg.getLevel(), arg.getPlayer(), arg.getHand()).getResult();
//            return interactionresult1 == InteractionResult.CONSUME ? InteractionResult.CONSUME_PARTIAL : interactionresult1;
//        }
//        return interactionresult;
        return place(new BlockPlaceContext(context));
    }

    public InteractionResult place(BlockPlaceContext context) {
//        if (!this.getBlock().isEnabled(arg.getLevel().enabledFeatures())) {
//            return InteractionResult.FAIL;
//        }
//        if (!arg.canPlace()) {
//            return InteractionResult.FAIL;
//        }
//        BlockPlaceContext blockplacecontext = this.updatePlacementContext(arg);
//        if (blockplacecontext == null) {
//            return InteractionResult.FAIL;
//        }
//        BlockState blockstate = this.getPlacementState(blockplacecontext);
//        if (blockstate == null) {
//            return InteractionResult.FAIL;
//        }
//        if (!this.placeBlock(blockplacecontext, blockstate)) {
//            return InteractionResult.FAIL;
//        }
//        BlockPos blockpos = blockplacecontext.getClickedPos();
//        Level level = blockplacecontext.getLevel();
//        Player player = blockplacecontext.getPlayer();
//        ItemStack itemstack = blockplacecontext.getItemInHand();
//        BlockState blockstate1 = level.getBlockState(blockpos);
//        if (blockstate1.is(blockstate.getBlock())) {
//            blockstate1 = this.updateBlockStateFromTag(blockpos, level, itemstack, blockstate1);
//            this.updateCustomBlockEntityTag(blockpos, level, player, itemstack, blockstate1);
//            blockstate1.getBlock().setPlacedBy(level, blockpos, blockstate1, player, itemstack);
//            if (player instanceof ServerPlayer) {
//                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockpos, itemstack);
//            }
//        }
//        SoundType soundtype = blockstate1.getSoundType(level, blockpos, arg.getPlayer());
//        level.playSound(player, blockpos, this.getPlaceSound(blockstate1, level, blockpos, arg.getPlayer()), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0f) / 2.0f, soundtype.getPitch() * 0.8f);
//        level.gameEvent(GameEvent.BLOCK_PLACE, blockpos, GameEvent.Context.of(player, blockstate1));
//        if (player == null || !player.getAbilities().instabuild) {
//            itemstack.shrink(1);
//        }
//        return InteractionResult.sidedSuccess(level.isClientSide);

        BlockState blockState = getPlacementState(context);


        BukkitUtils.setBlockAndUpdate(context, block.getKey().toString(), blockState, null);

//        Block target = context.getClickedBlock().getRelative(context.getClickedBlockFace());
//        if (!target.getType().isAir()) {
//            // target already exists a block.
//            return InteractionResult.FAIL;
//        }
//        BukkitUtils.setBlockAndUpdate(target, blockId, state, null);



//        CompoundTag bs = new CompoundTag();
//        bs.putString("lit", "true");
//        bs.putString("facing", "south");
//        bs.putString("face", "floor");
//
//        ListTag<CompoundTag> items = new ListTag<>(CompoundTag.class);
//        CompoundTag item = new SkinDescriptor("db:QueQGz91xn", SkinTypes.OUTFIT).asItemStack().save(new CompoundTag());
//        item.putByte("Slot", (byte) 0);
//        items.add(item);
//
//        CompoundTag bt = new CompoundTag();
//        bt.putByte("Powered", (byte) 1);
//        bt.put("Items", items);
//
//        BukkitUtils.setRedirectedBlockAndUpdate(context.getClickedBlock(), "armourers_workshop:hologram-projector", bs, bt);

        return InteractionResult.SUCCESS;
    }

    @Nullable
    protected BlockState getPlacementState(BlockPlaceContext arg) {
        BlockState blockState = getBlock().getStateForPlacement(arg);
//        return blockState != null && this.canPlace(arg, blockState) ? blockState : null;
        return blockState;
    }

    public Block getBlock() {
        return block;
    }
}
