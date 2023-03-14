package moe.plushie.armourers_workshop.plugin.api;

import net.querz.nbt.tag.CompoundTag;

public class ItemStack {

    public static final ItemStack EMPTY = new ItemStack("minecraft:air", 0);

    private String id;
    private int count;
    private final CompoundTag tag;

    public ItemStack(String id) {
        this(id, 1);
    }

    public ItemStack(String id, int count) {
        this(id, count, new CompoundTag());
    }

    public ItemStack(String id, int count, CompoundTag tag) {
        this.id = id;
        this.count = count;
        this.tag = tag;
    }

    public ItemStack(CompoundTag tag) {
        CompoundTag itemTag = tag;
        if (tag.containsKey("tag")) {
            itemTag = tag.getCompoundTag("tag");
        }
        String id = tag.getString("id");
        if (itemTag != null && itemTag.containsKey("__redirected_id__")) {
            id = itemTag.getString("__redirected_id__");
        }
        if (itemTag == null) {
            itemTag = new CompoundTag();
        }
        this.id = id;
        this.count = tag.getByte("Count");
        this.tag = itemTag;
    }

    public CompoundTag save(CompoundTag tag) {
        CompoundTag itemTag = getTag().clone();
        if (id.startsWith("minecraft:")) {
            tag.putString("id", id);
        } else {
            itemTag.putString("__redirected_id__", id);
            tag.putString("id", "minecraft:paper");
        }
        tag.putByte("Count", (byte) count);
        tag.put("tag", itemTag);
        return tag;
    }

    public CompoundTag getTag() {
        return tag;
    }

    public boolean isEmpty() {
        if (this == EMPTY) {
            return true;
        }
        return count <= 0;
    }


    public ItemStack copy() {
        return new ItemStack(id, count, tag.clone());
    }

    public ItemStack split(int i) {
        int j = Math.min(i, count);
        ItemStack itemStack = copy();
        itemStack.setCount(j);
        shrink(j);
        return itemStack;
    }


    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void grow(int i) {
        this.setCount(this.count + i);
    }

    public void shrink(int i) {
        this.grow(-i);
    }

    public int getMaxStackSize() {
        return Item.getMaxStackSizeById(id);
    }

    public boolean isStackable() {
        return getMaxStackSize() > 1;
    }

    public boolean isDamageableItem() {
        return false;
    }

    public static boolean tagMatches(ItemStack itemStack, ItemStack itemStack2) {
        if (itemStack.isEmpty() && itemStack2.isEmpty()) {
            return true;
        } else if (!itemStack.isEmpty() && !itemStack2.isEmpty()) {
            if (itemStack.getTag() == null && itemStack2.getTag() != null) {
                return false;
            } else {
                return itemStack.getTag() == null || itemStack.getTag().equals(itemStack2.getTag());
            }
        } else {
            return false;
        }
    }

    //    public static boolean matches(ItemStack itemStack, ItemStack itemStack2) {
//        if (itemStack.isEmpty() && itemStack2.isEmpty()) {
//            return true;
//        } else {
//            return !itemStack.isEmpty() && !itemStack2.isEmpty() ? itemStack.matches(itemStack2) : false;
//        }
//    }
//
//    private boolean matches(ItemStack itemStack) {
//        if (this.count != itemStack.count) {
//            return false;
//        } else if (!this.is(itemStack.getItem())) {
//            return false;
//        } else if (this.tag == null && itemStack.tag != null) {
//            return false;
//        } else {
//            return this.tag == null || this.tag.equals(itemStack.tag);
//        }
//    }
//
    public static boolean isSame(ItemStack itemStack, ItemStack itemStack2) {
        if (itemStack == itemStack2) {
            return true;
        } else {
            return !itemStack.isEmpty() && !itemStack2.isEmpty() && itemStack.sameItem(itemStack2);
        }
    }

//    public static boolean isSameIgnoreDurability(ItemStack itemStack, ItemStack itemStack2) {
//        if (itemStack == itemStack2) {
//            return true;
//        } else {
//            return !itemStack.isEmpty() && !itemStack2.isEmpty() && itemStack.sameItemStackIgnoreDurability(itemStack2);
//        }
//    }

    public boolean sameItem(ItemStack itemStack) {
        return !itemStack.isEmpty() && id.equals(itemStack.id);
    }

    //    public boolean sameItemStackIgnoreDurability(ItemStack itemStack) {
//        if (!this.isDamageableItem()) {
//            return this.sameItem(itemStack);
//        } else {
//            return !itemStack.isEmpty() && id.equals(itemStack.id);
//        }
//    }
//
    public static boolean isSameItemSameTags(ItemStack itemStack, ItemStack itemStack2) {
        return itemStack.id.equals(itemStack2.id) && tagMatches(itemStack, itemStack2);
    }

//    public boolean overrideStackedOnOther(Slot slot, ClickAction clickAction, Player player) {
//        return false;
//    }
//
//    public boolean overrideOtherStackedOnMe(ItemStack itemStack2, Slot slot, ClickAction clickAction, Player player, Object slotAccess) {
//        return false;
//    }


    @Override
    public String toString() {
        return "" + count + " " + id;
    }
}
