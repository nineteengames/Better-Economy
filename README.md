[![](https://jitpack.io/v/nineteengames/Better-EconomyAPI.svg)](https://jitpack.io/#nineteengames/Better-EconomyAPI)
# Better-Economy

Is a simple and free Econmy plugin, with an simple API!

# How to implement?
How to include the API with Maven:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
<dependencies>
    <dependency>
        <groupId>com.github.nineteengames</groupId>
        <artifactId>Better-EconomyAPI</artifactId>
        <version>1.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```
How to include the API with Gradle:
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
dependencies {
    compileOnly "com.github.nineteengames:Better-EconomyAPI:1.0"
}
```

# How to use?
```java
package com.example.exampleplugin;

import de.ntg.economy.API.EconomyAPI;
import de.ntg.economy.enums.EconomyResponse;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExamplePlugin extends JavaPlugin {

    private static EconomyAPI economyAPI = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (!setupEconomyAPI() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Better-Economy found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info(String.format("[%s] Disabled Plugin %s", getDescription().getName(), getDescription().getVersion()));
    }
    private boolean setupEconomyAPI() {
        if (getServer().getPluginManager().getPlugin("Better-Economy") == null) {
            return false;
        }
        economyAPI = new EconomyAPI();
        return true;
    }

    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if(!(sender instanceof Player)) {
            getLogger().info("Only players are supported for this Example Plugin, but you should not do this!!!");
            return true;
        }

        Player player = (Player) sender;

        if(command.getLabel().equals("test-economy")) {
            // Lets give the player 100 currency
            int amount = 100;
            EconomyResponse r = economyAPI.give(player, amount);
            if(r == EconomyResponse.SUCCESSFUL) {
                sender.sendMessage(String.format("You were given %s and now have %s", amount, economyAPI.get(player));
            } else {
                sender.sendMessage(String.format("An error occured"));
            }
            return true;
        }
        return true;
    }

    public static EconomyAPI getEconomy() {
        return economyAPI;
    }
}


```
