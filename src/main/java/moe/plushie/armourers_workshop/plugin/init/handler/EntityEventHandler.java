package moe.plushie.armourers_workshop.plugin.init.handler;

import moe.plushie.armourers_workshop.plugin.api.InteractionResult;
import moe.plushie.armourers_workshop.plugin.api.InteractionResultHolder;
import moe.plushie.armourers_workshop.plugin.api.Item;
import moe.plushie.armourers_workshop.plugin.api.ItemStack;
import moe.plushie.armourers_workshop.plugin.api.UseOnContext;
import moe.plushie.armourers_workshop.plugin.core.network.NetworkManager;
import moe.plushie.armourers_workshop.plugin.core.network.UpdateContextPacket;
import moe.plushie.armourers_workshop.plugin.utils.BukkitUtils;
import moe.plushie.armourers_workshop.plugin.utils.ObjectUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;

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

    @EventHandler
    public void onInteractBlock(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!event.hasItem() || event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
            return;
        }
        EquipmentSlot hand = event.getHand();
        ItemStack itemStack = BukkitUtils.wrap(event.getItem());
        Item item = itemStack.getItem();
        if (event.hasBlock() && !player.isSneaking()) {
            UseOnContext context = new UseOnContext(player, itemStack, event.getClickedBlock(), event.getBlockFace(), hand);
            if (item.useOn(context) != InteractionResult.PASS) {
                event.setCancelled(true);
            }
        } else {
            InteractionResultHolder<ItemStack> result = item.use(itemStack, player, hand);
            if (result.getObject() != itemStack) {
                player.getInventory().setItem(hand, BukkitUtils.unwrap(result.getObject()));
            }
            if (result.getResult() != InteractionResult.PASS) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        LivingEntity entity = ObjectUtils.safeCast(event.getRightClicked(), LivingEntity.class);
        if (entity == null) {
            return;
        }
        ItemStack itemStack = BukkitUtils.wrap(player.getInventory().getItem(event.getHand()));
        Item item = itemStack.getItem();
        if (item.interactLivingEntity(itemStack, player, entity, event.getHand()) != InteractionResult.PASS) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onAttackEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Player player = ObjectUtils.safeCast(event.getDamager(), Player.class);
        if (player == null) {
            return;
        }
        ItemStack itemStack = BukkitUtils.wrap(player.getInventory().getItemInMainHand());
        Item item = itemStack.getItem();
        if (item.attackLivingEntity(itemStack, player, entity) != InteractionResult.PASS) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSpawnEntity(EntitySpawnEvent event) {
//        ServerPlayer player = ObjectUtils.safeCast(entity, ServerPlayer.class);
//        if (player != null) {
//            NetworkManager.sendTo(new UpdateContextPacket(player), player);
//            NetworkManager.sendWardrobeTo(player, player);
//        }
    }
}
