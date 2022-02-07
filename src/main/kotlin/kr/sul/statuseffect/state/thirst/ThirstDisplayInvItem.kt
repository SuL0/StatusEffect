package kr.sul.statuseffect.state.thirst

import kr.sul.servercore.extensionfunction.UpdateInventorySlot.updateInventorySlot
import kr.sul.servercore.util.ItemBuilder.durabilityIB
import kr.sul.servercore.util.ItemBuilder.flagIB
import kr.sul.servercore.util.ItemBuilder.unbreakableIB
import kr.sul.statuseffect.playerstate.PlayerState
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import kotlin.math.ceil

object ThirstDisplayInvItem: Listener {
    // 플레이어 9번 슬롯
    private val thirstItemList = arrayListOf<ItemStack>().run {
        val item = ItemStack(Material.FLINT_AND_STEEL)
            .flagIB(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE).unbreakableIB(true)
        for (i in 11 downTo 1) {
            add(item.clone().durabilityIB(i))
        }
        this
    }

    fun updateThirstInvItem(playerState: PlayerState) {
        val ratio = ceil(playerState.thirst / PlayerState.DEFAULT_THIRST * 10).toInt() // 0~10
        val p = playerState.p
        if (thirstItemList[ratio] != p.inventory.getItem(8)) {
            p.inventory.setItem(8, thirstItemList[ratio])
//            p.updateInventorySlot(8)
        }
        // ServerCore의 updateInventory를 해 줘야하나?
    }


    @EventHandler(priority = EventPriority.LOW)
    fun onInvClick(e: InventoryClickEvent) {
        if (e.isCancelled) return
        if (e.clickedInventory is PlayerInventory && e.slot == 8) {
            e.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onDrop(e: PlayerDropItemEvent) {
        if (e.isCancelled) return
        if (e.itemDrop.itemStack.isThirstItem()) {
            e.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onSwap(e: PlayerSwapHandItemsEvent) {
        if (e.isCancelled) return
        if (e.offHandItem.isThirstItem()) {
            e.isCancelled = true
        }
    }

    // 현재로서는 갈증이 다 닳은 아이템이 durability 로는 제일 높은 수치를 가진다.
    private fun ItemStack.isThirstItem(): Boolean {
        return this.type == thirstItemList.first().type
                && this.durability >= thirstItemList.last().durability && this.durability <= thirstItemList.first().durability
    }
}