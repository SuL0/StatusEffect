package kr.sul.statuseffect.state.thirst

import kr.sul.servercore.file.SimplyLog
import kr.sul.statuseffect.StatusEffect.Companion.plugin
import kr.sul.statuseffect.playerstate.PlayerState
import kr.sul.statuseffect.playerstate.PlayerStateManager.getPlayerState
import kr.sul.statuseffect.state.StateManager
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionType

object EventRelatedToThirstListener: Listener {
    
    // ActiveWorld 가 아닌 곳으로 이동할 시, 갈증 초기화
    @EventHandler
    fun onMoveWorld(e: PlayerChangedWorldEvent) {
        if (StateManager.isInActiveWorld(e.player.world)) return
        getPlayerState(e.player).thirst = PlayerState.DEFAULT_THIRST
    }

    // 물을 마실 시, 갈증 초기화
    @EventHandler(priority = EventPriority.LOW)
    fun onConsumeWater(e: PlayerItemConsumeEvent) {
        if (e.isCancelled) return
        if (e.item.type == Material.POTION) {
            val potionMeta = e.item?.itemMeta as PotionMeta

            if (potionMeta.basePotionData.type == PotionType.WATER) {
                e.isCancelled = true

                if (e.player.inventory.itemInMainHand.type == e.item.type) {
                    if (e.player.inventory.itemInMainHand.amount == 1) {
                        e.player.inventory.itemInMainHand = null
                    } else {
                        e.player.inventory.itemInMainHand.amount -= 1
                    }
                    e.player.sendMessage("§c§lTHIRST: §f물을 섭취하였습니다.")
                    getPlayerState(e.player).thirst = PlayerState.DEFAULT_THIRST
                }

                // 손에 이벤트 대상인 WaterBottle 아이템이 없음 -> 유저가 버그 유발하려 할 때
                else {
                    SimplyLog.log(plugin, "손에 이벤트 대상인 WaterBottle 아이템이 없음",
                        "p: ${e.player.name}", "item: ${e.item.type} | ${e.item.itemMeta?.displayName}")
                }
            }
        }
    }

    // 죽을 시 상태 초기화는 필요없을 듯. onMoveWorld 가 있으니
}