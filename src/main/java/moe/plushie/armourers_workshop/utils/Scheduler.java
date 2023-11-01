package moe.plushie.armourers_workshop.utils;

import net.cocoonmc.Cocoon;
import org.bukkit.Bukkit;

public class Scheduler {

    /**
     * Run a task that will run on the next server tick.
     *
     * @param task the task to be run
     */
    public static void run(Runnable task) {
        Bukkit.getScheduler().runTask(Cocoon.getPlugin(), task);
    }

    /**
     * Run a task that will run asynchronously.
     *
     * @param task the task to be run
     */
    public static void runAsync(Runnable task) {
        Bukkit.getScheduler().runTaskAsynchronously(Cocoon.getPlugin(), task);
    }

    /**
     * Run a task that will run after the specified number of server ticks.
     *
     * @param task the task to be run
     * @param tick the ticks to wait before running the task
     */
    public static void runLater(Runnable task, int tick) {
        Bukkit.getScheduler().runTaskLater(Cocoon.getPlugin(), task, tick);
    }

    /**
     * Run a task that will run asynchronously after the specified number of server ticks.
     *
     * @param task the task to be run
     * @param tick the ticks to wait before running the task
     */
    public static void runLaterAsync(Runnable task, int tick) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(Cocoon.getPlugin(), task, tick);
    }

}
