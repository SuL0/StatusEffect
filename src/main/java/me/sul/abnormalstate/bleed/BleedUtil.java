package me.sul.abnormalstate.bleed;

public class BleedUtil {
    public static double calcTotalDamageOfBleeding(double damagedHealth) {
        return Math.log(damagedHealth) * 1.5 + 0.05;
    }
}
