package moe.plushie.armourers_workshop.plugin.core.menu;

import moe.plushie.armourers_workshop.customapi.CustomMenu;
import moe.plushie.armourers_workshop.customapi.CustomSlot;
import moe.plushie.armourers_workshop.plugin.api.Component;
import moe.plushie.armourers_workshop.plugin.api.FriendlyByteBuf;
import moe.plushie.armourers_workshop.plugin.api.MenuType;
import moe.plushie.armourers_workshop.plugin.core.network.FMLOpenContainer;
import moe.plushie.armourers_workshop.plugin.core.network.NetworkManager;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinSlotType;
import moe.plushie.armourers_workshop.plugin.utils.BukkitUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class ContainerMenu extends CustomMenu {

    protected final Player player;
    protected final MenuType<?> menuType;

    public ContainerMenu(MenuType<?> menuType, Player player) {
        super(player, menuType.getRegistryName());
        this.player = player;
        this.menuType = menuType;
    }

    @Override
    public void handleOpenWindowPacket(int windowId) {
        // we need send custom event.
        String id = getType().getRegistryName();
        FriendlyByteBuf buf = new FriendlyByteBuf();
        serialize(buf);
        Component title = Component.translatable("inventory." + id.replace(':', '.'));
        NetworkManager.sendTo(new FMLOpenContainer(id, windowId, title, buf), player);
    }

    public void serialize(FriendlyByteBuf buffer) {
        // nop
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public MenuType<?> getType() {
        return menuType;
    }


    public ItemStack quickMoveStack(Player player, int index, int slotSize) {
        CustomSlot slot = slots.get(index);
        if (slot == null || !slot.hasItem()) {
            return BukkitUtils.EMPTY_STACK;
        }
        ItemStack itemStack = slot.getItem();
        if (index >= 36) {
            if (!(moveItemStackTo(itemStack, 9, 36, false) || moveItemStackTo(itemStack, 0, 9, false))) {
                return BukkitUtils.EMPTY_STACK;
            }
            slot.set(BukkitUtils.EMPTY_STACK);
            return new ItemStack(itemStack);
        }
        if (!moveItemStackTo(itemStack, 36, slotSize, false)) {
            return BukkitUtils.EMPTY_STACK;
        }
        slot.setChanged();
        return BukkitUtils.EMPTY_STACK;
    }

    protected void addPlayerSlots(Inventory inventory, int slotsX, int slotsY) {
        addPlayerSlots(inventory, slotsX, slotsY, CustomSlot::new);
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

        CustomSlot create(Inventory inventory, int slot, int x, int y);
    }
}
