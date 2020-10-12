package kr.sul.abnormalstate.playerstate

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PlayerStateChangedEvent(val player: Player): Event() {

    override fun getHandlers(): HandlerList {
        return handlerList
    }
    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}