package kr.sul.abnormalstate.state.bleeding

import org.bukkit.scheduler.BukkitRunnable

abstract class BleedingRunnable(var remainDamage: Double) : BukkitRunnable()