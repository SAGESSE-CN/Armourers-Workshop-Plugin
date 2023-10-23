package moe.plushie.armourers_workshop.customapi;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public interface CustomBlockImpl {

    int placeItem(ItemStack itemStack, IBlockPlaceContext context, EquipmentSlot hand);

    interface IBlockPlaceContext {

        Player getPlayer();

        EquipmentSlot getHand();

        // 0 miss, 1 inside, 2 outside
        int getClickedType();

        Location getClickedPos();

        Location getClickedLocation();

        BlockFace getClickedFace();
    }
}
