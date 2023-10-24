package moe.plushie.armourers_workshop.plugin.core.menu;

import moe.plushie.armourers_workshop.plugin.api.FriendlyByteBuf;
import moe.plushie.armourers_workshop.plugin.api.MenuType;
import moe.plushie.armourers_workshop.plugin.api.WorldAccessor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class BlockContainerMenu extends ContainerMenu {

    protected final Inventory inventory;
    protected final WorldAccessor accessor;

    public BlockContainerMenu(MenuType<?> menuType, Player player, WorldAccessor accessor) {
        super(menuType, player);
        this.accessor = accessor;
        this.inventory = getInventory();

        this.addPlayerSlots(player.getInventory(), 8, 108);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return quickMoveStack(player, index, slots.size() - 1);
    }

    @Override
    public void serialize(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(accessor.getBlockPos());
    }
}
