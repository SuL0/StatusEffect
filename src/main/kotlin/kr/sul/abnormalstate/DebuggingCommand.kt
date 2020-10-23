package kr.sul.abnormalstate

import kr.sul.abnormalstate.playerstate.PlayerStateManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object DebuggingCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) return false
        if (args.isEmpty()) {
            sender.sendMessage("I don't know what you want")
            return false
        }

        val p = sender as Player

        when(args[0].toLowerCase()) {
            "PlayerState".toLowerCase() -> {
                val playerState = PlayerStateManager.getPlayerState(p)
                p.sendMessage("isBleeding: ${playerState.isBleeding}")
                p.sendMessage("thirst: ${playerState.thirst}")
            }
            else -> {
                p.sendMessage("I don't know what you want")
                return false
            }
        }
        return true
    }
}