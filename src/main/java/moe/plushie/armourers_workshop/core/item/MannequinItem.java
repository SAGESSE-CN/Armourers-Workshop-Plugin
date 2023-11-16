package moe.plushie.armourers_workshop.core.item;

import moe.plushie.armourers_workshop.core.data.MannequinHitResult;
import moe.plushie.armourers_workshop.core.entity.MannequinEntity;
import moe.plushie.armourers_workshop.init.ModEntityTypes;
import moe.plushie.armourers_workshop.utils.Constants;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.item.Item;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.item.context.UseOnContext;
import net.cocoonmc.core.math.Vector3d;
import net.cocoonmc.core.math.Vector3f;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.world.InteractionHand;
import net.cocoonmc.core.world.InteractionResult;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.Player;

public class MannequinItem extends Item {

    public MannequinItem(Properties properties) {
        super(properties);
    }

    public static boolean isSmall(ItemStack itemStack) {
        CompoundTag entityTag = itemStack.getTagElement("EntityTag");
        if (entityTag != null) {
            return entityTag.getBoolean("Small");
        }
        return false;
    }

    public static void setScale(ItemStack itemStack, float scale) {
        CompoundTag entityTag = itemStack.getOrCreateTagElement("EntityTag");
        entityTag.putFloat("Scale", scale);
    }

    public static float getScale(ItemStack itemStack) {
        CompoundTag entityTag = itemStack.getTagElement("EntityTag");
        if (entityTag == null || !entityTag.contains("Scale", 5)) {
            return 1.0f;
        }
        return entityTag.getFloat("Scale");
    }


    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getHand() != InteractionHand.MAIN_HAND) {
            return InteractionResult.FAIL;
        }
        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.FAIL;
        }
        Level level = context.getLevel();
        Vector3d origin = player.getLocation();
        MannequinHitResult rayTraceResult = MannequinHitResult.test(player, origin, context.getClickLocation(), context.getClickedPos());
        ItemStack itemStack = context.getItemInHand();
        MannequinEntity entity = ModEntityTypes.MANNEQUIN.create(level, rayTraceResult.getBlockPos(), itemStack.getTag());
        if (entity == null) {
            return InteractionResult.FAIL;
        }
        Vector3d clickedLocation = rayTraceResult.getLocation();
        entity.setLocation(clickedLocation); // 0.0f, 0.0f
        entity.setBodyRot(new Vector3f(0, rayTraceResult.getRotation(), 0));
//
        level.addFreshEntity(entity);
//        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ARMOR_STAND_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);

        itemStack.shrink(1);
        return InteractionResult.SUCCESS;
    }
}
