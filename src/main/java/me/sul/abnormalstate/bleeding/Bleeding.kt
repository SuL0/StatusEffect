package me.sul.abnormalstate.bleeding

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.plugin.Plugin
import java.util.*

class Bleeding(private val plugin: Plugin) : Listener {
    private val bleedingRunnableMap: MutableMap<Player, BleedingRunnable?> = HashMap()

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onDamage(e: EntityDamageEvent) {
        if (e.entity !is Player || e.cause == EntityDamageEvent.DamageCause.CUSTOM || e.finalDamage <= 0) return
        val victim = e.entity as Player
        addBleedScheduler(victim, BleedingUtil.calcTotalDamageOfBleeding(e.damage))
    }
    private fun addBleedScheduler(p: Player, bleedDamage: Double) {
        if (bleedDamage < 0.5) return
        if (isCurrentBleedingWorthThanOriginalBleeding(p, bleedDamage)) {
            // 기존 Runnable cancel
            if (bleedingRunnableMap.containsKey(p)) {
                bleedingRunnableMap[p]!!.cancel()
            }
            val bleedingRunnable: BleedingRunnable = object : BleedingRunnable(bleedDamage) {
                override fun run() {
                    if (remainDamage <= 0 || p.isDead) {
                        bleedingRunnableMap.remove(p)
                        cancel()
                        return
                    }
                    val damage = Math.min(remainDamage, MAX_BLEED_DAMAGE_PER_ONCE)
                    remainDamage -= damage
                    BleedingUtil.causeBleeding(p, damage)
                }
            }
            bleedingRunnableMap[p] = bleedingRunnable
            bleedingRunnable.runTaskTimer(plugin, BLEEDING_PERIOD.toLong(), BLEEDING_PERIOD.toLong())
        }
    }

    // 기존에 실행되고 있던 Runnable과 현재 Runnable 중 무엇이 더 가치있는지 비교
    private fun isCurrentBleedingWorthThanOriginalBleeding(p: Player, currentBleedDamage: Double): Boolean {
        if (!bleedingRunnableMap.containsKey(p)) return true
        val originalBleedDamage = bleedingRunnableMap[p]!!.remainDamage
        return originalBleedDamage < currentBleedDamage
    }

    companion object {
        private const val BLEEDING_PERIOD = 10 // tick
        private const val MAX_BLEED_DAMAGE_PER_ONCE = 0.5 // tick
    }
}