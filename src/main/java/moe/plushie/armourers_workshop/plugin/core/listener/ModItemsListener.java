package moe.plushie.armourers_workshop.plugin.core.listener;

import moe.plushie.armourers_workshop.plugin.utils.NBTEditor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.LoomInventory;

public class ModItemsListener implements Listener {

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        for (ItemStack itemStack : event.getInventory().getMatrix()) {
            if (itemStack != null) {
                if (NBTEditor.contains(itemStack, "__redirected_id__")) {
                    event.getInventory().setResult(new ItemStack(Material.AIR));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() instanceof LoomInventory) {
            ItemStack currentItem = event.getCurrentItem();
            if (NBTEditor.contains(currentItem, "__redirected_id__")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        if (NBTEditor.contains(event.getFuel(), "__redirected_id__")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        if (NBTEditor.contains(event.getSource(), "__redirected_id__")) {
            event.setCancelled(true);
        }
    }

}
