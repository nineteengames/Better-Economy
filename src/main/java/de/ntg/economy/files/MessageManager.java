package de.ntg.economy.files;

import de.ntg.economy.enums.ConfigMessages;
import de.ntg.economy.main.BetterEconomy;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

/*
 * Copyright (c) 2024 by nineteengames.
 */
public class MessageManager {

    private YamlConfiguration yamlConfiguration;

    public MessageManager() {
        File file = new File(BetterEconomy.getInstance().getDataFolder() + "/language", BetterEconomy.getInstance().getConfig().getString("language") + ".yml");
        if (file.exists()) {
            this.yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        } else {
            BetterEconomy.getInstance().getPluginLoader().disablePlugin(BetterEconomy.getInstance());

        }
    }

    public String getMessage(Player p, ConfigMessages message, int amount, String player_name) {
        String msg = yamlConfiguration.getString(message.name().toLowerCase());
        assert msg != null;

        if (msg.contains("&")) {
            msg = ChatColor.translateAlternateColorCodes('&', msg);
        }

        msg = msg.replaceAll("%amount%", String.valueOf(amount));
        msg = msg.replaceAll("%player%", player_name);

        if (p != null && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            msg = PlaceholderAPI.setPlaceholders(p, msg);
        }


        return msg;
    }

    public void sendMessage(Player p, ConfigMessages message, int amount, String player_name) {
        p.sendMessage(BetterEconomy.getInstance().getPluginConfig().getPrefix() + getMessage(p, message, amount, player_name));
    }
}