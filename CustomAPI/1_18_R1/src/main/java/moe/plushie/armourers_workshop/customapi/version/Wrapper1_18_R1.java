package moe.plushie.armourers_workshop.customapi.version;

import moe.plushie.armourers_workshop.customapi.*;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.network.protocol.game.PacketPlayOutCloseWindow;
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.EnumHand;
import net.minecraft.world.IInventory;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.inventory.Container;
import net.minecraft.world.inventory.Containers;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.ItemActionContext;
import net.minecraft.world.phys.MovingObjectPositionBlock;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R1.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class Wrapper1_18_R1 implements CustomAPI {

    @Override
    public CustomSlotImpl createCustomSlot(CustomSlotProvider impl, Inventory inventory, int index, int x, int y) {
        IInventory inventory1 = ((CraftInventory) inventory).getInventory();
        return new AbstractCustomSlot(impl, inventory1, index, x, y);
    }

    @Override
    public CustomMenuImpl createCustomMenu(CustomMenuProvider impl, Player player, InventoryHolder holder, int size, String title) {
        CraftInventoryCustom inventory = new CraftInventoryCustom(holder, size, title);
        return new AbstractCustomMenu(impl, toNMS(player), inventory);
    }

    @Override
    public CustomBlockImpl createCustomBlock() {
        return new AbstractCustomBlock();
    }

    public static class AbstractCustomSlot extends Slot implements CustomSlotImpl {

        private final CustomSlotProvider impl;

        public AbstractCustomSlot(CustomSlotProvider impl, IInventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
            this.impl = impl;
        }

        @Override
        public boolean f() {
            return impl.hasItem();
        }

        @Override
        public boolean hasContents() {
            return super.f();
        }

        @Override
        public void d(ItemStack itemStack) {
            impl.set(fromNMS(itemStack));
        }

        @Override
        public void setContents(org.bukkit.inventory.ItemStack itemStack) {
            super.d(toNMS(itemStack));
        }

        @Override
        public ItemStack e() {
            return toNMS(impl.getItem());
        }

        @Override
        public org.bukkit.inventory.ItemStack getContents() {
            return fromNMS(super.e());
        }

        @Override
        public ItemStack a(int var0) {
            return toNMS(impl.remove(var0));
        }

        @Override
        public org.bukkit.inventory.ItemStack removeContents(int i) {
            return fromNMS(super.a(i));
        }

        @Override
        public boolean a(ItemStack var0) {
            return impl.mayPlace(fromNMS(var0));
        }

        @Override
        public boolean mayPlace(org.bukkit.inventory.ItemStack itemStack) {
            return super.a(toNMS(itemStack));
        }

        @Override
        public boolean a(EntityHuman var0) {
            return impl.mayPickup(fromNMS(var0));
        }

        @Override
        public boolean mayPickup(Player player) {
            return super.a(toNMS(player));
        }

        @Override
        public void d() {
            impl.setChanged();
        }

        @Override
        public void setChanged() {
            super.d();
        }
    }

    public static class AbstractCustomMenu extends Container implements CustomMenuImpl {

        private final EntityPlayer playerEntity;
        private final CraftInventory inventory;
        private final CustomMenuProvider impl;

        private CraftInventoryView inventoryView;

        public AbstractCustomMenu(CustomMenuProvider impl, EntityPlayer player, CraftInventory inventory) {
            super(Containers.f, player.nextContainerCounter());
            this.playerEntity = player;
            this.inventory = inventory;
            this.impl = impl;
        }

        @Override
        public boolean a(EntityHuman human) {
            return impl.stillValid(fromNMS(human));
        }

        @Override
        public ItemStack b(EntityHuman human, int i) {
            return toNMS(impl.quickMoveStack(fromNMS(human), i));
        }

        @Override
        public void d() {
            impl.broadcastChanges();
        }

        @Override
        public void broadcastChanges() {
            super.d();
        }

        @Override
        public void addSlot(CustomSlotImpl slot) {
            super.a((Slot) slot);
        }

        @Override
        public boolean moveItemStackTo(org.bukkit.inventory.ItemStack itemStack, int i, int j, boolean bl) {
            return a(toNMS(itemStack), i, j, bl);
        }

        @Override
        public void openContainer() {
            impl.handleOpenWindowPacket(j);
            playerEntity.bW = this;
            playerEntity.a(this);
        }

        @Override
        public void closeContainer() {
            CraftEventFactory.handleInventoryCloseEvent(playerEntity);
            playerEntity.bW = playerEntity.bV;
            impl.handleCloseWindowPacket(j);
        }

        @Override
        public void sendOpenWindowPacket(int windowId) {
            playerEntity.b.a(new PacketPlayOutOpenWindow(windowId, a(), getTitle()));
        }

        @Override
        public void sendCloseWindowPacket(int windowId) {
            playerEntity.b.a(new PacketPlayOutCloseWindow(windowId));
        }

        @Override
        public CraftInventory getInventory() {
            return inventory;
        }

        @Override
        public CraftInventoryView getBukkitView() {
            if (inventoryView == null) {
                inventoryView = new CraftInventoryView(playerEntity.getBukkitEntity(), inventory, this);
            }
            return inventoryView;
        }
    }

    public static class AbstractCustomBlock implements CustomBlockImpl {

        @Override
        public int placeItem(org.bukkit.inventory.ItemStack itemStack, IBlockPlaceContext context, EquipmentSlot hand) {
            ItemStack itemStack1 = toNMS(itemStack);
            EnumHand hand1 = toNMS2(hand);
            Vec3D vec1 = toNMS_V3d(context.getClickedPos());
            BlockPosition pos1 = toNMS_BP(context.getClickedLocation());
            EnumDirection dir1 = toNMS2(context.getClickedFace());
            MovingObjectPositionBlock hitResult;
            if (context.getClickedType() != 0) {
                hitResult = new MovingObjectPositionBlock(vec1, dir1, pos1, context.getClickedType() == 1);
            } else {
                hitResult = MovingObjectPositionBlock.a(vec1, dir1, pos1);
            }
            EntityPlayer player = toNMS(context.getPlayer());
            ItemActionContext context1 = new ItemActionContext(player.cA(), player, toNMS2(context.getHand()), itemStack1, hitResult);
            return itemStack1.useOn(context1, hand1).ordinal();
        }
    }

    private static EntityPlayer toNMS(Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    private static Player fromNMS(EntityHuman entity) {
        return (Player) entity.getBukkitEntity();
    }

    private static ItemStack toNMS(org.bukkit.inventory.ItemStack itemStack) {
        return Proxy.of(itemStack, CraftItemStack::asNMSCopy);
    }

    private static org.bukkit.inventory.ItemStack fromNMS(ItemStack itemStack) {
        return Proxy.of(itemStack, CraftItemStack::asCraftMirror);
    }

    private static Vec3D toNMS_V3d(Location location) {
        return new Vec3D(location.getX(), location.getY(), location.getZ());
    }

    private static BlockPosition toNMS_BP(Location location) {
        return new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    private static EnumHand toNMS2(EquipmentSlot slot) {
        if (slot == EquipmentSlot.OFF_HAND) {
            return EnumHand.b;
        }
        return EnumHand.a;
    }

    private static EnumDirection toNMS2(BlockFace face) {
        switch (face) {
            case NORTH:
                return EnumDirection.c;
            case SOUTH:
                return EnumDirection.d;
            case EAST:
                return EnumDirection.f;
            case WEST:
                return EnumDirection.e;
            case UP:
                return EnumDirection.b;
            case DOWN:
                return EnumDirection.a;
            default:
                return EnumDirection.c;
        }
    }
}
