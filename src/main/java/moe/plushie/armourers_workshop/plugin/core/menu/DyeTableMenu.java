package moe.plushie.armourers_workshop.plugin.core.menu;

import moe.plushie.armourers_workshop.plugin.api.WorldAccessor;
import moe.plushie.armourers_workshop.plugin.core.item.SkinItem;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinDescriptor;
import moe.plushie.armourers_workshop.plugin.core.skin.color.ColorScheme;
import moe.plushie.armourers_workshop.plugin.core.skin.color.PaintColor;
import moe.plushie.armourers_workshop.plugin.core.skin.painting.SkinPaintType;
import moe.plushie.armourers_workshop.plugin.core.skin.painting.SkinPaintTypes;
import moe.plushie.armourers_workshop.plugin.init.ModItems;
import net.cocoonmc.core.inventory.MenuType;
import net.cocoonmc.core.inventory.Slot;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.nbt.CompoundTag;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class DyeTableMenu extends BlockContainerMenu {

    private final SkinPaintType[] paintTypes = {SkinPaintTypes.DYE_1, SkinPaintTypes.DYE_2, SkinPaintTypes.DYE_3, SkinPaintTypes.DYE_4, SkinPaintTypes.DYE_5, SkinPaintTypes.DYE_6, SkinPaintTypes.DYE_7, SkinPaintTypes.DYE_8};

    public DyeTableMenu(MenuType<?> menuType, Player player, WorldAccessor accessor) {
        super(menuType, player, accessor);
        this.addDataSlots(inventory, 68, 36, 22, 22);
        this.addInputSlot(inventory, 8, 26, 23, it -> SkinDescriptor.of(it).isEmpty(), it -> false);
        this.addOutputSlot(inventory, 9, 26, 69);
    }

    public ItemStack getOutputStack() {
        return outputSlots.get(0).getItem();
    }

    public void setOutputStack(ItemStack itemStack) {
        outputSlots.get(0).setItemNoUpdate(itemStack);
    }

    protected void addDataSlots(Inventory inventory, int x, int y, int itemWidth, int itemHeight) {
        for (int i = 0; i < 8; i++) {
            int ix = x + (i % 4) * itemWidth;
            int iy = y + (i / 4) * itemHeight;
            addDataSlot(inventory, i, ix, iy, PlaceFilter.only(ModItems.BOTTLE), null);
        }
    }

    @Override
    protected void onInputSlotChange(Slot slot) {
        if (getOutputStack().isEmpty()) {
            loadSkin(slot.getItem());
        }
    }

    @Override
    protected void onOutputSlotChange(Slot slot) {
        if (!slot.hasItem()) {
            loadSkin(ItemStack.EMPTY);
        }
    }

    @Override
    protected void onDataSlotChange(Slot slot) {
        applySkin(getOutputStack());
    }

    protected void loadSkin(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            inventory.clear();
            return;
        }
        SkinDescriptor descriptor = SkinDescriptor.of(itemStack);
        ColorScheme scheme = descriptor.getColorScheme();
        for (int i = 0; i < paintTypes.length; ++i) {
            ItemStack colorStack = ItemStack.EMPTY;
            PaintColor paintColor = scheme.getColor(paintTypes[i]);
            if (paintColor != null) {
                CompoundTag tag = CompoundTag.newInstance();
                tag.putInt("Color", paintColor.getRawValue());
                colorStack = new ItemStack(ModItems.BOTTLE, 1, tag);
            }
            inventory.setItem(i, colorStack.asBukkit());
        }
        setOutputStack(itemStack.copy());
    }

    protected void applySkin(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return;
        }
        ColorScheme newScheme = new ColorScheme();
        for (int i = 0; i < paintTypes.length; ++i) {
            ItemStack colorStack = dataSlots.get(i).getItem();
            PaintColor paintColor = PaintColor.WHITE;
//            PaintColor paintColor = BukkitUtils.getColor(colorStack);
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
        setOutputStack(newItemStack);
    }

    @Override
    public int getSlotSize() {
        return 10;
    }
}
