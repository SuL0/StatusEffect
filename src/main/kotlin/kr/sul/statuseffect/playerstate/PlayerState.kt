package kr.sul.statuseffect.playerstate

import org.bukkit.Bukkit
import org.bukkit.entity.Player

class PlayerState(val p: Player) {
    var isBleeding: Boolean = DEFAULT_ISBLEEDING
        set(value) {
            field = value
            Bukkit.getPluginManager().callEvent(PlayerStateChangedEvent(p))
        }
    var thirst: Double = DEFAULT_THIRST
        set(value) {
            field = value
            Bukkit.getPluginManager().callEvent(PlayerStateChangedEvent(p))
        }

    companion object {
        const val DEFAULT_ISBLEEDING = false
        const val DEFAULT_THIRST = 100.toDouble()
    }
}