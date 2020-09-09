package me.sul.abnormalstate.bleeding;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import java.util.List;
import java.util.stream.Collectors;

public class BleedingUtil {
    public static double calcTotalDamageOfBleeding(double damagedHealth) {
        return Math.log10(damagedHealth) * 1.5 + 0.05;
    }
    public static void causeBleeding(Player p, double damage) {
        p.damage(damage);
        spawnBloodParticle(p);
    }

    public static void spawnBloodParticle(Player p) {
        Location particleLoc = p.getLocation();

        List<Player> nearbyPlayers = Bukkit.getServer().getOnlinePlayers().stream()
                .filter(loopP -> loopP.getWorld().equals(p.getWorld()) &&
                        loopP.getLocation().distance(p.getLocation()) <= 50)
                .collect(Collectors.toList());

        p.getWorld().spawnParticle(Particle.BLOCK_CRACK, nearbyPlayers, p,
                particleLoc.getX(), particleLoc.getY(), particleLoc.getZ(),
                20, 0.3, 0.3, 0.3, 1,
                new MaterialData(Material.REDSTONE_BLOCK), true);  // 1.15버전은 new MaterialData(...) -> e.getBlock.getType() 만 해도됨
    }

}
