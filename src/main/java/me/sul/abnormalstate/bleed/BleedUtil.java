package me.sul.abnormalstate.bleed;

import org.bukkit.Bukkit;

public class BleedUtil {
    public static double calcTotalDamageOfBleeding(double damagedHealth) {
        Bukkit.getServer().broadcastMessage("Math.log10(" + damagedHealth + ") * 1.5 + 0.05  §c= " + Math.log10(damagedHealth) * 1.5 + 0.05) ;
        return Math.log10(damagedHealth) * 1.5 + 0.05;
    }
}
