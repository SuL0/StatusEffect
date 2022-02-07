package kr.sul.statuseffect.state.thirst

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import kr.sul.servercore.util.EntityTempDataMap
import kr.sul.statuseffect.StatusEffect.Companion.plugin
import kr.sul.statuseffect.playerstate.PlayerState
import kr.sul.statuseffect.playerstate.PlayerStateManager.getPlayerState
import kr.sul.statuseffect.state.StateManager
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.roundToInt

object Thirst: Listener {
    private const val THIRST_DECREASE_PERIOD = 20L
    private val previousLocationMap = EntityTempDataMap.create<Location>(plugin)
    init {
        registerThirstScheduler()
    }

    // ActiveWorld 가 아닌 곳으로 이동할 시, 갈증 초기화
    // 죽을 시 상태 초기화는 필요없을 듯. onMoveWorld 가 있으니
    @EventHandler
    fun onMoveWorld(e: PlayerChangedWorldEvent) {
        if (StateManager.isInActiveWorld(e.player.world)) return
        getPlayerState(e.player).thirst = PlayerState.DEFAULT_THIRST
    }

    // 점프할 때는 따로 onJump 하면 cnt = 3 하고 cnt -- 으로 까면서 갈증 제거
    private fun registerThirstScheduler() {
        object : BukkitRunnable() {
            override fun run() {
                for (p in Bukkit.getServer().onlinePlayers
                                .filter { StateManager.isValidTargetPlayer(it) }) {
                    val previousStateByThirst = getPlayerState(p).stateByThirst
                    // thirst 의 setter에 의해 값이 음수로 내려갈 일은 없음
                    if (getPlayerState(p).thirst > 0) {
                        if (!previousLocationMap.contains(p)
                            || !previousLocationMap[p]!!.isMoved(p.location)) {
                            getPlayerState(p).thirst -= 0.833
                        } else if (p.isSprinting) {
                            getPlayerState(p).thirst -= 5.55
                        } else {
                            getPlayerState(p).thirst -= 1.66
                        }
                        previousLocationMap[p] = p.location
                    }
                    val thirst = getPlayerState(p).thirst.roundToInt()
                }
            }
        }.runTaskTimer(plugin, THIRST_DECREASE_PERIOD, THIRST_DECREASE_PERIOD)
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onJump(e: PlayerJumpEvent) {
        if (e.isCancelled) return
        if (!StateManager.isValidTargetPlayer(e.player)) return
        getPlayerState(e.player).thirst -= 5.55

    }


    // 갈증에 의거한 상태에 따른 받는 데미지 증폭
    @EventHandler(priority = EventPriority.LOWEST)
    fun onDamage(e: EntityDamageEvent) {
        if (e.isCancelled || e.entity !is Player) return
        if (!StateManager.isValidTargetPlayer(e.entity as Player)) return
        when (getPlayerState(e.entity as Player).stateByThirst) {
            PlayerState.StateByThirst.VULNERABLE -> {
                e.damage = e.damage*1.5
            }
            PlayerState.StateByThirst.MORIBUND -> {
                e.damage = e.damage*10
            }
            else -> {}
        }
    }
    @EventHandler(priority = EventPriority.MONITOR)
    fun onDamage2(e: EntityDamageEvent) {
        if (e.isCancelled || e.entity !is Player) return
        if (!StateManager.isValidTargetPlayer(e.entity as Player)) return
        getPlayerState(e.entity as Player).thirst -= 25 * e.damage  // 하트 한 칸 = 2.0
    }

    // 러프하게 계산
    fun Location.isMoved(loc: Location): Boolean {
        return (this.x != loc.x || this.y != loc.y || this.z != loc.z)
    }
}