package kr.sul.statuseffect.item.food

import kr.sul.servercore.file.SimplyLog
import kr.sul.statuseffect.StatusEffect.Companion.plugin
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent

object FoodImpl: Listener {

    @EventHandler(priority = EventPriority.LOW)
    fun onEatFood(e: PlayerItemConsumeEvent) {
        if (e.isCancelled) return

        if (e.item.type.isEdible) {
            val material = e.item.type
            val itemName = e.item.itemMeta?.displayName ?: return

            val find = AllFoods.find(material, itemName)

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
                    SimplyLog.log(plugin, "손에 이벤트 대상인 Food 아이템이 없음",
                        "p: ${e.player.name}", "item: ${e.item.type} | ${e.item.itemMeta?.displayName}")
                }
            }
        }

    }
}