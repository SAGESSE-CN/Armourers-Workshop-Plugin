package moe.plushie.armourers_workshop.plugin.api;

import moe.plushie.armourers_workshop.plugin.init.ModLog;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class Menu {

    private final String registryName;
    private final Player player;
    private final ArrayList<Slot> slots = new ArrayList<>();

    public Menu(String registryName, Player player) {
        this.registryName = registryName;
        this.player = player;
    }

    public void handSlotClick(int slotId, String type) {
        Slot slot = findSlot(slotId);
        ModLog.info("你点击了容器, 槽位ID: {}, 类型: {}, 槽: {}", slotId, type, slot);
//        if (slot != null && slot.getItem() != null) {
//            player.getOpenInventory().setCursor(slot.getItem());
//        }

        //    PICKUP,
        //    QUICK_MOVE,
        //    SWAP,
        //    CLONE,
        //    THROW,
        //    QUICK_CRAFT,
        //    PICKUP_ALL;


    }

    @Nullable
    public Slot findSlot(int slotId) {
        if (slotId >= 0 && slotId < slots.size()) {
            return slots.get(slotId);
        }
        return null;
    }

    public void addSlot(Slot slot) {
        slots.add(slot);
    }

    public String getRegistryName() {
        return registryName;
    }

}

