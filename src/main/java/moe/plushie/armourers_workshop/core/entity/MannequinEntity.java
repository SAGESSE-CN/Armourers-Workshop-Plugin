package moe.plushie.armourers_workshop.core.entity;

import moe.plushie.armourers_workshop.core.skin.SkinWardrobe;
import moe.plushie.armourers_workshop.init.ModMenuTypes;
import moe.plushie.armourers_workshop.init.platform.MenuManager;
import net.cocoonmc.core.math.Vector3d;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.core.world.InteractionResult;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.EntityType;
import net.cocoonmc.core.world.entity.LivingEntity;
import net.cocoonmc.core.world.entity.Player;

public class MannequinEntity extends LivingEntity {

    public MannequinEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
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
}
