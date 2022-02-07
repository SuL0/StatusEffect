package kr.sul.statuseffect.playerstate

import kr.sul.servercore.util.MsgPrefix
import kr.sul.statuseffect.state.thirst.ThirstDisplayInvItem
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
            if (value <= StateByThirst.VULNERABLE.thirstLessOrEqualThan && field > StateByThirst.VULNERABLE.thirstLessOrEqualThan) {
                p.sendMessage("")
                p.sendMessage("${MsgPrefix.get("THI")}몸의 수분량이 70% 이하로 신체는 몸을 지키기 위해 취약상태로 전환됩니다.")
                p.sendMessage("${MsgPrefix.get("THI")}취약 상태 시에는 입는 피해량이 더 강하게 들어옵니다.")
            } else if (value <= StateByThirst.MORIBUND.thirstLessOrEqualThan.toDouble() && field > StateByThirst.MORIBUND.thirstLessOrEqualThan) {
                p.sendMessage("")
                p.sendMessage("${MsgPrefix.get("THI")}몸의 수분량이 60% 이하로 온 몸의 장기들이 신체를 지키기 위하여 빈사상태에 돌입합니다.")
                p.sendMessage("${MsgPrefix.get("THI")}빈사 상태 시에는 입는 피해량이 매우 강하게 들어옵니다.")
            }
            field = if (value >= 0) {
                value
            } else {
                0.0
            }
            ThirstDisplayInvItem.updateThirstInvItem(this)
            Bukkit.getPluginManager().callEvent(PlayerStateChangedEvent(p))
        }
    val stateByThirst: StateByThirst
        get() {
            return if (thirst > StateByThirst.VULNERABLE.thirstLessOrEqualThan) {
                StateByThirst.NORMAL
            } else if (thirst > StateByThirst.MORIBUND.thirstLessOrEqualThan) {
                StateByThirst.VULNERABLE
            } else {
                StateByThirst.MORIBUND
            }
        }


    enum class StateByThirst(val thirstLessOrEqualThan: Int) {
        NORMAL(1000),
        VULNERABLE(300),
        MORIBUND(0)
    }
    companion object {
        const val DEFAULT_ISBLEEDING = false
        const val DEFAULT_THIRST = 1000.toDouble()
    }
}