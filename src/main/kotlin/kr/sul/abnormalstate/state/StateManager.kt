package kr.sul.abnormalstate.state

import kr.sul.abnormalstate.AbnormalState.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.World
import org.bukkit.entity.Player

object StateManager {
    private val ACTIVE_WORLD = arrayListOf<World>()
    init {
        Bukkit.getScheduler().runTaskLater(plugin, {
            ACTIVE_WORLD.add(Bukkit.getWorld("BeachTown"))
        }, 1L) // 월드는 서버 켜지고, 1틱 뒤에 가져와짐
    }

    // GameMode 와 World 로 확인
    fun isValidTargetPlayer(p: Player): Boolean {
        return (p.gameMode == GameMode.SURVIVAL && isInActiveWorld(p.world))
    }
    fun isInActiveWorld(world: World): Boolean {
        return (ACTIVE_WORLD.contains(world))
    }
}