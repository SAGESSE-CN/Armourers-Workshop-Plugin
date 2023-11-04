package moe.plushie.armourers_workshop.core.menu;

import moe.plushie.armourers_workshop.api.WorldAccessor;
import moe.plushie.armourers_workshop.core.blockentity.DyeTableBlockEntity;
import moe.plushie.armourers_workshop.core.item.BottleItem;
import moe.plushie.armourers_workshop.core.item.SkinItem;
import moe.plushie.armourers_workshop.core.skin.SkinDescriptor;
import moe.plushie.armourers_workshop.core.skin.color.ColorScheme;
import moe.plushie.armourers_workshop.core.skin.color.PaintColor;
import moe.plushie.armourers_workshop.core.skin.painting.SkinPaintType;
import moe.plushie.armourers_workshop.core.skin.painting.SkinPaintTypes;
import moe.plushie.armourers_workshop.init.ModItems;
import net.cocoonmc.core.inventory.MenuType;
import net.cocoonmc.core.inventory.Slot;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.world.entity.Player;
import org.bukkit.inventory.Inventory;

public class DyeTableMenu extends UpdatableContainerMenu<DyeTableBlockEntity> {

    private boolean isLoading = false;
    private final SkinPaintType[] paintTypes = {SkinPaintTypes.DYE_1, SkinPaintTypes.DYE_2, SkinPaintTypes.DYE_3, SkinPaintTypes.DYE_4, SkinPaintTypes.DYE_5, SkinPaintTypes.DYE_6, SkinPaintTypes.DYE_7, SkinPaintTypes.DYE_8};

    public DyeTableMenu(MenuType<?> menuType, Player player, WorldAccessor accessor) {
        super(menuType, player, accessor);
        this.addDataSlots(getInventory(), 68, 36, 22, 22);
        this.addInputSlot(getInventory(), 8, 26, 23, PlaceFilter.allowsSkin(), it -> false);
        this.addOutputSlot(getInventory(), 9, 26, 69);
    }

    protected void addDataSlots(Inventory inventory, int x, int y, int itemWidth, int itemHeight) {
        for (int i = 0; i < 8; i++) {
            int ix = x + (i % 4) * itemWidth;
            int iy = y + (i / 4) * itemHeight;
            addDataSlot(inventory, i, ix, iy, this::allowsPlaceColor, null);
        }
    }

    @Override
    protected void onInputSlotChange(Slot slot) {
        if (!isLoading && outputSlots.get(0).getItem().isEmpty()) {
            loadSkin(slot.getItem());
        }
    }

    @Override
    protected void onOutputSlotChange(Slot slot) {
        if (!isLoading && !slot.hasItem()) {
            loadSkin(ItemStack.EMPTY);
        }
    }

    @Override
    protected void onDataSlotChange(Slot slot) {
        if (!isLoading) {
            applySkin(outputSlots.get(0).getItem());
        }
    }

    protected void loadSkin(ItemStack itemStack) {
        isLoading = true;
        loadSkin0(itemStack);
        isLoading = false;
    }

    protected void applySkin(ItemStack itemStack) {
        isLoading = true;
        applySkin0(itemStack);
        isLoading = false;
    }

    private void loadSkin0(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            inputSlots.forEach(it -> it.setItem(ItemStack.EMPTY));
            outputSlots.forEach(it -> it.setItem(ItemStack.EMPTY));
            dataSlots.forEach(it -> it.setItem(ItemStack.EMPTY));
            return;
        }
        SkinDescriptor descriptor = SkinDescriptor.of(itemStack);
        ColorScheme scheme = descriptor.getColorScheme();
        for (int i = 0; i < paintTypes.length; ++i) {
            ItemStack colorStack = ItemStack.EMPTY;
            PaintColor paintColor = scheme.getColor(paintTypes[i]);
            if (paintColor != null) {
                colorStack = ModItems.BOTTLE.getDefaultInstance();
                BottleItem.setColor(colorStack, paintColor);
            }
            dataSlots.get(i).setItem(colorStack);
        }
        outputSlots.get(0).setItem(itemStack.copy());
    }

    private void applySkin0(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return;
        }
        ColorScheme newScheme = new ColorScheme();
        for (int i = 0; i < paintTypes.length; ++i) {
            ItemStack colorStack = dataSlots.get(i).getItem();
            PaintColor paintColor = BottleItem.getColor(colorStack);
            if (paintColor != null) {
                newScheme.setColor(paintTypes[i], paintColor);
            }
        }
        SkinDescriptor descriptor = SkinDescriptor.of(itemStack);
        if (newScheme.equals(descriptor.getColorScheme())) {
            return; // not any changes.
        }
        descriptor = new SkinDescriptor(descriptor, newScheme);
        ItemStack newItemStack = itemStack.copy();
        SkinItem.setSkin(newItemStack, descriptor);
        outputSlots.get(0).setItem(newItemStack);
    }

    private boolean allowsPlaceColor(ItemStack itemStack) {
        if (inputSlots.get(0).hasItem()) {
            return itemStack.is(ModItems.BOTTLE);
        }
        return false;
    }
}
