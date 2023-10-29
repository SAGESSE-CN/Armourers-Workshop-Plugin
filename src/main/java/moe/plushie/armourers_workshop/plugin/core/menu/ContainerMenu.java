package moe.plushie.armourers_workshop.plugin.core.menu;

import net.cocoonmc.core.inventory.Menu;
import net.cocoonmc.core.inventory.MenuType;
import net.cocoonmc.core.inventory.Slot;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.network.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public abstract class ContainerMenu extends Menu {

    protected final Player player;
    protected final MenuType<?> menuType;

    public ContainerMenu(MenuType<?> menuType, Player player) {
        super(menuType, Component.translatable("inventory." + menuType.getRegistryName().toString().replace(':', '.')), player);
        this.player = player;
        this.menuType = menuType;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

    public ItemStack quickMoveStack(Player player, int index, int slotSize) {
        Slot slot = slots.get(index);
        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack itemStack = slot.getItem();
        if (index >= 36) {
            if (!(moveItemStackTo(itemStack, 9, 36, false) || moveItemStackTo(itemStack, 0, 9, false))) {
                return ItemStack.EMPTY;
            }
            slot.setItem(ItemStack.EMPTY);
            return itemStack.copy();
        }
        if (!moveItemStackTo(itemStack, 36, slotSize, false)) {
            return ItemStack.EMPTY;
        }
        slot.setChanged();
        return ItemStack.EMPTY;
    }

    protected void clearContainer(Player player, Inventory inventory) {
//        if (!player.isAlive() || player instanceof ServerPlayer && ((ServerPlayer)player).hasDisconnected()) {
//            for (int i = 0; i < container.getContainerSize(); ++i) {
//                player.drop(container.removeItemNoUpdate(i), false);
//            }
//            return;
//        }
//        for (int i = 0; i < container.getContainerSize(); ++i) {
//            Inventory inventory = player.getInventory();
//            if (!(inventory.player instanceof ServerPlayer)) continue;
//            inventory.placeItemBackInInventory(container.removeItemNoUpdate(i));
//        }
    }

    protected void addPlayerSlots(Inventory inventory, int slotsX, int slotsY) {
        addPlayerSlots(inventory, slotsX, slotsY, Slot::new);
    }

    protected void addPlayerSlots(Inventory inventory, int slotsX, int slotsY, ISlotBuilder builder) {
        for (int col = 0; col < 9; ++col) {
            this.addSlot(builder.create(inventory, col, slotsX + col * 18, slotsY + 58));
        }
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(builder.create(inventory, col + row * 9 + 9, slotsX + col * 18, slotsY + row * 18));
            }
        }
    }

    public interface ISlotBuilder {

        Slot create(Inventory inventory, int slot, int x, int y);
    }
}
