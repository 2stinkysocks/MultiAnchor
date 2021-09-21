package com.twostinkysocks.multianchor;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class RepeatingTask implements Runnable {

    private int taskId;

    public RepeatingTask(JavaPlugin plugin, long arg1, long arg2) {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, arg1, arg2);
    }

    public void cancelTask() {
        Bukkit.getScheduler().cancelTask(taskId);
    }

}