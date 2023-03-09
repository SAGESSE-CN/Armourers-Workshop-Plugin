package moe.plushie.armourers_workshop.plugin.utils;

import moe.plushie.armourers_workshop.plugin.ArmourersWorkshop;
import org.bukkit.Bukkit;

public class Scheduler {

    /**
     * Run a task that will run on the next server tick.
     *
     * @param task the task to be run
     */
    public static void run(Runnable task) {
        Bukkit.getScheduler().runTask(ArmourersWorkshop.INSTANCE, task);
    }

    /**
     * Run a task that will run asynchronously.
     *
     * @param task the task to be run
     */
    public static void runAsync(Runnable task) {
        Bukkit.getScheduler().runTaskAsynchronously(ArmourersWorkshop.INSTANCE, task);
    }

    /**
     * Run a task that will run after the specified number of server ticks.
     *
     * @param task the task to be run
     * @param tick the ticks to wait before running the task
     */
    public static void runLater(Runnable task, int tick) {
        Bukkit.getScheduler().runTaskLater(ArmourersWorkshop.INSTANCE, task, tick);
    }

    /**
     * Run a task that will run asynchronously after the specified number of server ticks.
     *
     * @param task the task to be run
     * @param tick the ticks to wait before running the task
     */
    public static void runLaterAsync(Runnable task, int tick) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(ArmourersWorkshop.INSTANCE, task, tick);
    }

}
