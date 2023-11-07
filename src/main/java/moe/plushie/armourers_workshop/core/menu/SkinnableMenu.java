package moe.plushie.armourers_workshop.core.menu;

import moe.plushie.armourers_workshop.api.WorldAccessor;
import moe.plushie.armourers_workshop.core.blockentity.SkinnableBlockEntity;
import net.cocoonmc.core.inventory.MenuType;
import net.cocoonmc.core.inventory.Slot;
import net.cocoonmc.core.network.Component;
import net.cocoonmc.core.world.entity.Player;
import org.bukkit.inventory.Inventory;

public class SkinnableMenu extends UpdatableContainerMenu<SkinnableBlockEntity> {

    private int row;
    private int column;
    private Inventory inventory;

    public SkinnableMenu(MenuType<?> menuType, Player player, WorldAccessor accessor) {
        super(menuType, player, accessor);
        this.row = 3;
        this.column = 9;
        this.title = getInventoryName();
        this.inventory = player.getEnderInventory();
        if (!blockEntity.isEnderInventory()) {
            this.row = blockEntity.getInventoryHeight();
            this.column = blockEntity.getInventoryWidth();
            this.inventory = createInventoryContainer().asBukkit();
        }
        addCustomSlots(inventory, 0, 0);
    }

    protected void addCustomSlots(Inventory inventory, int x, int y) {
        if (inventory == null) {
            return;
        }
        for (int j = 0; j < row; j++) {
            for (int i = 0; i < column; i++) {
                addSlot(new Slot(inventory, i + j * column, x + 18 * i + 1, y + j * 18 + 1));
            }
        }
    }

    public Component getInventoryName() {
        String title = blockEntity.getInventoryName();
        if (title != null && !title.isEmpty()) {
            return Component.literal(title);
        }
        return Component.translatable("inventory.armourers_workshop.skinnable");
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
