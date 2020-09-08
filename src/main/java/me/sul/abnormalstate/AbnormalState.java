package me.sul.abnormalstate;

import me.sul.abnormalstate.bleed.Bleed;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AbnormalState extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new Bleed(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
