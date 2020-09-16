package me.sul.abnormalstate.bleeding;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class Bleeding implements Listener {
    private static final int BLEEDING_PERIOD = 10; // tick
    private static final double MAX_BLEED_DAMAGE_PER_ONCE = 0.5; // tick
    private final Plugin plugin;

    private final Map<Player, BleedingRunnable> bleedingRunnableMap = new HashMap<>();

    public Bleeding(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player) || e.getCause() == EntityDamageEvent.DamageCause.CUSTOM || e.getFinalDamage() <= 0) return;
        Player victim = (Player) e.getEntity();
        addBleedScheduler(victim, BleedingUtil.calcTotalDamageOfBleeding(e.getDamage()));
    }

    private void addBleedScheduler(Player p, double bleedDamage) {
        if (bleedDamage < 0.5) return;

        if (isCurrentBleedingWorthThanOriginalBleeding(p, bleedDamage)) {
            // 기존 Runnable cancel
            if (bleedingRunnableMap.containsKey(p)) {
                bleedingRunnableMap.get(p).cancel();
            }

            BleedingRunnable bleedingRunnable = new BleedingRunnable(bleedDamage) {
                @Override
                public void run() {
                    if (remainDamage <= 0 || p.isDead()) {
                        bleedingRunnableMap.remove(p);
                        cancel(); return;
                    }

                    double damage = Math.min(remainDamage, MAX_BLEED_DAMAGE_PER_ONCE);
                    remainDamage -= damage;
                    BleedingUtil.causeBleeding(p, damage);
                }
            };
            bleedingRunnableMap.put(p, bleedingRunnable);
            bleedingRunnable.runTaskTimer(plugin, BLEEDING_PERIOD, BLEEDING_PERIOD);
        }
    }

    // 기존에 실행되고 있던 Runnable과 현재 Runnable 중 무엇이 더 가치있는지 비교
    private boolean isCurrentBleedingWorthThanOriginalBleeding(Player p, double currentBleedDamage) {
        if (!bleedingRunnableMap.containsKey(p)) return true;
        double originalBleedDamage = bleedingRunnableMap.get(p).remainDamage;
        return (originalBleedDamage < currentBleedDamage);
    }
}
