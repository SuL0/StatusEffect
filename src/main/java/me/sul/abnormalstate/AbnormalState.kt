package me.sul.abnormalstate

import me.sul.abnormalstate.bleeding.Bleeding
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class AbnormalState : JavaPlugin() {
    companion object {
        lateinit var instance : Plugin
    }

    override fun onEnable() {
        instance = this
        Bukkit.getPluginManager().registerEvents(Bleeding, this)
    }

    override fun onDisable() {
    }
}