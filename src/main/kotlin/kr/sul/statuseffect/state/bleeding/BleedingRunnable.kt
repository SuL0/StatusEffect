package kr.sul.statuseffect.state.bleeding

import org.bukkit.scheduler.BukkitRunnable

abstract class BleedingRunnable(var remainDamage: Double) : BukkitRunnable()