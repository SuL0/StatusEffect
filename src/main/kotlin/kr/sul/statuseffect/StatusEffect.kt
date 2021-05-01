package kr.sul.statuseffect

import kr.sul.statuseffect.item.food.FoodImpl
import kr.sul.statuseffect.playerstate.actionbar.DisplayPlayerStateInActionBar
import kr.sul.statuseffect.state.bleeding.Bleeding
import kr.sul.statuseffect.state.thirst.EventRelatedToThirstListener
import kr.sul.statuseffect.state.thirst.Thirst
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.JavaPluginLoader
import java.io.File

class StatusEffect : JavaPlugin {
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

        // Item
        Bukkit.getPluginManager().registerEvents(FoodImpl, this)

        // DebuggingCommand
        getCommand("statuseffect").executor = DebuggingCommand
    }



    override fun onDisable() {
    }
}