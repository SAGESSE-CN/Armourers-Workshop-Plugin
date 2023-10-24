package moe.plushie.armourers_workshop.plugin.api;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

public class UseOnContext {

    protected final ItemStack item;
    protected final Player player;
    protected final Block blockClicked;
    protected final BlockFace blockFace;
    protected final BlockPos clickedPos;
    protected final EquipmentSlot hand;

    public UseOnContext(Player player, ItemStack item, Block clickedBlock, BlockFace clickedFace) {
        this(player, item, clickedBlock, clickedFace, EquipmentSlot.HAND);
    }

    public UseOnContext(Player player, ItemStack item, Block clickedBlock, BlockFace clickedFace, EquipmentSlot hand) {
        this.player = player;
        this.item = item;
        this.blockClicked = clickedBlock;
        this.blockFace = clickedFace;
        this.clickedPos = BlockPos.of(clickedBlock.getLocation());
        this.hand = hand;
    }

    public Player getPlayer() {
        return this.player;
    }

    public World getWorld() {
        return player.getWorld();
    }

    public ItemStack getItem() {
        return item;
    }

    public EquipmentSlot getHand() {
        return this.hand;
    }

    public Block getClickedBlock() {
        return blockClicked;
    }

    public BlockPos getClickedPos() {
        return clickedPos;
    }

    public BlockFace getClickedFace() {
        return blockFace;
    }

    public BlockFace getHorizontalDirection() {
        if (player != null) {
            return player.getFacing();
        }
        return BlockFace.NORTH;
    }
}

