package moe.plushie.armourers_workshop.init.handler;

import moe.plushie.armourers_workshop.api.WorldAccessor;
import moe.plushie.armourers_workshop.core.network.NetworkManager;
import moe.plushie.armourers_workshop.core.network.UpdateContextPacket;
import moe.plushie.armourers_workshop.init.ModLog;
import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.block.BlockEntity;
import net.cocoonmc.core.block.BlockState;
import net.cocoonmc.core.world.Level;
import net.cocoonmc.core.world.entity.Player;
import net.cocoonmc.runtime.impl.BlockEntityAccessor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class EntityEventHandler implements Listener {

    public static String CHANNEL = "armourers_workshop:play";


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = Player.of(event.getPlayer());
        player.addChannel(CHANNEL);
        // first join, send the context.
        NetworkManager.sendTo(new UpdateContextPacket(player), player);
        NetworkManager.sendWardrobeTo(player, player);
    }

//    @EventHandler(ignoreCancelled = true)
//    public void onBlockBreak(BlockBreakEvent event) {
//        Level level = Level.of(event.getBlock().getWorld());
//        BlockPos pos = BlockPos.of(event.getBlock().getLocation());
//        BlockState state = level.getBlockState(pos);
//        if (state != null) {
//            ModLog.debug("{}", state);
//        }
//
////        WorldAccessor accessor = WorldAccessor.of()
//
////        ModLog.debug("{}: {} => {}", event.getPlayer(), event.getBlock(), event.getItems());
//////        Bukkit.broadcastMessage(event.getBlock().getType().name() + " => " + event.getEntityType().getName() + " : " + event.getTo().name());
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
