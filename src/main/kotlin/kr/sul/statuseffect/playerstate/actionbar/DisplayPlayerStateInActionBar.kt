package kr.sul.statuseffect.playerstate.actionbar

import kr.sul.statuseffect.StatusEffect.Companion.plugin
import kr.sul.statuseffect.playerstate.PlayerState
import kr.sul.statuseffect.playerstate.PlayerStateChangedEvent
import kr.sul.statuseffect.playerstate.PlayerStateManager.getPlayerState
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.roundToInt

/*
object DisplayPlayerStateInActionBar: Listener {
    init {
        registerActionbarScheduler()
    }

    private const val WATER_ICON = "§f訄" // §f 없으면 색 이상해짐
    private const val BLOOD_ICON = "§f訅"
    private const val INFECT_ICON = "§f訆"



    // TODO 출혈은 UI(?) 감염은 삭제 갈증은 아이템 9번칸
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
        /*
        val playerState = getPlayerState(p)
        val content = StringBuilder("")

        // 액션바 내용 채우기 //
        // 갈증
        val thirstProcessed = (playerState.thirst / PlayerState.DEFAULT_THIRST * 100 * 10).roundToInt() / 10.0
        if (thirstProcessed <= 20) {
            content.append("$WATER_ICON§c$thirstProcessed% §4(갈증)  ")
        } else {
            content.append("$WATER_ICON§f$thirstProcessed% §2(갈증)  ")
        }

        // 출혈
        if (playerState.isBleeding) {
            content.append("$BLOOD_ICON§c출혈 §4(출혈)  ")
        } else {
            content.append("$BLOOD_ICON§a정상 §2(출혈)  ")
        }

        // 감염
        content.append("$INFECT_ICON§a정상 §2(감염)")

        val blank = StringBuilder("")
        for (i in 0 until (110-content.length)) {
            blank.append(" ")
        }

        p.sendActionBar(content.toString())*/
    }
}*/