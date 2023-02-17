package com.shojabon.scommandrouter;

import com.shojabon.scommandrouter.DemoCommand.DemoCommandRouterJava;
import org.bukkit.plugin.java.JavaPlugin;

public final class SCommandRouterMain extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        new DemoCommandRouterJava(this);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
