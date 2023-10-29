package moe.plushie.armourers_workshop.plugin.core.network;

import moe.plushie.armourers_workshop.plugin.api.IEntitySerializer;
import moe.plushie.armourers_workshop.plugin.api.IServerPacketHandler;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinSlotType;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinWardrobe;
import moe.plushie.armourers_workshop.plugin.init.ModItems;
import moe.plushie.armourers_workshop.plugin.init.ModLog;
import moe.plushie.armourers_workshop.plugin.utils.BukkitUtils;
import moe.plushie.armourers_workshop.plugin.utils.DataAccessor;
import moe.plushie.armourers_workshop.plugin.utils.DataSerializers;
import moe.plushie.armourers_workshop.plugin.utils.ObjectUtils;
import net.cocoonmc.core.inventory.Container;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.network.FriendlyByteBuf;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class UpdateWardrobePacket extends CustomPacket {

    private final Mode mode;
    private final int entityId;

    private final Field field;
    private final Object fieldValue;

    private final CompoundTag compoundNBT;

    public UpdateWardrobePacket(FriendlyByteBuf buffer) {
        this.mode = buffer.readEnum(Mode.class);
        this.entityId = buffer.readInt();
        if (this.mode != Mode.SYNC_OPTION) {
            this.fieldValue = null;
            this.field = null;
            this.compoundNBT = buffer.readNbt();
        } else {
            this.field = buffer.readEnum(Field.class);
            this.fieldValue = field.getDataSerializer().read(buffer);
            this.compoundNBT = null;
        }
    }

    public UpdateWardrobePacket(SkinWardrobe wardrobe, Mode mode, CompoundTag compoundNBT, Field field, Object fieldValue) {
        this.mode = mode;
        this.entityId = wardrobe.getId();
        this.field = field;
        this.fieldValue = fieldValue;
        this.compoundNBT = compoundNBT;
    }

    public static UpdateWardrobePacket sync(SkinWardrobe wardrobe) {
        return new UpdateWardrobePacket(wardrobe, Mode.SYNC, wardrobe.serializeNBT(), null, null);
    }

    public static UpdateWardrobePacket pick(SkinWardrobe wardrobe, int slot, ItemStack itemStack) {
        CompoundTag compoundNBT = CompoundTag.newInstance();
        compoundNBT.putInt("Slot", slot);
        compoundNBT.put("Item", itemStack.save(CompoundTag.newInstance()));
        return new UpdateWardrobePacket(wardrobe, Mode.SYNC_ITEM, compoundNBT, null, null);
    }

    public static UpdateWardrobePacket field(SkinWardrobe wardrobe, Field field, Object value) {
        return new UpdateWardrobePacket(wardrobe, Mode.SYNC_OPTION, null, field, value);
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeEnum(mode);
        buffer.writeInt(entityId);
        if (compoundNBT != null) {
            buffer.writeNbt(compoundNBT);
        }
        if (field != null) {
            buffer.writeEnum(field);
            field.getDataSerializer().write(buffer, fieldValue);
        }
    }

    @Override
    public void accept(IServerPacketHandler packetHandler, Player player) {
        // We can't allow wardrobe updates without container.
        String playerName = player.getDisplayName();
//        if (!(player.containerMenu instanceof SkinWardrobeMenu)) {
//            ModLog.info("the wardrobe {} operation rejected for '{}'", field, playerName);
//            return;
//        }
        if (!checkSecurityByServer()) {
            ModLog.info("the wardrobe {} operation rejected for '{}', for security reasons.", field, playerName);
            return;
        }
        ModLog.debug("the wardrobe {} operation accepted for '{}'", field, playerName);
        SkinWardrobe wardrobe = apply(player);
        if (wardrobe != null) {
            NetworkManager.sendToTracking(this, player);
        }
    }

    @Nullable
    private SkinWardrobe apply(Player player) {
        Entity entity = BukkitUtils.findEntity(player.getWorld(), entityId);
        SkinWardrobe wardrobe = SkinWardrobe.of(entity);
        if (wardrobe == null) {
            return null;
        }
        switch (mode) {
            case SYNC: {
                wardrobe.deserializeNBT(compoundNBT);
                return wardrobe;
            }
            case SYNC_ITEM: {
                Container inventory = wardrobe.getInventory();
                int slot = compoundNBT.getInt("Slot");
                if (slot < inventory.getContainerSize()) {
                    inventory.setItem(slot, ItemStack.of(compoundNBT.getCompound("Item")));
                    wardrobe.save();
                    return wardrobe;
                }
                break;
            }
            case SYNC_OPTION: {
                if (field != null) {
                    field.set(wardrobe, fieldValue);
                    wardrobe.save();
                    return wardrobe;
                }
                break;
            }
        }
        return null;
    }

    private boolean checkSecurityByServer() {
        switch (mode) {
            case SYNC: {
                // the server side never accept sync request.
                return false;
            }
            case SYNC_ITEM: {
                int slot = compoundNBT.getInt("Slot");
                // for security reasons we need to check the position of the slot.
                int index = slot - SkinSlotType.DYE.getIndex();
                if (index < 8 || index >= SkinSlotType.DYE.getMaxSize()) {
                    return false;
                }
                // for security reasons we only allows the player upload the bottle item.
                ItemStack itemStack = ItemStack.of(compoundNBT.getCompound("Item"));
                if (itemStack.isEmpty()) {
                    return true;
                }
                return itemStack.getItem().equals(ModItems.BOTTLE);
            }
            case SYNC_OPTION: {
                return true;
            }
        }
        return true;
    }

    public enum Mode {
        SYNC, SYNC_ITEM, SYNC_OPTION
    }

    public enum Field {

        WARDROBE_ARMOUR_HEAD(EquipmentSlot.HEAD),
        WARDROBE_ARMOUR_CHEST(EquipmentSlot.CHEST),
        WARDROBE_ARMOUR_LEGS(EquipmentSlot.LEGS),
        WARDROBE_ARMOUR_FEET(EquipmentSlot.FEET),

        WARDROBE_EXTRA_RENDER(SkinWardrobe::shouldRenderExtra, SkinWardrobe::setRenderExtra),

        MANNEQUIN_IS_CHILD(null/*MannequinEntity.DATA_IS_CHILD*/),
        MANNEQUIN_IS_FLYING(null/*MannequinEntity.DATA_IS_FLYING*/),
        MANNEQUIN_IS_VISIBLE(null/*MannequinEntity.DATA_IS_VISIBLE*/),
        MANNEQUIN_IS_GHOST(null/*MannequinEntity.DATA_IS_GHOST*/),
        MANNEQUIN_EXTRA_RENDER(null/*MannequinEntity.DATA_EXTRA_RENDERER*/),

        MANNEQUIN_POSE(null/*DataSerializers.COMPOUND_TAG, MannequinEntity::saveCustomPose, MannequinEntity::readCustomPose*/),
        MANNEQUIN_POSITION(null/*DataSerializers.VECTOR_3D, MannequinEntity::position, MannequinEntity::moveTo*/),

        MANNEQUIN_TEXTURE(null/*MannequinEntity.DATA_TEXTURE*/);

        private final boolean broadcastChanges;
        private final DataAccessor<SkinWardrobe, ?> dataAccessor;

        Field(EquipmentSlot slotType) {
            this(w -> w.shouldRenderEquipment(slotType), (w, v) -> w.setRenderEquipment(slotType, v));
        }

        Field(Function<SkinWardrobe, Boolean> supplier, BiConsumer<SkinWardrobe, Boolean> applier) {
            this.broadcastChanges = true;
            this.dataAccessor = DataAccessor
                    .withDataSerializer(SkinWardrobe.class, DataSerializers.BOOLEAN)
                    .withSupplier(supplier)
                    .withApplier(applier);
        }

        //        <S extends Entity, T> Field(IEntitySerializer<T> dataSerializer, Function<S, T> supplier, BiConsumer<S, T> applier) {
//            this.broadcastChanges = false;
//            this.dataAccessor = AWDataAccessor
//                    .withDataSerializer(SkinWardrobe.class, dataSerializer)
//                    .withSupplier((wardrobe) -> {
//                        if (wardrobe.getEntity() != null) {
//                            return supplier.apply(ObjectUtils.unsafeCast(wardrobe.getEntity()));
//                        }
//                        return null;
//                    })
//                    .withApplier((wardrobe, value) -> {
//                        if (wardrobe.getEntity() != null) {
//                            applier.accept(ObjectUtils.unsafeCast(wardrobe.getEntity()), value);
//                        }
//                    });
//        }
//
        <T> Field(T o/*EntityDataAccessor<T> dataParameter*/) {
            this.broadcastChanges = false;
            this.dataAccessor = null;
//            this(DataSerializers.of(dataParameter.getSerializer()), e -> e.getEntityData().get(dataParameter), (e, v) -> e.getEntityData().set(dataParameter, v));
        }

        public <T> void set(SkinWardrobe wardrobe, T value) {
            DataAccessor<SkinWardrobe, T> dataAccessor = getDataAccessor();
            dataAccessor.set(wardrobe, value);
        }

        public <T> T get(SkinWardrobe wardrobe, T defaultValue) {
            DataAccessor<SkinWardrobe, T> dataAccessor = getDataAccessor();
            T value = dataAccessor.get(wardrobe);
            if (value != null) {
                return value;
            }
            return defaultValue;
        }

        public <T> DataAccessor<SkinWardrobe, T> getDataAccessor() {
            return ObjectUtils.unsafeCast(dataAccessor);
        }

        public <T> IEntitySerializer<T> getDataSerializer() {
            DataAccessor<SkinWardrobe, T> dataAccessor = getDataAccessor();
            return dataAccessor.dataSerializer;
        }
    }
}
