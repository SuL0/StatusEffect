package me.sul.abnormalstate.bleeding

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.material.MaterialData
import java.util.stream.Collectors

object BleedingUtil {
    fun calcTotalDamageOfBleeding(damagedHealth: Double): Double {
        return Math.log10(damagedHealth) * 1.5 + 0.05
    }

    fun causeBleeding(p: Player, damage: Double) {
        p.damage(damage)
        spawnBloodParticle(p)
    }

    fun spawnBloodParticle(p: Player) {
        val particleLoc = p.location
        val nearbyPlayers = Bukkit.getServer().onlinePlayers.stream()
                .filter { loopP: Player ->
                    loopP.world == p.world &&
                            loopP.location.distance(p.location) <= 50
                }
                .collect(Collectors.toList())
        p.world.spawnParticle(Particle.BLOCK_CRACK, nearbyPlayers, p,
                particleLoc.x, particleLoc.y, particleLoc.z,
                20, 0.3, 0.3, 0.3, 1.0,
                MaterialData(Material.REDSTONE_BLOCK), true) // 1.15버전은 new MaterialData(...) -> e.getBlock.getType() 만 해도됨
    }
}