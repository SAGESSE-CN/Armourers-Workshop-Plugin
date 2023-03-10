package moe.plushie.armourers_workshop.plugin.api;

public interface Slot {

     ItemStack getItem();

    boolean hasItem();

    void set(ItemStack itemStack);

    default void setChanged() {

    }
}
