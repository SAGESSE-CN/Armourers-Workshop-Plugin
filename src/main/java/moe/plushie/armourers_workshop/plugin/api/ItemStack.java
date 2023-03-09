package moe.plushie.armourers_workshop.plugin.api;

import net.querz.nbt.tag.CompoundTag;

public class ItemStack {

    public static final ItemStack EMPTY = new ItemStack("minecraft:air");

    private String id;
    private int count;
    private CompoundTag tag = new CompoundTag();


    public ItemStack(String id) {
        this(id, 1);
    }

    public ItemStack(String id, int count) {
        this.id = id;
        this.count = count;
    }

    public ItemStack(CompoundTag tag) {
        CompoundTag itemTag = null;
        if (tag.containsKey("tag")) {
            itemTag = tag.getCompoundTag("tag");
        }
        String id = tag.getString("id");
        if (itemTag != null && itemTag.containsKey("__redirected_id__")) {
            id = itemTag.getString("__redirected_id__");
        }
        this.id = id;
        this.count = tag.getByte("Count");
        if (itemTag != null) {
            this.tag = itemTag;
        }
    }

    public CompoundTag save(CompoundTag tag) {
        CompoundTag itemTag = this.tag.clone();
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
        return this == EMPTY;
    }
}
