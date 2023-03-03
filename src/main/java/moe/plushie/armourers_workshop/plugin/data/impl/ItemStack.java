package moe.plushie.armourers_workshop.plugin.data.impl;

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
        this.id = tag.getString("id");
        this.count = tag.getByte("Count");
        if (tag.containsKey("tag")) {
            this.tag = tag.getCompoundTag("tag");
        }
    }

    public void save(CompoundTag tag) {
        tag.putString("id", id);
        tag.putByte("Count", (byte) count);
        tag.put("tag", this.tag);
    }

    public CompoundTag getTag() {
        return tag;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }
}
