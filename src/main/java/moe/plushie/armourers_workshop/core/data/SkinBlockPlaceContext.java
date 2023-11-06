package moe.plushie.armourers_workshop.core.data;

import moe.plushie.armourers_workshop.core.blockentity.SkinnableBlockEntity;
import moe.plushie.armourers_workshop.core.skin.Skin;
import moe.plushie.armourers_workshop.core.skin.SkinDescriptor;
import moe.plushie.armourers_workshop.core.skin.SkinLoader;
import moe.plushie.armourers_workshop.core.skin.SkinProperties;
import moe.plushie.armourers_workshop.core.skin.SkinProperty;
import moe.plushie.armourers_workshop.core.skin.SkinTypes;
import moe.plushie.armourers_workshop.init.ModBlocks;
import moe.plushie.armourers_workshop.utils.OptionalCompoundTag;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.item.context.BlockHitResult;
import net.cocoonmc.core.item.context.BlockPlaceContext;
import net.cocoonmc.core.item.context.UseOnContext;
import net.cocoonmc.core.math.Quaternionf;
import net.cocoonmc.core.math.Rectangle3f;
import net.cocoonmc.core.math.Rectangle3i;
import net.cocoonmc.core.math.Vector3f;
import net.cocoonmc.core.math.Vector4f;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.core.world.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

public class SkinBlockPlaceContext extends BlockPlaceContext {

    private Vector3f rotations = Vector3f.ZERO;
    private SkinDescriptor skin = SkinDescriptor.EMPTY;
    private ArrayList<Part> parts = new ArrayList<>();

    private SkinProperties properties;

    public SkinBlockPlaceContext(UseOnContext context) {
        super(context);
        this.loadElements(SkinLoader.getInstance()::loadSkin);
    }

    public SkinBlockPlaceContext(Player player, InteractionHand hand, ItemStack itemStack, BlockHitResult traceResult) {
        super(player.getLevel(), player, hand, itemStack, traceResult);
        this.loadElements(SkinLoader.getInstance()::getSkin);
    }

    protected void transform(Vector3f r) {
        for (Part part : parts) {
            part.transform(r);
        }
    }

    protected void loadElements(Function<String, Skin> provider) {
        ItemStack itemStack = getItemInHand();
        SkinDescriptor descriptor = SkinDescriptor.of(itemStack);
        if (descriptor.isEmpty()) {
            return;
        }
        Skin skin = provider.apply(descriptor.getIdentifier());
        if (skin == null) {
            return;
        }
        ArrayList<Part> parts = new ArrayList<>();
        ArrayList<BlockPos> blockPosList = new ArrayList<>();
        skin.getBlockBounds().forEach((pos, shape) -> {
            if (pos.equals(BlockPos.ZERO)) {
                parts.add(new ParentPart(pos, shape, blockPosList, descriptor, skin));
            } else {
                parts.add(new Part(pos, shape));
            }
        });
        this.skin = descriptor;
        this.parts = parts;
        this.properties = skin.getProperties();
        BlockState state = ModBlocks.SKINNABLE.getStateForPlacement(this);
        if (state != null) {
            this.rotations = SkinnableBlockEntity.getRotations(state);
            this.transform(rotations);
        }
        // copy all transformed block pose into list.
        for (Part part : parts) {
            blockPosList.add(part.getOffset());
        }
    }

    public <V> V getProperty(SkinProperty<V> property) {
        if (properties != null && !properties.isEmpty()) {
            return properties.get(property);
        }
        return property.getDefaultValue();
    }

    public boolean canPlace(Part part) {
        if (skin.isEmpty()) {
            return false;
        }
        if (skin.getType() != SkinTypes.BLOCK) {
            return false;
        }
        BlockPos pos = super.getClickedPos().offset(part.getOffset());
        return accessor.canBeReplaced(level, pos);
    }

    @Override
    public boolean canPlace() {
        return parts != null && parts.stream().allMatch(this::canPlace) && super.canPlace();
    }

    public SkinDescriptor getSkin() {
        return skin;
    }

    public ArrayList<Part> getParts() {
        return parts;
    }

    public static class Part {

        private BlockPos offset;
        private Rectangle3i shape;

        public Part() {
            this(BlockPos.ZERO, Rectangle3i.ZERO);
        }

        public Part(BlockPos offset, Rectangle3i shape) {
            this.offset = offset;
            this.shape = shape;
        }

        public void writeToNBT(OptionalCompoundTag tag) {
            tag.putOptionalBlockPos("Refer", offset, null);
            tag.putOptionalRectangle3i("Shape", shape, null);
        }

        public void transform(Vector3f r) {
            Quaternionf q = new Quaternionf(r.getX(), r.getY(), r.getZ(), true);
            Vector4f f = new Vector4f(offset.getX(), offset.getY(), offset.getZ(), 1.0f);
            f.transform(q);
            offset = new BlockPos(Math.round(f.x()), Math.round(f.y()), Math.round(f.z()));

            Rectangle3f of = new Rectangle3f(shape);
            of.mul(q);
            shape = new Rectangle3i(0, 0, 0, 0, 0, 0);
            shape.setX(Math.round(of.getX()));
            shape.setY(Math.round(of.getY()));
            shape.setZ(Math.round(of.getZ()));
            shape.setWidth(Math.round(of.getWidth()));
            shape.setHeight(Math.round(of.getHeight()));
            shape.setDepth(Math.round(of.getDepth()));
        }

        public BlockPos getOffset() {
            return offset;
        }

        public Rectangle3i getShape() {
            return shape;
        }

        public CompoundTag getEntityTag() {
            CompoundTag tag = CompoundTag.newInstance();
            writeToNBT(new OptionalCompoundTag(tag));
            return tag;
        }
    }

    public static class ParentPart extends Part {

        private final SkinDescriptor descriptor;
        private final SkinProperties properties;
        private final Collection<BlockPos> blockPosList;
//        private Collection<SkinMarker> markerList;

        public ParentPart(BlockPos offset, Rectangle3i shape, Collection<BlockPos> blockPosList, SkinDescriptor descriptor, Skin skin) {
            super(offset, shape);
            this.descriptor = descriptor;
            this.blockPosList = blockPosList;
            this.properties = skin.getProperties();
//            this.markerList = skin.getMarkers();
        }

        @Override
        public void writeToNBT(OptionalCompoundTag tag) {
            super.writeToNBT(tag);
            tag.putOptionalBlockPosArray("Refers", blockPosList);
            //tag.putOptionalSkinMarkerArray(Markers, markerList);
            tag.putOptionalSkinDescriptor("Skin", descriptor);
            tag.putOptionalSkinProperties("SkinProperties", properties);
        }

        @Override
        public void transform(Vector3f r) {
            super.transform(r);
//            OpenQuaternionf q = new OpenQuaternionf(r.getX(), r.getY(), r.getZ(), true);
//            ArrayList<SkinMarker> newMarkerList = new ArrayList<>();
//            for (SkinMarker marker : markerList) {
//                Vector4f f = new Vector4f(marker.x, marker.y, marker.z, 1.0f);
//                f.transform(OpenMatrix4f.createScaleMatrix(-1, -1, 1));
//                f.transform(q);
//                int x = Math.round(f.x());
//                int y = Math.round(f.y());
//                int z = Math.round(f.z());
//                marker = new SkinMarker((byte) x, (byte) y, (byte) z, marker.meta);
//                newMarkerList.add(marker);
//            }
//            this.markerList = newMarkerList;
        }
    }
}
