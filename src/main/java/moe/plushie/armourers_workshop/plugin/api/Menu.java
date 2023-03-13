package moe.plushie.armourers_workshop.plugin.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class Menu {

    protected final String registryName;
    protected final ArrayList<Slot> slots = new ArrayList<>();

    public Menu(String registryName) {
        this.registryName = registryName;
    }

    public boolean handSlotClick(int slot, int button, ClickType type, Player player) {
        return false;
    }

    @Nullable
    public Slot safeGetSlot(int slotId) {
        if (slotId >= 0 && slotId < slots.size()) {
            return slots.get(slotId);
        }
        return null;
    }

    public void addSlot(Slot slot) {
        slot.setIndex(slots.size());
        slots.add(slot);
    }

    public String getRegistryName() {
        return registryName;
    }
}

