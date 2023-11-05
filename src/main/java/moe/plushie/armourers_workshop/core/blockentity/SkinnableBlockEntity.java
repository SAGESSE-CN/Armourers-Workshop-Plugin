package moe.plushie.armourers_workshop.core.blockentity;

import com.google.common.collect.ImmutableMap;
import moe.plushie.armourers_workshop.core.skin.SkinDescriptor;
import moe.plushie.armourers_workshop.core.skin.SkinProperties;
import moe.plushie.armourers_workshop.core.skin.SkinProperty;
import moe.plushie.armourers_workshop.utils.ObjectUtils;
import moe.plushie.armourers_workshop.utils.OptionalCompoundTag;
import moe.plushie.armourers_workshop.utils.Rectangle3i;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.Direction;
import net.cocoonmc.core.block.BlockEntityType;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.block.state.properties.AttachFace;
import net.cocoonmc.core.inventory.Container;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.math.Vector3f;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.utils.ContainerHelper;
import net.cocoonmc.core.utils.NonNullList;
import net.cocoonmc.core.utils.Pair;
import net.cocoonmc.core.world.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Function;

public class SkinnableBlockEntity extends UpdatableContainerBlockEntity {

    private static final BlockPos INVALID = BlockPos.of(-1);

    private static final ImmutableMap<?, Vector3f> FACING_TO_ROT = new ImmutableMap.Builder<Object, Vector3f>()
            .put(Pair.of(AttachFace.CEILING, Direction.EAST), new Vector3f(180, 270, 0))
            .put(Pair.of(AttachFace.CEILING, Direction.NORTH), new Vector3f(180, 180, 0))
            .put(Pair.of(AttachFace.CEILING, Direction.WEST), new Vector3f(180, 90, 0))
            .put(Pair.of(AttachFace.CEILING, Direction.SOUTH), new Vector3f(180, 0, 0))
            .put(Pair.of(AttachFace.WALL, Direction.EAST), new Vector3f(0, 270, 0))
            .put(Pair.of(AttachFace.WALL, Direction.SOUTH), new Vector3f(0, 180, 0))
            .put(Pair.of(AttachFace.WALL, Direction.WEST), new Vector3f(0, 90, 0))
            .put(Pair.of(AttachFace.WALL, Direction.NORTH), new Vector3f(0, 0, 0))
            .put(Pair.of(AttachFace.FLOOR, Direction.EAST), new Vector3f(0, 270, 0))
            .put(Pair.of(AttachFace.FLOOR, Direction.SOUTH), new Vector3f(0, 180, 0))
            .put(Pair.of(AttachFace.FLOOR, Direction.WEST), new Vector3f(0, 90, 0))
            .put(Pair.of(AttachFace.FLOOR, Direction.NORTH), new Vector3f(0, 0, 0))
            .build();

    private BlockPos refer = INVALID;
    private Rectangle3i shape = Rectangle3i.ZERO;

    private NonNullList<ItemStack> items;
    private Collection<BlockPos> refers;
//    private Collection<SkinMarker> markers;

    private BlockPos linkedBlockPos = null;

    private SkinProperties properties;
    private SkinDescriptor descriptor = SkinDescriptor.EMPTY;

    private ItemStack droppedStack = null;

    private boolean isParent = false;

    public SkinnableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

//    public static Vector3f getRotations(BlockState state) {
//        AttachFace face = state.getOptionalValue(SkinnableBlock.FACE).orElse(AttachFace.FLOOR);
//        Direction facing = state.getOptionalValue(SkinnableBlock.FACING).orElse(Direction.NORTH);
//        return FACING_TO_ROT.getOrDefault(Pair.of(face, facing), Vector3f.ZERO);
//    }

    @Override
    public void readFromNBT(CompoundTag tag1) {
        OptionalCompoundTag tag = new OptionalCompoundTag(tag1);
        refer = tag.getOptionalBlockPos("Refer", INVALID);
        shape = tag.getOptionalRectangle3i("Shape", Rectangle3i.ZERO);
        isParent = BlockPos.ZERO.equals(refer);
        if (!isParent()) {
            return;
        }
        SkinProperties oldProperties = properties;
        refers = tag.getOptionalBlockPosArray("Refers");
        //markers = tag.getOptionalSkinMarkerArray("Markers");
        descriptor = tag.getOptionalSkinDescriptor("Skin");
        properties = tag.getOptionalSkinProperties("SkinProperties");
        linkedBlockPos = tag.getOptionalBlockPos("LinkedPos", null);
        if (oldProperties != null) {
            oldProperties.copyFrom(properties);
            properties = oldProperties;
        }
        ContainerHelper.loadAllItems(tag1, getOrCreateItems());
    }

    @Override
    public void writeToNBT(CompoundTag tag1) {
        OptionalCompoundTag tag = new OptionalCompoundTag(tag1);
        tag.putOptionalBlockPos("Refer", refer, INVALID);
        tag.putOptionalRectangle3i("Shape", shape, Rectangle3i.ZERO);
        if (!isParent()) {
            return;
        }
        tag.putOptionalBlockPosArray("Refers", refers);
        //tag.putOptionalSkinMarkerArray("Markers", markers);
        tag.putOptionalSkinDescriptor("Skin", descriptor);
        tag.putOptionalSkinProperties("SkinProperties", properties);
        tag.putOptionalBlockPos("LinkedPos", linkedBlockPos, null);

        ContainerHelper.saveAllItems(tag1, getOrCreateItems());
    }

    public void updateBlockStates() {
        setChanged();
        Level level = getLevel();
        if (level != null) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 11);
        }
    }

    public SkinDescriptor getDescriptor() {
        if (isParent()) {
            return descriptor;
        }
        return SkinDescriptor.EMPTY;
    }

    public void setDescriptor(SkinDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public BlockPos getLinkedBlockPos() {
        return getValueFromParent(te -> te.linkedBlockPos);
    }

    public void setLinkedBlockPos(BlockPos linkedBlockPos) {
        SkinnableBlockEntity blockEntity = getParent();
        if (blockEntity != null) {
            blockEntity.linkedBlockPos = linkedBlockPos;
            blockEntity.updateBlockStates();
        }
    }

    public void kill() {
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return getOrCreateItems();
    }

    @Override
    public int getContainerSize() {
        return 9 * 9;
    }

    @Nullable
    public String getInventoryName() {
        return getProperty(SkinProperty.ALL_CUSTOM_NAME);
    }

    @Nullable
    @Override
    public Container getInventory() {
        return getParent();
    }

    public Collection<BlockPos> getRefers() {
        if (refers == null) {
            refers = getValueFromParent(te -> te.refers);
        }
        return refers;
    }

    public BlockPos getParentPos() {
        return getBlockPos().subtract(refer);
    }

//    public Vector3d getSeatPos() {
//        float dx = 0, dy = 0, dz = 0;
//        BlockPos parentPos = getParentPos();
//        Collection<SkinMarker> markers = getMarkers();
//        if (markers != null && !markers.isEmpty()) {
//            SkinMarker marker = markers.iterator().next();
//            dx = marker.x / 16.0f;
//            dy = marker.y / 16.0f;
//            dz = marker.z / 16.0f;
//        }
//        return new Vector3d(parentPos.getX() + dx, parentPos.getY() + dy, parentPos.getZ() + dz);
//    }
//
//    public BlockPos getBedPos() {
//        BlockPos parentPos = getParentPos();
//        Collection<SkinMarker> markers = getMarkers();
//        if (markers == null || markers.isEmpty()) {
//            Direction facing = getBlockState().getOptionalValue(SkinnableBlock.FACING).orElse(Direction.NORTH);
//            return parentPos.relative(Rotation.CLOCKWISE_180.rotate(facing));
//        }
//        SkinMarker marker = markers.iterator().next();
//        return parentPos.offset(marker.x / 16, marker.y / 16, marker.z / 16);
//    }
//
//    public Collection<SkinMarker> getMarkers() {
//        if (markers == null) {
//            markers = getValueFromParent(te -> te.markers);
//        }
//        return markers;
//    }

    @Nullable
    public SkinProperties getProperties() {
        if (properties == null) {
            properties = getValueFromParent(te -> te.properties);
        }
        return properties;
    }

    @Nullable
    public SkinnableBlockEntity getParent() {
        if (isParent()) {
            return this;
        }
        if (getLevel() != null && refer != INVALID) {
            return ObjectUtils.safeCast(getLevel().getBlockEntity(getParentPos()), SkinnableBlockEntity.class);
        }
        return null;
    }

    public void setDropped(ItemStack itemStack) {
        this.droppedStack = itemStack;
    }

    public ItemStack getDropped() {
        return droppedStack;
    }

    public boolean isDropped() {
        return droppedStack != null;
    }

    public boolean isLadder() {
        return getProperty(SkinProperty.BLOCK_LADDER);
    }

    public boolean isGrowing() {
        return getProperty(SkinProperty.BLOCK_GLOWING);
    }

    public boolean isSeat() {
        return getProperty(SkinProperty.BLOCK_SEAT);
    }

    public boolean isBed() {
        return getProperty(SkinProperty.BLOCK_BED);
    }

    public boolean isLinked() {
        return getLinkedBlockPos() != null;
    }

    public boolean isInventory() {
        return getProperty(SkinProperty.BLOCK_INVENTORY) || isEnderInventory();
    }

    public boolean isEnderInventory() {
        return getProperty(SkinProperty.BLOCK_ENDER_INVENTORY);
    }

    public boolean isParent() {
        return isParent;
    }

    public boolean noCollision() {
        return getProperty(SkinProperty.BLOCK_NO_COLLISION);
    }

    public int getInventoryWidth() {
        return getProperty(SkinProperty.BLOCK_INVENTORY_WIDTH);
    }

    public int getInventoryHeight() {
        return getProperty(SkinProperty.BLOCK_INVENTORY_HEIGHT);
    }

    private NonNullList<ItemStack> getOrCreateItems() {
        if (items == null) {
            items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        }
        return items;
    }

    @Nullable
    private <V> V getValueFromParent(Function<SkinnableBlockEntity, V> getter) {
        SkinnableBlockEntity blockEntity = getParent();
        if (blockEntity != null) {
            return getter.apply(blockEntity);
        }
        return null;
    }

    private <V> V getProperty(SkinProperty<V> property) {
        SkinProperties properties = getProperties();
        if (properties != null) {
            return properties.get(property);
        }
        return property.getDefaultValue();
    }

    @Override
    public CompoundTag getUpdateTag() {
        return serializeNBT();
    }
}
