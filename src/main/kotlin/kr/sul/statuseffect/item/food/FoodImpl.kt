package kr.sul.statuseffect.item.food

import kr.sul.servercore.file.simplylog.LogLevel
import kr.sul.servercore.file.simplylog.SimplyLog
import kr.sul.statuseffect.StatusEffect.Companion.plugin
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerItemConsumeEvent


// FIXME: e.isCancelled 때문에, 음식을 겹친 상태에서 연달아서 먹으면 안 먹어지는 사소한 버그가 있음
object FoodImpl: Listener {
    @EventHandler
    fun onTest(e: PlayerCommandPreprocessEvent) {
        if (e.message == "/hungry" && e.player.isOp) {
            e.isCancelled = true
            e.player.health = 2.0
            e.player.foodLevel = 2
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onEatFood(e: PlayerItemConsumeEvent) {
        if (e.isCancelled) return

        if (e.item.type.isEdible) {
            val material = e.item.type
            val itemName = e.item.itemMeta?.displayName ?: return

            val find = FoodType.find(material, itemName)

            if (find != null) {
                e.isCancelled = true

                if (e.player.inventory.itemInMainHand.type == e.item.type) {
                    if (e.player.inventory.itemInMainHand.amount == 1) {
                        e.player.inventory.itemInMainHand = null
                    } else {
                        e.player.inventory.itemInMainHand.amount -= 1
                    }
                    e.player.health += (find.addHealth*2).toInt()  // 1이 반 칸
                    e.player.foodLevel += (find.addFoodLevel*2).toInt()  // 1이 반 칸
                }

                // 손에 이벤트 대상인 Food 아이템이 없음 -> 유저가 버그 유발하려 할 때
               else {
                    SimplyLog.log(LogLevel.ERROR_LOW, plugin, "손에 이벤트 대상인 Food 아이템이 없음",
                        "p: ${e.player.name}", "item: ${e.item.type} | ${e.item.itemMeta?.displayName}")
                }
            }
        }
    }

}