package kr.sul.abnormalstate.bleeding

import org.bukkit.scheduler.BukkitRunnable

abstract class BleedingRunnable(var remainDamage: Double) : BukkitRunnable()