package kr.sul.statuseffect.item.forbleeding

import kr.sul.servercore.util.GetAimedPlayer
import kr.sul.statuseffect.StatusEffect.Companion.plugin
import kr.sul.statuseffect.playerstate.PlayerState
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scheduler.BukkitRunnable

object BandageImpl: Listener {
    object BandageItem {
        private val material = Material.NETHER_STALK
        private const val itemName = "§e붕대"
        private val lore = arrayListOf("§7사용법", " §f출혈 시 붕대를 들고 우클릭 해주세요.  ", "§7감아주기", " §f대상을 바라보고 F키")

        fun isBandage(toCheck: ItemStack): Boolean {
            return (toCheck.type == material && toCheck.itemMeta?.displayName == itemName)
        }

        // TODO: BandageItem 뽑는 function 만들기. 특수문자 한자 알아내야 함
//        fun getItem(): ItemStack {
//        }
    }
    private const val IS_BANDAGING_KEY = "statusEffect.isBandaging"
    private const val TIME_FOR_USE_ITEM = (5)*20



    @EventHandler(priority = EventPriority.LOWEST)  // Interaction 이란 현상에 대해 관계 없이(따라서 isCancelled check X), 키 입력만 받아오고 싶다는 것
    fun onRightClickAtEntity(e: PlayerInteractAtEntityEvent) {
        if (BandageItem.isBandage(e.player.inventory.itemInMainHand)) {
            startBandaging(e.player, e.player)
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onRightClick(e: PlayerInteractEvent) {
        if (BandageItem.isBandage(e.player.inventory.itemInMainHand)) {
            startBandaging(e.player, e.player)
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPressF(e: PlayerSwapHandItemsEvent) {
        if (BandageItem.isBandage(e.player.inventory.itemInMainHand)) {
            val p = e.player
            val target = GetAimedPlayer.get(e.player, 3.0)

            if (target == null) {
                p.sendMessage("§c§lBLE: §7대상이 3칸 안에 존재해야 합니다.")
            } else {
                e.isCancelled = true
                startBandaging(p, target)
            }
        }
    }



    private fun startBandaging(p: Player, target: Player) {
        // Bandaging 할 수 있는지, 조건 체크
        if (p == target) {
            if (isBandaging(p)) {
                p.sendMessage("§c§lBLE: §7이미 붕대를 감는 중입니다.")
                return
            }
            if (!PlayerState(p).isBleeding) {
                p.sendMessage("§c§lBLE: §7현재 출혈 상태가 아닙니다.")
                return
            }
        }
        if (p != target) {
            if (isBandaging(target)) {
                p.sendMessage("§c§lBLE: §7상대방은 이미 붕대를 감는 중입니다.")
                return
            }
            if (!PlayerState(target).isBleeding) {
                p.sendMessage("§c§lBLE: §7대상이 출혈 상태가 아닙니다.")
                return
            }
        }


        // Bandaging 시작
        if (p == target) {
            setBandaging(p, true)
            p.sendTitle("", "§7붕대를 감는 중.", 10, 21, 0)
        } else {
            setBandaging(p, true)
            setBandaging(target, true)
            p.sendTitle("", "§e${target.name}§f에게 붕대를 감아주는 중.", 10, 21, 0)
            target.sendTitle("", "§e${p.name}§f이 붕대를 감아주는 중.", 10, 21, 0)
        }

        val taskTimerPeriod = 5
        object: BukkitRunnable() {
            var passedTick = 0
            override fun run() {
                passedTick += taskTimerPeriod

                // 붕대감기 취소
                if (!BandageItem.isBandage(p.inventory.itemInMainHand) || (p != target && GetAimedPlayer.get(p, 3.0) != target)
                        || p.isDead || target.isDead) {  // isDead = !isOnline
                    setBandaging(p, false)
                    p.sendTitle("", "§c붕대 사용이 취소되었습니다.", 10, 20, 10)
                    if (p != target) {
                        setBandaging(target, false)
                        target.sendTitle("", "§c상대방이 붕대 사용을 취소하였습니다.", 10, 20, 10)
                    }
                    cancel()
                    return
                }

                // 사용 완료 시
                else if (passedTick >= TIME_FOR_USE_ITEM) {
                    if (BandageItem.isBandage(p.inventory.itemInMainHand)) {
                        if (p.inventory.itemInMainHand.amount == 1) {
                            p.inventory.itemInMainHand = null
                        } else {
                            p.inventory.itemInMainHand.amount -= 1
                        }
                        if (p == target) {
                            setBandaging(p, false)
                            p.sendTitle("", "§f붕대를 사용하여, §c출혈§f이 멎었습니다.", 10, 20, 10)
                        } else {
                            setBandaging(p, false)
                            setBandaging(target, false)
                            p.sendTitle("", "§e${target.name}§f에게 붕대를 사용하였습니다.", 10, 20, 10)
                            target.sendTitle("", "§e${p.name}§f로부터 붕대를 사용받아, §c출혈§f이 멎었습니다.", 10, 20, 20)
                        }
                    }
                    cancel()
                    return
                }

                // sendTitle 업데이트
                else if (passedTick % 20 == 0) {
                    val dotStr = StringBuilder(".")
                    for (i in 1..passedTick/20) {
                       dotStr.append(".")
                    }

                    if (p == target) {
                        p.sendTitle("", "§7붕대를 감는 중$dotStr", 0, 21, 0)
                    } else {
                        p.sendTitle("", "§e${target.name}§f에게 붕대를 감아주는 중$dotStr", 0, 21, 0)
                        target.sendTitle("", "§e${p.name}§f이 붕대를 감아주는 중$dotStr", 0, 21, 0)
                    }
                }
            }
        }.runTaskTimer(plugin, taskTimerPeriod.toLong(), taskTimerPeriod.toLong())
    }



    private fun isBandaging(p: Player): Boolean {
        return p.hasMetadata(IS_BANDAGING_KEY)
    }
    private fun setBandaging(p: Player, bool: Boolean) {
        if (bool) {
            p.setMetadata(IS_BANDAGING_KEY, FixedMetadataValue(plugin, true))
        } else {
            p.removeMetadata(IS_BANDAGING_KEY, plugin)
        }
    }
}