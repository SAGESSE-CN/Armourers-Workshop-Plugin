package moe.plushie.armourers_workshop.plugin.core.menu;

import moe.plushie.armourers_workshop.customapi.CustomSlot;
import moe.plushie.armourers_workshop.plugin.api.FriendlyByteBuf;
import moe.plushie.armourers_workshop.plugin.api.MenuType;
import moe.plushie.armourers_workshop.plugin.api.WorldAccessor;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinDescriptor;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinSlotType;
import moe.plushie.armourers_workshop.plugin.init.ModItems;
import moe.plushie.armourers_workshop.plugin.utils.BukkitUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DyeTableMenu extends BlockContainerMenu {

    public DyeTableMenu(MenuType<?> menuType, Player player, WorldAccessor accessor) {
        super(menuType, player, accessor);
        this.addCustomSlots(inventory, 68, 36, 22, 22);
        this.addInputSlot(inventory, 8, 26, 23);
        this.addOutputSlot(inventory, 9, 26, 69);
    }

    public ItemStack getOutputStack() {
        return inventory.getItem(9);
    }

    public void setOutputStack(ItemStack itemStack) {
        inventory.setItem(9, itemStack);
    }

    protected void addInputSlot(Inventory inventory, int slot, int x, int y) {
        addSlot(new CustomSlot(inventory, slot, x, y) {

            @Override
            public boolean mayPickup(Player p_82869_1_) {
                return false;
            }

            @Override
            public boolean mayPlace(ItemStack itemStack) {
                return !SkinDescriptor.of(BukkitUtils.wrap(itemStack)).isEmpty();
            }

            @Override
            public void setChanged() {
                super.setChanged();
//                if (inventory.getItem(9).isEmpty()) {
//                    loadSkin(getItem());
//                }
            }
        });
    }

    protected void addOutputSlot(Inventory inventory, int slot, int x, int y) {
        addSlot(new CustomSlot(inventory, slot, x, y) {

            @Override
            public boolean mayPlace(ItemStack p_75214_1_) {
                return false;
            }

            @Override
            public void setChanged() {
                super.setChanged();
                if (!hasItem()) {
                    loadSkin(BukkitUtils.EMPTY_STACK);
                }
            }
        });
    }

    protected void addCustomSlots(Inventory inventory, int x, int y, int itemWidth, int itemHeight) {
        for (int i = 0; i < 8; i++) {
            int ix = x + (i % 4) * itemWidth;
            int iy = y + (i / 4) * itemHeight;
            addSlot(new LockableSlot(inventory, i, ix, iy, SkinSlotType.DYE));
        }
    }


    protected void loadSkin(ItemStack itemStack) {
//        if (itemStack.isEmpty()) {
//            inventory.clearContent();
//            return;
//        }
//        SkinDescriptor descriptor = SkinDescriptor.of(itemStack);
//        ColorScheme scheme = descriptor.getColorScheme();
//        for (int i = 0; i < paintTypes.length; ++i) {
//            ItemStack colorStack = ItemStack.EMPTY;
//            IPaintColor paintColor = scheme.getColor(paintTypes[i]);
//            if (paintColor != null) {
//                colorStack = new ItemStack(ModItems.BOTTLE.get());
//                ColorUtils.setColor(colorStack, paintColor);
//            }
//            inventory.setItem(i, colorStack);
//        }
//        setOutputStack(itemStack.copy());
    }

    protected void applySkin(ItemStack itemStack) {
//        if (itemStack.isEmpty()) {
//            return;
//        }
//        ColorScheme newScheme = new ColorScheme();
//        for (int i = 0; i < paintTypes.length; ++i) {
//            ItemStack colorStack = inventory.getItem(i);
//            IPaintColor paintColor = ColorUtils.getColor(colorStack);
//            if (paintColor != null) {
//                newScheme.setColor(paintTypes[i], paintColor);
//            }
//        }
//        SkinDescriptor descriptor = SkinDescriptor.of(itemStack);
//        if (newScheme.equals(descriptor.getColorScheme())) {
//            return; // not any changes.
//        }
//        descriptor = new SkinDescriptor(descriptor, newScheme);
//        ItemStack newItemStack = itemStack.copy();
//        SkinDescriptor.setDescriptor(newItemStack, descriptor);
//        setOutputStack(newItemStack);
    }

    @Override
    public int getSlotSize() {
        return 10;
    }

    public class LockableSlot extends SkinSlot {

        public LockableSlot(Inventory inventory, int slot, int x, int y, SkinSlotType... slotTypes) {
            super(inventory, slot, x, y, slotTypes);
        }

        @Override
        public boolean mayPlace(ItemStack itemStack) {
            return BukkitUtils.wrap(itemStack).getItem().equals(ModItems.BOTTLE);
        }

        @Override
        public void setChanged() {
            super.setChanged();
            applySkin(getOutputStack());
        }
    }
}
