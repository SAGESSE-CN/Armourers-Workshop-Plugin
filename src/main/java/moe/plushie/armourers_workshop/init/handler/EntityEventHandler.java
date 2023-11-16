package moe.plushie.armourers_workshop.init.handler;

import moe.plushie.armourers_workshop.core.network.NetworkManager;
import moe.plushie.armourers_workshop.core.network.UpdateContextPacket;
import moe.plushie.armourers_workshop.core.skin.SkinWardrobe;
import moe.plushie.armourers_workshop.init.ModConfig;
import net.cocoonmc.core.utils.BukkitHelper;
import net.cocoonmc.core.world.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class EntityEventHandler implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        // first join, send the context.
        Player player = Player.of(event.getPlayer());
        NetworkManager.sendTo(new UpdateContextPacket(player), player);
        NetworkManager.sendWardrobeTo(player, player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerSpawn(PlayerRespawnEvent event) {
        Player player = Player.of(event.getPlayer());
        BukkitHelper.runTask(() -> {
            SkinWardrobe wardrobe = SkinWardrobe.of(player);
            if (wardrobe != null) {
                wardrobe.broadcast();
            }
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = Player.of(event.getEntity());
        if (!shouldKeepWardrobe(player, event.getKeepInventory())) {
            return;
        }
        SkinWardrobe oldWardrobe = SkinWardrobe.of(player);
        if (oldWardrobe != null) {
            oldWardrobe.dropAll(itemStack -> BukkitHelper.dropItem(itemStack, player));
        }
    }

    private static boolean shouldKeepWardrobe(Player player, boolean keepInventory) {
        if (player.isSpectator()) {
            return true;
        }
        // 0 = use keep inventory rule
        // 1 = never drop
        // 2 = always drop
        int keep = ModConfig.Common.prefersWardrobeDropOnDeath;
        if (keep == 1) {
            return true;
        }
        if (keep == 2) {
            return false;
        }
        return keepInventory;
    }
}
