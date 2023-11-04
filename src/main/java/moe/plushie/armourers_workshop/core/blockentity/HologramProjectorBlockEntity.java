package moe.plushie.armourers_workshop.core.blockentity;

import moe.plushie.armourers_workshop.core.block.HologramProjectorBlock;
import moe.plushie.armourers_workshop.core.skin.SkinDescriptor;
import moe.plushie.armourers_workshop.utils.OptionalCompoundTag;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.BlockEntityType;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.math.Vector3f;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.utils.ContainerHelper;
import net.cocoonmc.core.utils.NonNullList;
import net.cocoonmc.core.world.Level;

public class HologramProjectorBlockEntity extends UpdatableContainerBlockEntity {

    private int powerMode = 0;
    private float modelScale = 1.0f;

    private boolean isGlowing = true;
    private boolean isPowered = false;
    private boolean showRotationPoint = false;

    private Vector3f modelAngle = Vector3f.ZERO;
    private Vector3f modelOffset = Vector3f.ZERO;

    private Vector3f rotationSpeed = Vector3f.ZERO;
    private Vector3f rotationOffset = Vector3f.ZERO;

    private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    public HologramProjectorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        ContainerHelper.loadAllItems(tag, items);
        OptionalCompoundTag tag1 = new OptionalCompoundTag(tag);
        modelAngle = tag1.getOptionalVector3f("Angle", Vector3f.ZERO);
        modelOffset = tag1.getOptionalVector3f("Offset", Vector3f.ZERO);
        rotationSpeed = tag1.getOptionalVector3f("RotSpeed", Vector3f.ZERO);
        rotationOffset = tag1.getOptionalVector3f("RotOffset", Vector3f.ZERO);
        isGlowing = tag1.getOptionalBoolean("Glowing", true);
        isPowered = tag1.getOptionalBoolean("Powered", false);
        modelScale = tag1.getOptionalFloat("Scale", 1.0f);
        powerMode = tag1.getOptionalInt("PowerMode", 0);
    }

    @Override
    public void writeToNBT(CompoundTag tag) {
        ContainerHelper.saveAllItems(tag, items);
        OptionalCompoundTag tag1 = new OptionalCompoundTag(tag);
        tag1.putOptionalVector3f("Angle", modelAngle, Vector3f.ZERO);
        tag1.putOptionalVector3f("Offset", modelOffset, Vector3f.ZERO);
        tag1.putOptionalVector3f("RotSpeed", rotationSpeed, Vector3f.ZERO);
        tag1.putOptionalVector3f("RotOffset", rotationOffset, Vector3f.ZERO);
        tag1.putOptionalBoolean("Glowing", isGlowing, true);
        tag1.putOptionalBoolean("Powered", isPowered, false);
        tag1.putOptionalFloat("Scale", modelScale, 1.0f);
        tag1.putOptionalInt("PowerMode", powerMode, 0);
    }

    public void updatePowerStats() {
        boolean newValue = isRunningForState(getBlockState());
        if (newValue != isPowered) {
            updateBlockStates();
        }
    }

    public void updateBlockStates() {
        BlockState state = getBlockState();
        isPowered = isRunningForState(state);
        boolean growing = isPowered && isGlowing;
        if (state.getValue(HologramProjectorBlock.LIT) != growing) {
            state = state.setValue(HologramProjectorBlock.LIT, growing);
            level.setBlock(blockPos, state, 3);
        } else {
            level.sendBlockUpdated(blockPos, state, state, 3);
        }
        setChanged();
    }

    public int getPowerMode() {
        return powerMode;
    }

    public void setPowerMode(int powerMode) {
        this.powerMode = powerMode;
        this.updateBlockStates();
    }

    protected boolean isRunningForState(BlockState state) {
        if (!SkinDescriptor.of(getItem(0)).isEmpty()) {
            switch (powerMode) {
                case 1:
                    return level.hasNeighborSignal(getBlockPos());
                case 2:
                    return !level.hasNeighborSignal(getBlockPos());
                default:
                    return true;
            }
        }
        return false;
    }

    public boolean isGlowing() {
        return isGlowing;
    }

    public void setGlowing(boolean glowing) {
        this.isGlowing = glowing;
        this.updateBlockStates();
    }

    public void setShowRotationPoint(boolean showRotationPoint) {
        this.showRotationPoint = showRotationPoint;
    }

    public boolean shouldShowRotationPoint() {
        return showRotationPoint;
    }

    public Vector3f getRotationSpeed() {
        return this.rotationSpeed;
    }

    public void setRotationSpeed(Vector3f rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
        this.updateBlockStates();
    }

    public Vector3f getRotationOffset() {
        return this.rotationOffset;
    }

    public void setRotationOffset(Vector3f rotationOffset) {
        this.rotationOffset = rotationOffset;
        this.updateBlockStates();
    }

    public Vector3f getModelOffset() {
        return this.modelOffset;
    }

    public void setModelOffset(Vector3f modelOffset) {
        this.modelOffset = modelOffset;
        this.updateBlockStates();
    }

    public Vector3f getModelAngle() {
        return this.modelAngle;
    }

    public void setModelAngle(Vector3f modelAngle) {
        this.modelAngle = modelAngle;
        this.updateBlockStates();
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public void setContainerChanged() {
        super.setContainerChanged();
        this.updateBlockStates();
    }

    @Override
    public CompoundTag getUpdateTag() {
        return serializeNBT();
    }
}
