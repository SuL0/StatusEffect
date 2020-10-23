package kr.sul.abnormalstate.state.thirst

import kr.sul.abnormalstate.AbnormalState.Companion.plugin
import kr.sul.abnormalstate.playerstate.PlayerStateManager.getPlayerState
import kr.sul.abnormalstate.state.StateManager
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.roundToInt

object Thirst: Listener {
    // 갈증 주기 10분
    private const val THIRST_DECREASE_PERIOD = 6*20L

    init {
        registerThirstScheduler()
    }

    private fun registerThirstScheduler() {
        object : BukkitRunnable() {
            override fun run() {
                for (p in Bukkit.getServer().onlinePlayers
                                .filter { StateManager.isValidTargetPlayer(it) }) {
                    if (getPlayerState(p).thirst > 0) {
                        if(p.isSprinting) {
                            getPlayerState(p).thirst -= (0.5 + Math.random())*1.3
                        } else {
                            getPlayerState(p).thirst -= 0.5 + Math.random()
                        }
                        // 갈증이 음수로 내려가는 것 방지
                        if (getPlayerState(p).thirst < 0) {
                            getPlayerState(p).thirst = 0.0
                        }
                    }

                    when(val thirst = getPlayerState(p).thirst.roundToInt()) {
                        in 20 downTo 10 -> {
                            if (thirst % 3 == 0) p.sendMessage("§c§lTHIRST: §7목이 말라옵니다. §9물§7을 섭취하십시오.")
                        }
                        in 10 downTo 1 -> {
                            if (thirst % 3 == 0) p.sendMessage("§c§lTHIRST: §7목이 상당히 마릅니다. §9물§7을 섭취하십시오.")
                        }
                        0 -> {
                            if (Math.random() <= 0.3) p.sendMessage("§c§lTHIRST: §7목이 말라 §c데미지§7를 입고 있습니다! §9물§7을 섭취하십시오.")
                            p.damage(1.0)
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, THIRST_DECREASE_PERIOD, THIRST_DECREASE_PERIOD)
    }
}