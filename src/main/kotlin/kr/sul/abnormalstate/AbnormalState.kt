package kr.sul.abnormalstate

import kr.sul.abnormalstate.bleeding.Bleeding
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.JavaPluginLoader
import java.io.File

class AbnormalState : JavaPlugin {
    constructor() : super() {}
    constructor(loader: JavaPluginLoader?, description: PluginDescriptionFile?, dataFolder: File?, file: File?) : super(loader, description, dataFolder, file) {}

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