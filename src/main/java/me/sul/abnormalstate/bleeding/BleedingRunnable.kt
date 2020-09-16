package me.sul.abnormalstate.bleeding;

import org.bukkit.scheduler.BukkitRunnable;

public abstract class BleedingRunnable extends BukkitRunnable {
    double remainDamage;
    public BleedingRunnable(double remainDamage) {
        this.remainDamage = remainDamage;
    }
}
