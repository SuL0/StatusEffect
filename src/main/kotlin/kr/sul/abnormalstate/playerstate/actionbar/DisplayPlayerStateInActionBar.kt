package kr.sul.abnormalstate.playerstate.actionbar

import kr.sul.abnormalstate.AbnormalState.Companion.plugin
import kr.sul.abnormalstate.playerstate.PlayerState
import kr.sul.abnormalstate.playerstate.PlayerStateChangedEvent
import kr.sul.abnormalstate.playerstate.PlayerStateManager.getPlayerState
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.roundToInt

object DisplayPlayerStateInActionBar: Listener {
    init {
        registerActionbarScheduler()
    }
    private fun registerActionbarScheduler() {
        object: BukkitRunnable() {
            override fun run() {
                Bukkit.getServer().onlinePlayers.forEach { sendPlayerStateActionBar(it) }
            }
        }.runTaskTimer(plugin, 0L, 40L)
    }



    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        sendPlayerStateActionBar(e.player)
    }
    @EventHandler
    fun onPlayerStateChanged(e: PlayerStateChangedEvent) {
        sendPlayerStateActionBar(e.player)
    }

    private fun sendPlayerStateActionBar(p: Player) {
        val playerState = getPlayerState(p)
        val content = StringBuilder("")

        // 액션바 내용 채우기 //
        // 갈증
        val thirstProcessed = (playerState.thirst / PlayerState.DEFAULT_THIRST * 100 * 10).roundToInt() / 10.0
        content.append("§4[ §b갈증 §7- §f$thirstProcessed% §4] ")

        // 출혈
        if (playerState.isBleeding) {
            content.append("§4[ §c출혈중 §4] ")
        }

        p.sendActionBar(content.toString())
    }
}