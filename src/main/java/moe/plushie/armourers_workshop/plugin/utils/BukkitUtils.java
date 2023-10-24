package moe.plushie.armourers_workshop.plugin.utils;

import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.reflect.accessors.FieldAccessor;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import moe.plushie.armourers_workshop.customapi.CustomBlock;
import moe.plushie.armourers_workshop.plugin.api.Block;
import moe.plushie.armourers_workshop.plugin.api.BlockEntity;
import moe.plushie.armourers_workshop.plugin.api.BlockPos;
import moe.plushie.armourers_workshop.plugin.api.BlockState;
import moe.plushie.armourers_workshop.plugin.api.Blocks;
import moe.plushie.armourers_workshop.plugin.api.ItemStack;
import moe.plushie.armourers_workshop.plugin.api.Items;
import moe.plushie.armourers_workshop.plugin.api.UseOnContext;
import net.querz.nbt.io.SNBTUtil;
import net.querz.nbt.tag.CompoundTag;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Skull;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class BukkitUtils {

    private static FieldAccessor GET_PROFILE;
    public static final org.bukkit.inventory.ItemStack EMPTY_STACK = new org.bukkit.inventory.ItemStack(Material.AIR, 0);

    public static ItemStack wrap(org.bukkit.inventory.ItemStack itemStack) {
        if (itemStack == null || itemStack == EMPTY_STACK || itemStack.getType() == Material.AIR || itemStack.getAmount() <= 0) {
            return ItemStack.EMPTY;
        }
        return new WrappedItemStack(itemStack);
    }

    public static org.bukkit.inventory.ItemStack unwrap(ItemStack itemStack) {
        if (itemStack == null || itemStack == ItemStack.EMPTY || itemStack.isEmpty()) {
            return EMPTY_STACK;
        }
        // yep, we not need convert it.
        if (itemStack instanceof WrappedItemStack) {
            return ((WrappedItemStack) itemStack).itemStack;
        }
        return FastCache.ITEM_TO_BUKKIT_ITEM.computeIfAbsent(itemStack, it -> {
            try {
                CompoundTag tag = new CompoundTag();
                itemStack.save(tag);
                return NBTEditor.getItemFromTag(NBTEditor.getNBTCompound(SNBTUtil.toSNBT(tag)));
            } catch (Exception e) {
                return null;
            }
        });
    }

    public static Block wrap(org.bukkit.block.Block block) {
        Material type = block.getType();
        // maybe is dynamic type.
        if (type == Material.PLAYER_HEAD || type == Material.PLAYER_WALL_HEAD) {
            BlockEntity blockEntity = getBlockEntity(block);
            if (blockEntity != null) {
                return blockEntity.getBlock();
            }
        }
        return Blocks.byId(type.getKey().toString());
    }

    public static BlockEntity getBlockEntity(org.bukkit.block.Block block) {
        Material type = block.getType();
        if (type != Material.PLAYER_HEAD && type != Material.PLAYER_WALL_HEAD) {
            return null;
        }
        // maybe is dynamic type.
        Skull skull = (Skull) block.getState();
        if (skull.getOwningPlayer() == null || skull.getOwningPlayer().getName() != null) {
            return null;
        }
        return FastCache.STATE_TO_BLOCK_ENTITY.computeIfAbsent(skull, key -> {
            String texture = getSkullTexture(block);
            return Blocks.loadFromTexture(skull.getWorld(), BlockPos.of(skull.getLocation()), texture);
        });
    }

    public static void giveItemTo(ItemStack itemStack, Player player) {
        Inventory inventory = player.getInventory();
        if (isFull(inventory)) {
            drop(itemStack, player, true);
        } else {
            inventory.addItem(unwrap(itemStack));
        }
    }

    public static void drop(ItemStack itemStack, Player player, boolean bl2) {
        org.bukkit.inventory.ItemStack itemStack1 = unwrap(itemStack);
        if (itemStack1 != null) {
            Scheduler.run(() -> {
                player.getWorld().dropItem(player.getLocation(), itemStack1);
//            if (bl2) {
//                itemEntity.setThrower(player.getUUID());
//            }
            });
        }
    }

    public static boolean isFull(Inventory inventory) {
        return inventory.firstEmpty() < 0;
    }

    public static boolean isInteractable(org.bukkit.block.Block block) {
        return wrap(block).isInteractable();
    }

    public static class WrappedItemStack extends ItemStack {

        private CompoundTag lazyTag;
        private final org.bukkit.inventory.ItemStack itemStack;

        public WrappedItemStack(org.bukkit.inventory.ItemStack itemStack) {
            super(Items.byId(realId(itemStack)), itemStack.getAmount());
            this.itemStack = itemStack;
        }

        @Override
        public void setCount(int count) {
            super.setCount(count);
            itemStack.setAmount(count);
        }

        @Override
        public CompoundTag getTag() {
            if (lazyTag != null) {
                return lazyTag;
            }
            lazyTag = realTag(itemStack);
            return lazyTag;
        }

        @Override
        public int getMaxStackSize() {
            return itemStack.getMaxStackSize();
        }

        @Override
        public ItemStack copy() {
            return new WrappedItemStack(itemStack.clone());
        }

        private static String realId(org.bukkit.inventory.ItemStack itemStack) {
            String id = itemStack.getType().getKey().toString();
            String redirectedId = NBTEditor.getString(itemStack, "__redirected_id__");
            if (redirectedId != null && !redirectedId.isEmpty()) {
                String newId = Items.getRealId(redirectedId);
                if (newId != null) {
                    return newId;
                }
            }
            return id;
        }

        private static CompoundTag realTag(org.bukkit.inventory.ItemStack itemStack) {
            try {
                if (itemStack.hasItemMeta()) {
                    NBTEditor.NBTCompound tag = NBTEditor.getNBTCompound(itemStack, "tag");
                    if (tag != null) {
                        return (CompoundTag) SNBTUtil.fromSNBT(tag.toJson());
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return new CompoundTag();
        }
    }


    public static void setBlock(UseOnContext context, String id, @Nullable BlockState state, @Nullable CompoundTag tag) {
        try {
            String skinURL = getSkinURL(id, state, tag);

            // ..
            Location pos = context.getClickedBlock().getLocation();
            Location location = context.getClickedBlock().getLocation();
            CustomBlock.BlockPlaceContext context1;
            context1 = new CustomBlock.BlockPlaceContext(context.getPlayer(), context.getHand(), 2, pos, location, context.getClickedFace());
            CustomBlock.placeItem(NBTEditor.getHead(skinURL), context1, context.getHand());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setBlockAndUpdate(UseOnContext context, String id, @Nullable BlockState state, @Nullable CompoundTag tag) {
        setBlock(context, id, state, tag);
        org.bukkit.block.Block target = context.getClickedBlock().getRelative(context.getClickedFace());
        if (!target.getType().isAir()) {
            target.getState().update(true, false);
        }
    }

    public static void updateBlock(org.bukkit.block.Block block, String id, @Nullable BlockState state, @Nullable CompoundTag tag) {
        try {
            String skinURL = getSkinURL(id, state, tag);
            if (block.getType() != Material.PLAYER_HEAD) {
                block.setType(Material.PLAYER_HEAD, true);
            }
            setSkullTexture(block, skinURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateBlockAndUpdate(org.bukkit.block.Block block, String id, @Nullable BlockState state, @Nullable CompoundTag tag) {
        updateBlock(block, id, state, tag);
        block.getState().update(true, false);
    }

    public static Entity findEntity(World world, int entityId) {
        for (Entity entity : world.getEntities()) {
            if (entity.getEntityId() == entityId) {
                return entity;
            }
        }
        return null;
    }

    private static String getSkinURL(String id, @Nullable BlockState state, @Nullable CompoundTag tag) throws IOException {
        CompoundTag blockTag = new CompoundTag();
        blockTag.putString("id", id);
        if (state != null) {
            CompoundTag stateTag = state.serialize();
            if (stateTag.size() != 0) {
                blockTag.put("state", stateTag);
            }
        }
        if (tag != null) {
            blockTag.put("tag", tag);
        }
        String value = SNBTUtil.toSNBT(blockTag);
        return "\",__redirected_block__:" + value + ",__block_redirected__:\"";
    }


    private static String getSkullTexture(org.bukkit.block.Block block) {
        Skull skull = (Skull) block.getState();
        if (GET_PROFILE == null) {
            GET_PROFILE = Accessors.getFieldAccessorOrNull(skull.getClass(), "profile", Object.class);
            if (GET_PROFILE == null) {
                return null;
            }
        }
        Object handle = GET_PROFILE.get(skull);
        if (handle == null) {
            return null;
        }
        WrappedGameProfile profile = WrappedGameProfile.fromHandle(handle);
        WrappedSignedProperty property = profile.getProperties().get("textures").stream().findFirst().orElse(null);
        if (property == null) {
            return null;
        }
        return property.getValue();
    }

    private static void setSkullTexture(org.bukkit.block.Block block, String texture) {
        NBTEditor.setSkullTexture(block, texture);
    }
}
