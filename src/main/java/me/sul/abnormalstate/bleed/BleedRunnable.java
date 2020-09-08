package me.sul.abnormalstate.bleed;

import org.bukkit.scheduler.BukkitRunnable;

public abstract class BleedRunnable extends BukkitRunnable {
    double remainDamage;
    public BleedRunnable(double remainDamage) {
        this.remainDamage = remainDamage;
    }
}
