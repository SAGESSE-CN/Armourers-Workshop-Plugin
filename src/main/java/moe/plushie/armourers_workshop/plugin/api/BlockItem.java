package moe.plushie.armourers_workshop.plugin.api;

import moe.plushie.armourers_workshop.plugin.utils.BukkitUtils;
import net.querz.nbt.tag.CompoundTag;

public class BlockItem extends Item {

    private final String blockId;

    public BlockItem(String blockId, Properties properties) {
        super(properties);
        this.blockId = blockId;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
//        ItemStack itemStack = context.getItem();

        CompoundTag state = new CompoundTag();

        String name = context.getPlayer().getFacing().getOppositeFace().toString();
        state.putString("facing", name.toLowerCase());

        BukkitUtils.setBlockAndUpdate(context, blockId, state, null);

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
}
