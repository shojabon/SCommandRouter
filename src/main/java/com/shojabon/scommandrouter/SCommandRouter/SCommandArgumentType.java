package com.shojabon.scommandrouter.SCommandRouter;

import org.apache.logging.log4j.util.Base64Util;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public enum SCommandArgumentType {
    ONLINE_PLAYER(e -> {
        ArrayList<String> result = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers()){
            result.add(p.getName());
        }
        return result;
    }, null),

    WORLD(e -> {
        ArrayList<String> result = new ArrayList<>();
        for(World w : Bukkit.getWorlds()){
            result.add(w.getName());
        }
        return result;
    }, null),

    BOOLEAN(e -> {
        return new ArrayList<>(List.of("true", "false"));
    },
            string -> {
        try{
            Boolean.parseBoolean(string);
            return true;
        }catch (Exception e){
            return false;
        }
    }),

    INTEGER(null,
            checkingString -> {
        try{
            Integer.parseInt(checkingString);
            return true;
        }catch (Exception e){
            return false;
        }
    }),
    LONG(null,
            checkingString -> {
        try{
            Long.parseLong(checkingString);
            return true;
        }catch (Exception e){
            return false;
        }
    }),
    DOUBLE(null,
            checkingString -> {
                try{
                    Double.parseDouble(checkingString);
                    return true;
                }catch (Exception e){
                    return false;
                }
            });

    Function<CommandSender, ArrayList<String>> allowedString = null;
    Function<String, Boolean> argumentParser = null;
    SCommandArgumentType(Function<CommandSender, ArrayList<String>> allowedString, Function<String, Boolean> argumentParser){
        this.allowedString = allowedString;
        this.argumentParser = argumentParser;
    }
}
