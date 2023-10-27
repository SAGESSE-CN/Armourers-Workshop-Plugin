package moe.plushie.armourers_workshop.plugin.init.handler;

import moe.plushie.armourers_workshop.plugin.core.network.NetworkManager;
import moe.plushie.armourers_workshop.plugin.core.network.UpdateContextPacket;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.lang.reflect.Method;

public class EntityEventHandler implements Listener {

    public static String CHANNEL = "armourers_workshop:play";


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        try {
            Class<? extends CommandSender> senderClass = player.getClass();
            Method addChannel = senderClass.getDeclaredMethod("addChannel", String.class);
            addChannel.setAccessible(true);
            addChannel.invoke(player, CHANNEL);
            // first join, send the context.
            NetworkManager.sendTo(new UpdateContextPacket(player), player);
            NetworkManager.sendWardrobeTo(player, player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @EventHandler
//    public void onInteractBlock(PlayerInteractEvent event) {
//        Player player = event.getPlayer();
//        EquipmentSlot hand = event.getHand();
//        ItemStack itemStack = BukkitUtils.wrap(event.getItem());
//        Item item = itemStack.getItem();
//        if (event.hasBlock() && event.getClickedBlock() != null) {
//            World world = player.getWorld();
//            BlockPos blockPos = BlockPos.of(event.getClickedBlock().getLocation());
//            // for attack(left-click) will pass to vanilla.
//            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
//                // for we custom block, check it and cancel event.
//                BlockEntity blockEntity = BukkitUtils.getBlockEntity(event.getClickedBlock());
//                if (blockEntity != null && blockEntity.getBlockState().attack(world, blockPos, player) != InteractionResult.PASS) {
//                    event.setCancelled(true);
//                    return;
//                }
//                return;
//            }
//            // order: useOnFirst -> block.use -> useOn
//            UseOnContext context = new UseOnContext(player, itemStack, event.getClickedBlock(), event.getBlockFace(), hand);
//            if (event.useItemInHand() != Event.Result.DENY && item.useOnFirst(itemStack, context) != InteractionResult.PASS) {
//                event.setCancelled(true);
//                return;
//            }
//            // block.use
//            ItemStack mainHandItem = BukkitUtils.wrap(player.getInventory().getItemInMainHand());
//            ItemStack offHandItem = BukkitUtils.wrap(player.getInventory().getItemInOffHand());
//            boolean flag = !mainHandItem.isEmpty() || !offHandItem.isEmpty();
//            boolean flag1 = player.isSneaking() && flag && (!mainHandItem.doesSneakBypassUse(player, world, blockPos) || !offHandItem.doesSneakBypassUse(player, world, blockPos));
//            if (event.useItemInHand() == Event.Result.ALLOW || (event.useItemInHand() != Event.Result.DENY && !flag1)) {
//                // for we custom block, check it and cancel event.
//                BlockEntity blockEntity = BukkitUtils.getBlockEntity(event.getClickedBlock());
//                if (blockEntity != null && blockEntity.getBlockState().use(world, blockPos, player, hand) != InteractionResult.PASS) {
//                    event.setCancelled(true);
//                    return;
//                }
//                // for vanilla interactive block, we pass it directly.
//                if (context.getClickedBlock().getType().isInteractable()) {
//                    return;
//                }
//            }
//            // use item on context.
//            if (event.useItemInHand() != Event.Result.DENY && item.useOn(context) != InteractionResult.PASS) {
//                event.setCancelled(true);
//                return;
//            }
//        } else {
//            // order: use
//            InteractionResultHolder<ItemStack> result = item.use(itemStack, player, hand);
//            if (result.getObject() != itemStack) {
//                player.getInventory().setItem(hand, BukkitUtils.unwrap(result.getObject()));
//            }
//            if (result.getResult() != InteractionResult.PASS) {
//                event.setCancelled(true);
//            }
//        }
//        // safe: we need to cancel all unknown events.
//        if (item.asMaterial() == null) {
//            event.setCancelled(true);
//        }
//    }
//
//    @EventHandler
//    public void onInteractEntity(PlayerInteractEntityEvent event) {
//        Player player = event.getPlayer();
//        LivingEntity entity = ObjectUtils.safeCast(event.getRightClicked(), LivingEntity.class);
//        if (entity == null) {
//            return;
//        }
//        ItemStack itemStack = BukkitUtils.wrap(player.getInventory().getItem(event.getHand()));
//        Item item = itemStack.getItem();
//        if (item.interactLivingEntity(itemStack, player, entity, event.getHand()) != InteractionResult.PASS) {
//            event.setCancelled(true);
//        }
//    }
//
//    @EventHandler
//    public void onAttackEntity(EntityDamageByEntityEvent event) {
//        Entity entity = event.getEntity();
//        Player player = ObjectUtils.safeCast(event.getDamager(), Player.class);
//        if (player == null) {
//            return;
//        }
//        ItemStack itemStack = BukkitUtils.wrap(player.getInventory().getItemInMainHand());
//        Item item = itemStack.getItem();
//        if (item.attackLivingEntity(itemStack, player, entity) != InteractionResult.PASS) {
//            event.setCancelled(true);
//        }
//    }

//    @EventHandler
//    public void onSpawnEntity(EntitySpawnEvent event) {
//        ServerPlayer player = ObjectUtils.safeCast(entity, ServerPlayer.class);
//        if (player != null) {
//            NetworkManager.sendTo(new UpdateContextPacket(player), player);
//            NetworkManager.sendWardrobeTo(player, player);
//        }
//    }
}
