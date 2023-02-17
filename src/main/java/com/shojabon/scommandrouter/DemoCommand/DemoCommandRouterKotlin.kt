package com.shojabon.scommandrouter.DemoCommand

import com.shojabon.scommandrouter.SCommandRouter.SCommandArgumentType
import com.shojabon.scommandrouter.SCommandRouter.SCommandData
import com.shojabon.scommandrouter.SCommandRouter.SCommandObject
import com.shojabon.scommandrouter.SCommandRouter.SCommandRouter
import com.shojabon.scommandrouter.SCommandRouterMain
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class DemoCommandRouterKotlin(private val plugin: SCommandRouterMain) : SCommandRouter(plugin, "demo") {
    init {
        registerCommands()
        registerEvents()
    }

    fun registerEvents() {
        setNoPermissionEvent { e: SCommandData -> e.sender.sendMessage("§c§lYou do not have permissions") }
        setOnNoCommandFoundEvent { e: SCommandData -> e.sender.sendMessage("§c§lThe command doesn't exist") }
    }

    fun registerCommands() {

        addCommand(
                SCommandObject()
                        .prefix("test1")
                        .argument("player", SCommandArgumentType.ONLINE_PLAYER)
                        .argument("color", "blue", "red", "yellow")
                        .explanation("Command to give player a colored wool")
                        .permission("CommandRouter.color")

                        .inlineExecutor { e: SCommandData ->
                            val p = e.sender as Player
                            if (e.args[2].equals("blue")) p.inventory.addItem(ItemStack(Material.BLUE_WOOL))
                            if (e.args[2].equals("red")) p.inventory.addItem(ItemStack(Material.RED_WOOL))
                            if (e.args[2].equals("yellow")) p.inventory.addItem(ItemStack(Material.YELLOW_WOOL))
                            p.sendMessage("Free wool!")
                        }
        )


        addCommand(
                SCommandObject()
                        .prefix("test2")
                        .argument("offlinePlayer") { e: CommandSender? ->
                            //offline player name picker
                            val result = ArrayList<String?>()
                            for (p in Bukkit.getOfflinePlayers()) {
                                result.add(p.name)
                            }
                            result
                        }
                        .explanation("Display name of uuid")
                        .permission("CommandRouter.uuid.search")

                        .inlineExecutor { e: SCommandData ->
                            val player = Bukkit.getOfflinePlayer(e.args[1])
                            e.sender.sendMessage("uuid of " + e.args[1] + " is " + player.uniqueId)
                        }
        )
    }
}