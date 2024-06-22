package de.ntg.economy.main;

import de.ntg.economy.API.EconomyAPI;
import de.ntg.economy.commands.EcoCommand;
import de.ntg.economy.commands.PayCommand;
import de.ntg.economy.events.Events;
import de.ntg.economy.files.Config;
import de.ntg.economy.files.Data;
import de.ntg.economy.files.MessageManager;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class BetterEconomy extends JavaPlugin {

    private static BetterEconomy instace;


    private static Data data;

    private static EconomyAPI api;

    private static Config config;

    private static MessageManager messageManager;

    public static BetterEconomy getInstance() {
        return instace;
    }

    public static EconomyAPI getAPI() {
        return api;
    }

    public Data getData() {
        return data;
    }

    public Config getPluginConfig() {
        return config;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        loadConfigs();
        instace = this;
        data = new Data(getInstance());
        api = new EconomyAPI();
        config = new Config();
        messageManager = new MessageManager();
        int pluginId = 22333; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);

        getCommand("eco").setExecutor(new EcoCommand());
        getCommand("pay").setExecutor(new PayCommand());


        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) { //


            new PlaceholderAPI(this).register(); //


        }

        if (getPluginConfig().getDatabaseEnable()) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                @Override
                public void run() {
                    data.connect();
                }
            }, 0L, 36000L); //0 Tick initial delay, 20 Tick (1 Second) between repeats
        } else {
            data.connect();
        }


        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new Events(), this);
        getLogger().info("§aBetter Economy loaded");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void loadConfigs() {
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveResource("config.yml", false);
        }
        if (!new File(getDataFolder(), "language/lang_en.yml").exists()) {
            saveResource("language/lang_en.yml", false);
        }
        if (!new File(getDataFolder(), "language/lang_fr.yml").exists()) {
            saveResource("language/lang_fr.yml", false);
        }
        if (!new File(getDataFolder(), "language/lang_it.yml").exists()) {
            saveResource("language/lang_it.yml", false);
        }
        if (!new File(getDataFolder(), "language/lang_ru.yml").exists()) {
            saveResource("language/lang_ru.yml", false);
        }
        if (!new File(getDataFolder(), "language/lang_de.yml").exists()) {
            saveResource("language/lang_de.yml", false);
        }

    }
}