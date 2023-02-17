
# SCommandRouter

A library to easily create minecraft plugin commands in spigot/paper.


## Demo

#### Kotlin
```kotlin
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
        addCommand( // equivalent to /demo test1 <player> <color>
                SCommandObject()
                        .prefix("test1")
                        .argument("player", SCommandArgumentType.ONLINE_PLAYER)
                        .argument("color", "blue", "red", "yellow")
                        .explanation("Command to give player a colored wool")
                        .permission("CommandRouter.color")
                        .executor(GiveWoolCommand(this))
        )
    }

```
#### Java
```java
public class DemoCommandRouterJava extends SCommandRouter {

    SCommandRouterMain plugin;

    public DemoCommandRouterJava(SCommandRouterMain plugin) {
        super(plugin, "demo");
        this.plugin = plugin;
        registerCommands();
        registerEvents();
    }

    public void registerEvents() {
        setNoPermissionEvent(e -> e.sender.sendMessage("§c§lYou do not have permissions"));
        setOnNoCommandFoundEvent(e -> e.sender.sendMessage("§c§lThe command doesn't exist"));
    }

    public void registerCommands() {
        addCommand( // equivalent to /demo test1 <player> <color>
                new SCommandObject()
                        .prefix("test1")
                        .argument("player", SCommandArgumentType.ONLINE_PLAYER)
                        .argument("color", "blue", "red", "yellow")
                        .explanation("Command to give player a colored wool")
                        .permission("CommandRouter.color")
                        .executor(new GiveWoolCommand(this))
        );
    }
}
```

### Supports inline tab completion candidate generation and inline execution
```Kotlin
addCommand(
        SCommandObject()
                .prefix("test2")
                .argument("offlinePlayer") { e: CommandSender? ->
                    //offline player name candidates for the parameter `offlinePlayer/args[1]`
                    val result = ArrayList<String?>()
                    for (p in Bukkit.getOfflinePlayers()) {
                        result.add(p.name)
                    }
                    result
                }
                .explanation("Display name of uuid")
                .permission("CommandRouter.uuid.search")

                .inlineExecutor { e: SCommandData -> // inline executor
                    val player = Bukkit.getOfflinePlayer(e.args[1])
                    e.sender.sendMessage("uuid of " + e.args[1] + " is " + player.uniqueId)
                }
)
```