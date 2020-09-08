package me.sul.abnormalstate.bleed;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class Bleed implements Listener {
    private static final int BLEED_PERIOD = 6; // tick
    private static final double MAX_BLEED_DAMAGE_PER_ONCE = 0.5; // tick
    private final Plugin plugin;

    private final Map<Player, BleedRunnable> bleedRunnableMap = new HashMap<>();

    public Bleed(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player) || e.getCause() == EntityDamageEvent.DamageCause.CUSTOM) return;
        Player victim = (Player) e.getEntity();
        addBleedScheduler(victim, BleedUtil.calcTotalDamageOfBleeding(e.getDamage()));
    }

    private void addBleedScheduler(Player p, double bleedDamage) {
        if (bleedDamage < 0.5) return;

        if (isCurrentBleedWorthThanOriginalBleed(p, bleedDamage)) {
            // 기존 Runnable cancel
            if (bleedRunnableMap.containsKey(p)) {
                bleedRunnableMap.get(p).cancel();
            }

            BleedRunnable bleedRunnable = new BleedRunnable(bleedDamage) {
                @Override
                public void run() {
                    if (remainDamage <= 0) {
                        bleedRunnableMap.remove(p);
                        cancel(); // NOTE: 밑에 코드 실행하고 cancel? 아니면 바로 cancel?
                        return;
                    }

                    double damage = Math.min(remainDamage, MAX_BLEED_DAMAGE_PER_ONCE);
                    remainDamage -= damage;
                    p.damage(damage);
                }
            };
            bleedRunnableMap.put(p, bleedRunnable);
            bleedRunnable.runTaskTimer(plugin, BLEED_PERIOD, BLEED_PERIOD);
        }
    }

    // 기존에 실행되고 있던 Runnable과 현재 Runnable 중 무엇이 더 가치있는지 비교
    private boolean isCurrentBleedWorthThanOriginalBleed(Player p, double currentBleedDamage) {
        if (!bleedRunnableMap.containsKey(p)) return true;
        double originalBleedDamage = bleedRunnableMap.get(p).remainDamage;
        return (originalBleedDamage < currentBleedDamage);
    }


}
