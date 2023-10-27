package moe.plushie.armourers_workshop.plugin.core.menu;

import moe.plushie.armourers_workshop.plugin.api.WorldAccessor;
import net.cocoonmc.core.inventory.MenuType;
import net.cocoonmc.core.inventory.Slot;
import net.cocoonmc.core.item.Item;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.network.FriendlyByteBuf;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Predicate;

public abstract class BlockContainerMenu extends ContainerMenu {

    protected final Inventory inventory;
    protected final WorldAccessor accessor;

    protected final ArrayList<Slot> inputSlots = new ArrayList<>();
    protected final ArrayList<Slot> outputSlots = new ArrayList<>();
    protected final ArrayList<Slot> dataSlots = new ArrayList<>();

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


    protected void addInputSlot(Inventory inventory, int slot, int x, int y) {
        addInputSlot(inventory, slot, x, y, null, null);
    }

    protected void addInputSlot(Inventory inventory, int slot, int x, int y, @Nullable Predicate<ItemStack> mayPlace, @Nullable Predicate<Player> mayPickup) {
        inputSlots.add(addSlot(new Slot(inventory, slot, x, y) {

            @Override
            public boolean mayPickup(Player player) {
                if (mayPickup != null) {
                    return mayPickup.test(player);
                }
                return super.mayPickup(player);
            }

            @Override
            public boolean mayPlace(ItemStack itemStack) {
                if (mayPlace != null) {
                    return mayPlace.test(itemStack);
                }
                return super.mayPlace(itemStack);
            }

            @Override
            public void setChanged() {
                super.setChanged();
                onInputSlotChange(this);
            }
        }));
    }

    protected void addOutputSlot(Inventory inventory, int slot, int x, int y) {
        outputSlots.add(addSlot(new Slot(inventory, slot, x, y) {

            @Override
            public boolean mayPlace(ItemStack itemStack) {
                return false;
            }

            @Override
            public void setChanged() {
                super.setChanged();
                onOutputSlotChange(this);
            }
        }));
    }

    protected void addDataSlot(Inventory inventory, int slot, int x, int y) {
        addDataSlot(inventory, slot, x, y, null, null);
    }

    protected void addDataSlot(Inventory inventory, int slot, int x, int y, @Nullable Predicate<ItemStack> mayPlace, @Nullable Predicate<Player> mayPickup) {
        dataSlots.add(addSlot(new Slot(inventory, slot, x, y) {

            @Override
            public boolean mayPickup(Player player) {
                if (mayPickup != null) {
                    return mayPickup.test(player);
                }
                return super.mayPickup(player);
            }

            @Override
            public boolean mayPlace(ItemStack itemStack) {
                if (mayPlace != null) {
                    return mayPlace.test(itemStack);
                }
                return super.mayPlace(itemStack);
            }

            @Override
            public void setChanged() {
                super.setChanged();
                onDataSlotChange(this);
            }
        }));
    }

    protected void onInputSlotChange(Slot slot) {
    }

    protected void onOutputSlotChange(Slot slot) {
    }

    protected void onDataSlotChange(Slot slot) {
    }


    public static class PlaceFilter {

        public static Predicate<ItemStack> only(Item item) {
            return itemStack -> itemStack.is(item);
        }
    }

}
