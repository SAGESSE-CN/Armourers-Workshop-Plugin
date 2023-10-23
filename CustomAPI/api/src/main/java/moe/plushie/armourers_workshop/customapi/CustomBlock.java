package moe.plushie.armourers_workshop.customapi;

import moe.plushie.armourers_workshop.customapi.version.Versions;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class CustomBlock {

    public static void placeItem(ItemStack itemStack, BlockPlaceContext context, EquipmentSlot c) {
        CustomBlockImpl impl = Versions.API.createCustomBlock();
        impl.placeItem(itemStack, context, c);
    }

    public static class BlockPlaceContext implements CustomBlockImpl.IBlockPlaceContext {

        protected final Player player;
        protected final EquipmentSlot hand;
        protected final int clickedType;
        protected final Location clickedPos;
        protected final Location clickedLocation;
        protected final BlockFace clickedFace;

        public BlockPlaceContext(Player player, EquipmentSlot hand, int clickedType, Location clickedPos, Location clickedLocation, BlockFace clickedFace) {
            this.player = player;
            this.hand = hand;
            this.clickedType = clickedType;
            this.clickedPos = clickedPos;
            this.clickedLocation = clickedLocation;
            this.clickedFace = clickedFace;
        }

        @Override
        public Player getPlayer() {
            return player;
        }

        @Override
        public EquipmentSlot getHand() {
            return hand;
        }

        @Override
        public BlockFace getClickedFace() {
            return clickedFace;
        }

        @Override
        public Location getClickedPos() {
            return clickedPos;
        }

        @Override
        public Location getClickedLocation() {
            return clickedLocation;
        }

        @Override
        public int getClickedType() {
            return clickedType;
        }
    }
}
