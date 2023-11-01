package moe.plushie.armourers_workshop.builder.blockentity;

import moe.plushie.armourers_workshop.core.skin.color.PaintColor;
import moe.plushie.armourers_workshop.utils.OptionalCompoundTag;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.BlockEntity;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.world.Level;

public class ColorMixerBlockEntity extends BlockEntity {

    private PaintColor color = PaintColor.WHITE;

    public ColorMixerBlockEntity(Level level, BlockPos pos, BlockState blockState) {
        super(level, pos, blockState);
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
        Level level = getLevel();
        if (level != null) {
            BlockState state = getBlockState();
            level.sendBlockUpdated(getBlockPos(), state, state, 3);
        }
    }
}
