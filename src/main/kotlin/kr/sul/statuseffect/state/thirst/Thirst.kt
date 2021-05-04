package kr.sul.statuseffect.state.thirst

import kr.sul.statuseffect.StatusEffect.Companion.plugin
import kr.sul.statuseffect.playerstate.PlayerState
import kr.sul.statuseffect.playerstate.PlayerStateManager.getPlayerState
import kr.sul.statuseffect.state.StateManager
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent
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
                            if (thirst % 4 == 0) p.sendMessage("§c§lTHI: §7목이 말라옵니다. §9물§7을 섭취하십시오.")
                        }
                        in 10 downTo 1 -> {
                            if (thirst % 3 == 0) p.sendMessage("§c§lTHI: §7목이 상당히 마릅니다. §9물§7을 섭취하십시오.")
                        }
                        0 -> {
                            if (Math.random() <= 0.3) p.sendMessage("§c§lTHI: §7목이 말라 §c데미지§7를 입고 있습니다! §9물§7을 섭취하십시오.")
                            p.damage(1.0)
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, THIRST_DECREASE_PERIOD, THIRST_DECREASE_PERIOD)
    }


    // ActiveWorld 가 아닌 곳으로 이동할 시, 갈증 초기화
    // 죽을 시 상태 초기화는 필요없을 듯. onMoveWorld 가 있으니
    @EventHandler
    fun onMoveWorld(e: PlayerChangedWorldEvent) {
        if (StateManager.isInActiveWorld(e.player.world)) return
        getPlayerState(e.player).thirst = PlayerState.DEFAULT_THIRST
    }
}