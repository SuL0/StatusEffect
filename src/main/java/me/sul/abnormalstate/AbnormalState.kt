package me.sul.abnormalstate

import me.sul.abnormalstate.bleeding.Bleeding
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class AbnormalState : JavaPlugin() {
    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(Bleeding(this), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}