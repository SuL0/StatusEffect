package kr.sul.statuseffect.state.thirst

import kr.sul.statuseffect.StatusEffect.Companion.plugin
import kr.sul.statuseffect.playerstate.PlayerState
import kr.sul.statuseffect.playerstate.PlayerStateManager.getPlayerState
import kr.sul.statuseffect.state.StateManager
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.ItemStack

object EventRelatedToThirstListener: Listener {
    
    // ActiveWorld 가 아닌 곳으로 이동할 시, 갈증 초기화
    @EventHandler
    fun onMoveWorld(e: PlayerChangedWorldEvent) {
        if (StateManager.isInActiveWorld(e.player.world)) return
        getPlayerState(e.player).thirst = PlayerState.DEFAULT_THIRST
    }

    // 물을 마실 시, 갈증 초기화
    @EventHandler
    fun onConsume(e: PlayerItemConsumeEvent) {
        if (e.item.type == Material.POTION) {
            e.player.sendMessage("§c§lTHIRST: §f물을 섭취하였습니다.")
            getPlayerState(e.player).thirst = PlayerState.DEFAULT_THIRST

            // 물병 회수
            Bukkit.getServer().scheduler.scheduleSyncDelayedTask(plugin, {
                e.player.inventory.removeItem(ItemStack(Material.GLASS_BOTTLE))
                if (e.player.inventory.itemInOffHand.type == Material.GLASS_BOTTLE) {
                    e.player.inventory.itemInOffHand = null
                }
            }, 1L)
        }
    }

    // 죽을 시 초기화는 필요없을 듯. onMoveWorld 가 있으니
}