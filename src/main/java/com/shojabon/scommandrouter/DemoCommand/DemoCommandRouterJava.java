package com.shojabon.scommandrouter.DemoCommand;

import com.shojabon.scommandrouter.SCommandRouter.SCommandArgumentType;
import com.shojabon.scommandrouter.SCommandRouter.SCommandObject;
import com.shojabon.scommandrouter.SCommandRouter.SCommandRouter;
import com.shojabon.scommandrouter.SCommandRouterMain;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class DemoCommandRouterJava extends SCommandRouter {

    SCommandRouterMain plugin;

    public DemoCommandRouterJava(SCommandRouterMain plugin){
        super(plugin, "demo");
        this.plugin = plugin;
        registerCommands();
        registerEvents();
    }

    public void registerEvents(){
        setNoPermissionEvent(e -> e.sender.sendMessage("§c§lYou do not have permissions"));
        setOnNoCommandFoundEvent(e -> e.sender.sendMessage("§c§lThe command doesn't exist"));
    }

    public void registerCommands(){

        addCommand(
                new SCommandObject()
                        .prefix("test1")
                        .argument("player", SCommandArgumentType.ONLINE_PLAYER)
                        .argument("color", "blue", "red", "yellow")
                        .explanation("Command to give player a colored wool")
                        .permission("CommandRouter.color")
                        .inlineExecutor(e -> {
                            Player p = (Player)e.sender;
                            if(e.args[2].equalsIgnoreCase("blue")) p.getInventory().addItem(new ItemStack(Material.BLUE_WOOL));
                            if(e.args[2].equalsIgnoreCase("red")) p.getInventory().addItem(new ItemStack(Material.RED_WOOL));
                            if(e.args[2].equalsIgnoreCase("yellow")) p.getInventory().addItem(new ItemStack(Material.YELLOW_WOOL));
                            p.sendMessage("Free wool!");
                        })
        );

        addCommand(
                new SCommandObject()
                        .prefix("test2")
                        .argument("offlinePlayer", e -> {
                            //offline player name picker
                            ArrayList<String> result = new ArrayList<>();
                            for(OfflinePlayer p : Bukkit.getOfflinePlayers()){
                                result.add(p.getName());
                            }
                            return result;
                        })
                        .explanation("Display name of uuid")
                        .permission("CommandRouter.uuid.search")
                        .inlineExecutor(e -> {
                            OfflinePlayer player = Bukkit.getOfflinePlayer(e.args[1]);
                            e.sender.sendMessage("uuid of " + e.args[1] + " is " + player.getUniqueId());
                        })
        );

    }
}
