package kr.sul.statuseffect.state

import kr.sul.servercore.util.ClassifyWorlds
import kr.sul.statuseffect.StatusEffect.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.World
import org.bukkit.entity.Player

object StateManager {
    private val ACTIVE_WORLD = arrayListOf<World>()
    init {
        Bukkit.getScheduler().runTaskLater(plugin, {
            ACTIVE_WORLD.addAll(ClassifyWorlds.beachTownWorlds)
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