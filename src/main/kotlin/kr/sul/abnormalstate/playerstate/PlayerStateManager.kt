package kr.sul.abnormalstate.playerstate

import org.bukkit.entity.Player
import org.bukkit.event.Listener

object PlayerStateManager: Listener {
    private val playerStateMap = hashMapOf<Player, PlayerState>()

    fun getPlayerState(p: Player): PlayerState {
        // PlayerState 가 이미 있다면 불러오기
        if (playerStateMap.containsKey(p)) {
            return playerStateMap[p]!!
        }

        // 없다면 새로 생성하기
        val playerState = PlayerState(p)
        playerStateMap[p] = playerState
        return playerState
    }
}