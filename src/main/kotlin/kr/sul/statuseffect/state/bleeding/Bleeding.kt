package kr.sul.statuseffect.state.bleeding

import kr.sul.statuseffect.StatusEffect.Companion.plugin
import kr.sul.statuseffect.playerstate.PlayerStateManager.getPlayerState
import kr.sul.statuseffect.state.StateManager
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

object Bleeding : Listener {
    private const val BLEEDING_PERIOD = 10L // tick
    private const val MAX_BLEED_DAMAGE_PER_ONCE = 0.5 // tick

    private val bleedingRunnableMap: MutableMap<Player, BleedingRunnable> = HashMap()

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onDamage(e: EntityDamageEvent) {
        if (e.entity !is Player || !StateManager.isValidTargetPlayer(e.entity as Player)) return
        if (e.cause == EntityDamageEvent.DamageCause.CUSTOM || e.finalDamage <= 0) return
        val victim = e.entity as Player
        makePlayerBleedingFewTimes(victim, BleedingUtil.calcTotalDamageOfBleeding(e.damage))
    }
    private fun makePlayerBleedingFewTimes(p: Player, bleedDamage: Double) {
        if (bleedDamage < 0.5) return

        if (isCurrentBleedingWorthThanOriginalBleeding(p, bleedDamage)) {
            // 기존 Runnable cancel
            if (bleedingRunnableMap.containsKey(p)) {
                bleedingRunnableMap[p]!!.cancel()
            }

            getPlayerState(p).isBleeding = true
            val bleedingRunnable: BleedingRunnable = object : BleedingRunnable(bleedDamage) { // 익명클래스 = 상속의 연장
                override fun run() {
                    // Bleeding 끝
                    if (remainDamage <= 0 || p.isDead) { // isDead가 ServerQuit 도 포함
                        getPlayerState(p).isBleeding = false
                        bleedingRunnableMap.remove(p)
                        cancel()
                        return
                    }
                    val damageToInflict = Math.min(remainDamage, MAX_BLEED_DAMAGE_PER_ONCE)
                    remainDamage -= damageToInflict
                    BleedingUtil.causeBleeding(p, damageToInflict)
                }
            }
            bleedingRunnableMap[p] = bleedingRunnable
            bleedingRunnable.runTaskTimer(plugin, BLEEDING_PERIOD, BLEEDING_PERIOD)
        }
    }

    // 기존에 실행되고 있던 Runnable과 현재 Runnable 중 무엇이 더 가치있는지 비교
    private fun isCurrentBleedingWorthThanOriginalBleeding(p: Player, currentBleedDamage: Double): Boolean {
        if (!bleedingRunnableMap.containsKey(p)) return true
        val originalBleedDamage = bleedingRunnableMap[p]!!.remainDamage
        return originalBleedDamage < currentBleedDamage
    }
}