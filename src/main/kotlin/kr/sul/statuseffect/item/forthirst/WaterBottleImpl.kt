package kr.sul.statuseffect.item.forthirst

import kr.sul.servercore.file.simplylog.LogLevel
import kr.sul.servercore.file.simplylog.SimplyLog
import kr.sul.statuseffect.StatusEffect.Companion.plugin
import kr.sul.statuseffect.playerstate.PlayerState
import kr.sul.statuseffect.playerstate.PlayerStateManager
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionType

object WaterBottleImpl: Listener {

    // 물을 마실 시, 갈증 초기화
    @EventHandler(priority = EventPriority.LOW)
    fun onConsumeWater(e: PlayerItemConsumeEvent) {
        if (e.isCancelled) return
        if (e.item.type == Material.POTION) {
            val potionMeta = e.item?.itemMeta as PotionMeta

            if (potionMeta.basePotionData.type == PotionType.WATER) {
                e.isCancelled = true
                val p = e.player

                if (p.inventory.itemInMainHand.type == e.item.type) {
                    if (p.inventory.itemInMainHand.amount == 1) {
                        p.inventory.itemInMainHand = null
                    } else {
                        p.inventory.itemInMainHand.amount -= 1
                    }
                    p.sendMessage("§c§lTHI: §f물을 섭취하였습니다.")
                    PlayerStateManager.getPlayerState(p).thirst = PlayerState.DEFAULT_THIRST
                }

                // 손에 이벤트 대상인 WaterBottle 아이템이 없음 -> 유저가 버그 유발하려 할 때
                else {
                    SimplyLog.log(LogLevel.ERROR_LOW, plugin, "손에 이벤트 대상인 WaterBottle 아이템이 없음",
                        "p: ${p.name}", "item: ${e.item.type} | ${e.item.itemMeta?.displayName}")
                }
            }
        }
    }
}