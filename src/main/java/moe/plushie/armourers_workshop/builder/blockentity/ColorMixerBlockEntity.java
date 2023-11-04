package moe.plushie.armourers_workshop.builder.blockentity;

import moe.plushie.armourers_workshop.core.skin.color.PaintColor;
import moe.plushie.armourers_workshop.utils.OptionalCompoundTag;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.BlockEntity;
import net.cocoonmc.core.block.BlockEntityType;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.world.Level;

public class ColorMixerBlockEntity extends BlockEntity {

    private PaintColor color = PaintColor.WHITE;

    public ColorMixerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void readFromNBT(CompoundTag nbt) {
        OptionalCompoundTag tag = new OptionalCompoundTag(nbt);
        color = tag.getOptionalPaintColor("Color", PaintColor.WHITE);
    }

    @Override
    public void writeToNBT(CompoundTag nbt) {
        OptionalCompoundTag tag = new OptionalCompoundTag(nbt);
        tag.putOptionalPaintColor("Color", color, PaintColor.WHITE);
    }

    public PaintColor getColor() {
        return color;
    }

    public void setColor(PaintColor color) {
        this.color = color;
        this.setChanged();
        this.sendBlockUpdates();
    }

    public void sendBlockUpdates() {
        if (level != null) {
            BlockState state = getBlockState();
            level.sendBlockUpdated(getBlockPos(), state, state, 3);
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        return serializeNBT();
    }
}
