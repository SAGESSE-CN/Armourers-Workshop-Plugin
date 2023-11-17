package moe.plushie.armourers_workshop.core.entity;

import moe.plushie.armourers_workshop.core.skin.SkinWardrobe;
import moe.plushie.armourers_workshop.core.texture.PlayerTextureDescriptor;
import moe.plushie.armourers_workshop.init.ModEntitySerializers;
import moe.plushie.armourers_workshop.init.ModMenuTypes;
import moe.plushie.armourers_workshop.init.platform.MenuManager;
import moe.plushie.armourers_workshop.utils.OptionalCompoundTag;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.math.Rotations;
import net.cocoonmc.core.math.Vector3d;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.network.syncher.EntityDataAccessor;
import net.cocoonmc.core.network.syncher.SynchedEntityData;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.core.world.InteractionResult;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.EntityType;
import net.cocoonmc.core.world.entity.LivingEntity;
import net.cocoonmc.core.world.entity.Player;

public class MannequinEntity extends LivingEntity {

    public static final Rotations DEFAULT_HEAD_POSE = new Rotations(0.0f, 0.0f, 0.0f);
    public static final Rotations DEFAULT_BODY_POSE = new Rotations(0.0f, 0.0f, 0.0f);
    public static final Rotations DEFAULT_LEFT_ARM_POSE = new Rotations(-10.0f, 0.0f, -10.0f);
    public static final Rotations DEFAULT_RIGHT_ARM_POSE = new Rotations(-15.0f, 0.0f, 10.0f);
    public static final Rotations DEFAULT_LEFT_LEG_POSE = new Rotations(-1.0f, 0.0f, -1.0f);
    public static final Rotations DEFAULT_RIGHT_LEG_POSE = new Rotations(1.0f, 0.0f, 1.0f);

    public static final EntityDataAccessor<Boolean> DATA_IS_CHILD = SynchedEntityData.defineId(MannequinEntity.class, -1, ModEntitySerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> DATA_IS_FLYING = SynchedEntityData.defineId(MannequinEntity.class, -1, ModEntitySerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> DATA_IS_GHOST = SynchedEntityData.defineId(MannequinEntity.class, -1, ModEntitySerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> DATA_IS_VISIBLE = SynchedEntityData.defineId(MannequinEntity.class, -1, ModEntitySerializers.BOOLEAN);
    public static final EntityDataAccessor<Float> DATA_SCALE = SynchedEntityData.defineId(MannequinEntity.class, -1, ModEntitySerializers.FLOAT);
    public static final EntityDataAccessor<Boolean> DATA_EXTRA_RENDERER = SynchedEntityData.defineId(MannequinEntity.class, -1, ModEntitySerializers.BOOLEAN);
    public static final EntityDataAccessor<PlayerTextureDescriptor> DATA_TEXTURE = SynchedEntityData.defineId(MannequinEntity.class, -1, ModEntitySerializers.PLAYER_TEXTURE);

    private org.bukkit.entity.ArmorStand delegate;

    public MannequinEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_CHILD, false);
        this.entityData.define(DATA_IS_FLYING, false);
        this.entityData.define(DATA_IS_GHOST, false);
        this.entityData.define(DATA_IS_VISIBLE, true);
        this.entityData.define(DATA_EXTRA_RENDERER, true);
        this.entityData.define(DATA_SCALE, 1.0f);
        this.entityData.define(DATA_TEXTURE, PlayerTextureDescriptor.EMPTY);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> dataParameter) {
        super.onSyncedDataUpdated(dataParameter);
        if (dataParameter == DATA_IS_FLYING) {
            delegate.setGravity(!entityData.get(DATA_IS_FLYING));
        }
        if (dataParameter == DATA_IS_GHOST) {
            delegate.setCollidable(entityData.get(DATA_IS_GHOST));
        }
        if (dataParameter == DATA_IS_VISIBLE) {
            delegate.setVisible(entityData.get(DATA_IS_VISIBLE));
        }
    }

    @Override
    public InteractionResult interactAt(Player player, Vector3d position, InteractionHand interactionHand) {
//        if (isMarker()) {
//            return InteractionResult.PASS;
//        }
//        ItemStack itemStack = player.getItemInHand(hand);
//        if (itemStack.is(ModItems.MANNEQUIN_TOOL.get())) {
//            return InteractionResult.PASS;
//        }
//        if (itemStack.is(Items.NAME_TAG)) {
//            Component customName = null;
//            if (itemStack.hasCustomHoverName() && !player.isShiftKeyDown()) {
//                customName = itemStack.getHoverName();
//            }
//            setCustomName(customName);
//            return InteractionResult.sidedSuccess(getLevel().isClientSide());
//        }
//        if (player.isShiftKeyDown()) {
//            double ry = TrigUtils.getAngleDegrees(player.getX(), player.getZ(), getX(), getZ()) + 90.0;
//            Rotations rotations = getBodyPose();
//            float yRot = this.getYRot();
//            setBodyPose(new Rotations(rotations.getX(), (float) ry - yRot, rotations.getZ()));
//            return InteractionResult.sidedSuccess(getLevel().isClientSide());
//        }
        SkinWardrobe wardrobe = SkinWardrobe.of(this);
        if (wardrobe != null && wardrobe.isEditable(player)) {
            MenuManager.openMenu(ModMenuTypes.WARDROBE, player, wardrobe);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.readExtendedData(tag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        this.addExtendedData(tag);
    }


    public void setHeadPose(Rotations rotations) {
        this.delegate.setHeadPose(rotations.asBukkit());
    }

    public void setBodyPose(Rotations rotations) {
        this.delegate.setBodyPose(rotations.asBukkit());
    }

    public void setLeftArmPose(Rotations rotations) {
        this.delegate.setLeftArmPose(rotations.asBukkit());
    }

    public void setRightArmPose(Rotations rotations) {
        this.delegate.setRightArmPose(rotations.asBukkit());
    }

    public void setLeftLegPose(Rotations rotations) {
        this.delegate.setLeftLegPose(rotations.asBukkit());
    }

    public void setRightLegPose(Rotations rotations) {
        this.delegate.setRightLegPose(rotations.asBukkit());
    }

    public Rotations getHeadPose() {
        return Rotations.of(this.delegate.getHeadPose());
    }

    public Rotations getBodyPose() {
        return Rotations.of(this.delegate.getBodyPose());
    }

    public Rotations getLeftArmPose() {
        return Rotations.of(this.delegate.getLeftArmPose());
    }

    public Rotations getRightArmPose() {
        return Rotations.of(this.delegate.getRightArmPose());
    }

    public Rotations getLeftLegPose() {
        return Rotations.of(this.delegate.getLeftLegPose());
    }

    public Rotations getRightLegPose() {
        return Rotations.of(this.delegate.getRightLegPose());
    }

    @Override
    public void setDelegate(org.bukkit.entity.Entity delegate) {
        super.setDelegate(delegate);
        this.delegate = (org.bukkit.entity.ArmorStand) delegate;
    }

    @Override
    public org.bukkit.entity.ArmorStand asBukkit() {
        return delegate;
    }

    @Override
    public void sendToBukkit() {
        super.sendToBukkit();
        this.delegate.setGravity(!entityData.get(DATA_IS_FLYING));
        this.delegate.setCollidable(entityData.get(DATA_IS_GHOST));
        this.delegate.setVisible(entityData.get(DATA_IS_VISIBLE));
    }


    public void readExtendedData(CompoundTag tag1) {
        OptionalCompoundTag tag = new OptionalCompoundTag(tag1);
        entityData.set(DATA_IS_CHILD, tag.getOptionalBoolean("Small", false));
        entityData.set(DATA_IS_FLYING, tag.getOptionalBoolean("Flying", false));
        entityData.set(DATA_IS_GHOST, tag.getOptionalBoolean("Ghost", false));
        entityData.set(DATA_IS_VISIBLE, tag.getOptionalBoolean("ModelVisible", true));
        entityData.set(DATA_EXTRA_RENDERER, tag.getOptionalBoolean("ExtraRender", true));

        entityData.set(DATA_SCALE, tag.getOptionalFloat("Scale", 1.0f));
        entityData.set(DATA_TEXTURE, tag.getOptionalTextureDescriptor("Texture", PlayerTextureDescriptor.EMPTY));

        readCustomPose(tag1.getCompound("Pose"));
    }

    public void addExtendedData(CompoundTag tag1) {
        OptionalCompoundTag tag = new OptionalCompoundTag(tag1);
        tag.putOptionalBoolean("Small", entityData.get(DATA_IS_CHILD), false);
        tag.putOptionalBoolean("Flying", entityData.get(DATA_IS_FLYING), false);
        tag.putOptionalBoolean("Ghost", entityData.get(DATA_IS_GHOST), false);
        tag.putOptionalBoolean("ModelVisible", entityData.get(DATA_IS_VISIBLE), true);
        tag.putOptionalBoolean("ExtraRender", entityData.get(DATA_EXTRA_RENDERER), true);

        tag.putOptionalFloat("Scale", entityData.get(DATA_SCALE), 1.0f);
        tag.putOptionalTextureDescriptor("Texture", entityData.get(DATA_TEXTURE), PlayerTextureDescriptor.EMPTY);

        tag1.put("Pose", saveCustomPose());
    }

    public CompoundTag saveCustomPose() {
        CompoundTag tag1 = CompoundTag.newInstance();
        OptionalCompoundTag tag = new OptionalCompoundTag(tag1);
        tag.putOptionalRotations("Head", getHeadPose(), DEFAULT_HEAD_POSE);
        tag.putOptionalRotations("Body", getBodyPose(), DEFAULT_BODY_POSE);
        tag.putOptionalRotations("LeftArm", getLeftArmPose(), DEFAULT_LEFT_ARM_POSE);
        tag.putOptionalRotations("RightArm", getRightArmPose(), DEFAULT_RIGHT_ARM_POSE);
        tag.putOptionalRotations("LeftLeg", getLeftLegPose(), DEFAULT_LEFT_LEG_POSE);
        tag.putOptionalRotations("RightLeg", getRightLegPose(), DEFAULT_RIGHT_LEG_POSE);
        return tag1;
    }

    public void readCustomPose(CompoundTag tag1) {
        OptionalCompoundTag tag = new OptionalCompoundTag(tag1);
        setHeadPose(tag.getOptionalRotations("Head", DEFAULT_HEAD_POSE));
        setBodyPose(tag.getOptionalRotations("Body", DEFAULT_BODY_POSE));
        setLeftArmPose(tag.getOptionalRotations("LeftArm", DEFAULT_LEFT_ARM_POSE));
        setRightArmPose(tag.getOptionalRotations("RightArm", DEFAULT_RIGHT_ARM_POSE));
        setLeftLegPose(tag.getOptionalRotations("LeftLeg", DEFAULT_LEFT_LEG_POSE));
        setRightLegPose(tag.getOptionalRotations("RightLeg", DEFAULT_RIGHT_LEG_POSE));
    }

    public void saveMannequinToolData(CompoundTag entityTag) {
        addExtendedData(entityTag);
    }

    public void readMannequinToolData(CompoundTag entityTag, ItemStack itemStack) {
        CompoundTag newEntityTag = CompoundTag.newInstance();
//        if (MannequinToolOptions.CHANGE_OPTION.get(itemStack)) {
//            newEntityTag.merge(entityTag);
//            newEntityTag.remove(Constants.Key.ENTITY_SCALE);
//            newEntityTag.remove(Constants.Key.ENTITY_POSE);
//            newEntityTag.remove(Constants.Key.ENTITY_TEXTURE);
//        }
//        if (MannequinToolOptions.CHANGE_SCALE.get(itemStack)) {
//            auto oldValue = entityTag.get(Constants.Key.ENTITY_SCALE);
//            if (oldValue != null) {
//                newEntityTag.put(Constants.Key.ENTITY_SCALE, oldValue);
//            }
//        }
//        if (MannequinToolOptions.CHANGE_ROTATION.get(itemStack)) {
//            auto oldValue = entityTag.getCompound(Constants.Key.ENTITY_POSE);
//            if (MannequinToolOptions.MIRROR_MODE.get(itemStack) && !oldValue.isEmpty()) {
//                CompoundTag newPoseTag = oldValue.copy();
//                DataSerializers.mirrorRotations(oldValue, Constants.Key.ENTITY_POSE_HEAD, DEFAULT_HEAD_POSE, newPoseTag, Constants.Key.ENTITY_POSE_HEAD, DEFAULT_HEAD_POSE);
//                DataSerializers.mirrorRotations(oldValue, Constants.Key.ENTITY_POSE_BODY, DEFAULT_BODY_POSE, newPoseTag, Constants.Key.ENTITY_POSE_BODY, DEFAULT_BODY_POSE);
//                DataSerializers.mirrorRotations(oldValue, Constants.Key.ENTITY_POSE_RIGHT_ARM, DEFAULT_RIGHT_ARM_POSE, newPoseTag, Constants.Key.ENTITY_POSE_LEFT_ARM, DEFAULT_LEFT_ARM_POSE);
//                DataSerializers.mirrorRotations(oldValue, Constants.Key.ENTITY_POSE_LEFT_ARM, DEFAULT_LEFT_ARM_POSE, newPoseTag, Constants.Key.ENTITY_POSE_RIGHT_ARM, DEFAULT_RIGHT_ARM_POSE);
//                DataSerializers.mirrorRotations(oldValue, Constants.Key.ENTITY_POSE_RIGHT_LEG, DEFAULT_RIGHT_LEG_POSE, newPoseTag, Constants.Key.ENTITY_POSE_LEFT_LEG, DEFAULT_LEFT_LEG_POSE);
//                DataSerializers.mirrorRotations(oldValue, Constants.Key.ENTITY_POSE_LEFT_LEG, DEFAULT_LEFT_LEG_POSE, newPoseTag, Constants.Key.ENTITY_POSE_RIGHT_LEG, DEFAULT_RIGHT_LEG_POSE);
//                oldValue = newPoseTag;
//            }
//            newEntityTag.put(Constants.Key.ENTITY_POSE, oldValue);
//        }
//        if (MannequinToolOptions.CHANGE_TEXTURE.get(itemStack)) {
//            auto oldValue = entityTag.get(Constants.Key.ENTITY_TEXTURE);
//            if (oldValue != null) {
//                newEntityTag.put(Constants.Key.ENTITY_TEXTURE, oldValue);
//            }
//        }
        // load into entity
        readExtendedData(newEntityTag);
    }
}
