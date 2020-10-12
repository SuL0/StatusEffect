package kr.sul.abnormalstate

import kr.sul.abnormalstate.playerstate.actionbar.DisplayPlayerStateInActionBar
import kr.sul.abnormalstate.state.bleeding.Bleeding
import kr.sul.abnormalstate.state.thirst.EventRelatedToThirstListener
import kr.sul.abnormalstate.state.thirst.Thirst
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
        lateinit var plugin : Plugin
    }

    override fun onEnable() {
        plugin = this
        registerClasses()
    }

    private fun registerClasses() {
        // PlayerState
        Bukkit.getPluginManager().registerEvents(DisplayPlayerStateInActionBar, this)
        // Bleeding
        Bukkit.getPluginManager().registerEvents(Bleeding, this)
        // Thirst
        Bukkit.getPluginManager().registerEvents(Thirst, this)
        Bukkit.getPluginManager().registerEvents(EventRelatedToThirstListener, this)
        // DebuggingCommand
        getCommand("abnormalstate").executor = DebuggingCommand
    }



    override fun onDisable() {
    }
}