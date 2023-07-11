package com.shojabon.scommandrouter.SCommandRouter;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class SCommandObject {

    public ArrayList<SCommandArgument> arguments = new ArrayList<>();
    public ArrayList<CommandExecutor> executors = new ArrayList<>();
    public ArrayList<Consumer<SCommandData>> inlineExecutors = new ArrayList<>();
    public boolean isInfinity = false;

    public String permission = null;

    ArrayList<String> explanation = new ArrayList<>();

    public SCommandObject infinity(){
        isInfinity = true;
        return this;
    }

    public SCommandObject explanation(String... explanation){
        this.explanation.addAll(List.of(explanation));
        return this;
    }

    public SCommandObject permission(@NotNull String permission){
        this.permission = permission;
        return this;
    }




    public SCommandObject argument(SCommandArgument arg){
        arguments.add(arg);
        return this;
    }

    public SCommandObject prefix(String prefix){
        arguments.add(new SCommandArgument()
                .allowedString(prefix));
        return this;
    }
    public SCommandObject argument(String alias){
        arguments.add(new SCommandArgument()
                .alias(alias));
        return this;
    }

    public SCommandObject argument(String alias, String... allowedStrings){
        arguments.add(new SCommandArgument()
                .alias(alias)
                .allowedString(allowedStrings));
        return this;
    }

    public SCommandObject argument(String alias, Function<CommandSender, ArrayList<String>> function, boolean strict){
        if(strict){
            arguments.add(new SCommandArgument()
                    .alias(alias)
                    .allowedStringsFunction(function));
        }else{
            arguments.add(new SCommandArgument()
                    .alias(alias)
                    .aliasStringsFunction(function));
        }
        return this;
    }

    public SCommandObject argument(String alias, Function<CommandSender, ArrayList<String>> function){
        this.argument(alias, function, true);
        return this;
    }

    public SCommandObject argument(String alias, SCommandArgumentType type){
        arguments.add(new SCommandArgument()
                .alias(alias)
                .allowedStringsFunction(type.allowedString)
                .argumentParser(type.argumentParser)
        );
        return this;
    }





    public SCommandObject executor(CommandExecutor event){
        executors.add(event);
        return this;
    }

    public SCommandObject inlineExecutor(Consumer<SCommandData> event){
        inlineExecutors.add(event);
        return this;
    }

    public boolean hasPermission(CommandSender sender){
        if(permission == null) return false;
        return sender.hasPermission(permission);
    }

    public boolean matches(String[] args, CommandSender sender){
        if(args.length < arguments.size()) return false;
        if(args.length > arguments.size() && !isInfinity) return false;
        for(int i = 0; i < args.length; i++){
            if(arguments.size()-1 <= i && isInfinity) continue;
            if(!arguments.get(i).matches(args[i], sender)){
                return false;
            }
        }
        return true;
    }

    public boolean validOption(String[] args, CommandSender sender){
        if(args.length > arguments.size()) return false;
        for(int i = 0; i < args.length-1; i++){
            if(!arguments.get(i).matches(args[i], sender)){
                return false;
            }
        }
        return true;
    }

    public void execute(SCommandData obj){
        for(CommandExecutor executor : executors){
            executor.onCommand(obj.sender, obj.command, obj.label, obj.args);
        }
        for(Consumer<SCommandData> event: inlineExecutors){
            event.accept(obj);
        }
    }

    public BaseComponent[] helpText(String baseCommand, String prefix, CommandSender sender){
        ComponentBuilder builder = new ComponentBuilder();

        StringBuilder commandExplanation = new StringBuilder();
        for(String exp : explanation){
            commandExplanation.append(exp).append("\n");
        }
        if(explanation.size() != 0) commandExplanation.append("\n");
        commandExplanation.append("§e==========Permission==========\n");
        if(permission != null){
            commandExplanation.append("§d").append(permission).append("\n");
        }else{
            commandExplanation.append("§d").append("なし").append("\n");
        }

        builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(commandExplanation.toString())));
        builder.append(prefix + "/" + baseCommand + " ");


        for(SCommandArgument arg : arguments){
            ArrayList<String> allowedStrings = arg.getAllowedStrings(sender);
            if(allowedStrings.size() == 1){
                builder.append(prefix + allowedStrings.get(0));
            }else{
                if(arg.alias != null) builder.append(prefix + "<" + arg.alias + ">");
            }
            //explanation
            StringBuilder explanation = new StringBuilder();
            for(String exp : arg.explanations){
                explanation.append("§d").append(exp).append("\n");
            }
            if(arg.explanations.size() != 0) explanation.append("\n");

            if(allowedStrings.size() != 0 && allowedStrings.size() != 1){
                explanation.append("§e==========Available Parameter==========\n");
                for(String argument: allowedStrings){
                    explanation.append("§d").append(argument).append("\n");
                }
            }
            builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(explanation.toString())));
            builder.append(" ");
            builder.event((HoverEvent) null);
        }

        return builder.create();
    }



}
