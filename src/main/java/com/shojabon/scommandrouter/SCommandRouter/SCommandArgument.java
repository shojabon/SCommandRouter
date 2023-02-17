package com.shojabon.scommandrouter.SCommandRouter;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class SCommandArgument {
    public String alias = null;
    private ArrayList<String> allowedStrings = new ArrayList<>();

    public ArrayList<String> explanations = new ArrayList<>();

    private ArrayList<String> deprecated = new ArrayList<>();

    private Function<CommandSender, ArrayList<String>> allowedStringsFunction = null;
    private Function<String, Boolean> argumentParser = null;

    public SCommandObject rootCommandObject;


    public SCommandArgument explanation(String... text){
        explanations.addAll(List.of(text));
        return this;
    }
    public SCommandArgument deprecated(String... text){
        deprecated.addAll(List.of(text));
        return this;
    }

    public SCommandArgument allowedString(String... string){
        allowedStrings.addAll(List.of(string));
        return this;
    }

    public SCommandArgument allowedStringsFunction(Function<CommandSender, ArrayList<String>> function){
        this.allowedStringsFunction = function;
        return this;
    }

    public SCommandArgument argumentParser(Function<String, Boolean> function){
        this.argumentParser = function;
        return this;
    }

    public SCommandArgument alias(String alias){
        this.alias = alias;
        return this;
    }

    public ArrayList<String> getAllowedStrings(CommandSender sender){
        ArrayList<String> results = new ArrayList<>(allowedStrings);
        if(allowedStringsFunction != null){
            try{
                results.addAll(allowedStringsFunction.apply(sender));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return results;
    }

    public boolean matches(String arg, CommandSender sender){
        //test allowed strings
        ArrayList<String> allowedStrings = getAllowedStrings(sender);
        if(allowedStrings.size() == 0 && argumentParser == null) return true;
        for(String testingAllowedString: allowedStrings){
            if(testingAllowedString.equalsIgnoreCase(arg)) return true;
        }
        if(argumentParser != null){
            try{
                boolean result = argumentParser.apply(arg);
                if(!result) return false;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }


}
