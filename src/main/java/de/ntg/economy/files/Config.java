package de.ntg.economy.files;

import de.ntg.economy.main.BetterEconomy;
import net.md_5.bungee.api.ChatColor;

public class Config {

    public String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&', BetterEconomy.getInstance().getConfig().getString("prefix"));
    }

    public Double getStartMoney() {
        return BetterEconomy.getInstance().getConfig().getDouble("start_money");
    }

    //Mysql
    public Boolean getDatabaseEnable() {
        return BetterEconomy.getInstance().getConfig().getBoolean("Database.Enabled");
    }

    public String getDatabaseHost() {
        return BetterEconomy.getInstance().getConfig().getString("Database.Host");
    }

    public String getDatabasePort() {
        return BetterEconomy.getInstance().getConfig().getString("Database.Port");
    }

    public String getDatabaseDatabase() {
        return BetterEconomy.getInstance().getConfig().getString("Database.Database");
    }

    public String getDatabaseUsername() {
        return BetterEconomy.getInstance().getConfig().getString("Database.Username");
    }

    public String getDatabasePassword() {
        return BetterEconomy.getInstance().getConfig().getString("Database.Password");
    }
}
