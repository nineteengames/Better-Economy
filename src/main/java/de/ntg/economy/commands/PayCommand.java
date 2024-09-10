package de.ntg.economy.commands;

import de.ntg.economy.enums.ConfigMessages;
import de.ntg.economy.enums.EconomyResponse;
import de.ntg.economy.main.BetterEconomy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 2) {
                Player target = Bukkit.getPlayer(args[0]);

                double amount = Double.parseDouble(args[1]);


                if (amount < 0) {
                    //message USE_POSITIVE_NUMBERS
                    BetterEconomy.getInstance().getMessageManager().sendMessage(p, ConfigMessages.USE_POSITIVE_NUMBERS, amount, p.getName());
                    return true;
                }

                if (target == null) {
                    //message PLAYER_NOT_FOUND
                    BetterEconomy.getInstance().getMessageManager().sendMessage(p, ConfigMessages.PLAYER_NOT_FOUND, amount, args[0]);
                    return true;
                }


                EconomyResponse economyResponse = BetterEconomy.getAPI().remove(p, amount);
                if (economyResponse == EconomyResponse.SUCCESSFUL) {
                    BetterEconomy.getAPI().give(target, amount);
                    BetterEconomy.getInstance().getMessageManager().sendMessage(p, ConfigMessages.GAVE_MONEY_PLAYER, amount, target.getName());
                    BetterEconomy.getInstance().getMessageManager().sendMessage(target, ConfigMessages.GOT_MONEY, amount, p.getName());
                } else if (economyResponse == EconomyResponse.PLAYER_DOES_NOT_HAVE_ENOUGH_MONEY) {
                    BetterEconomy.getInstance().getMessageManager().sendMessage(p, ConfigMessages.PLAYER_NOT_ENOUGH_MONEY, BetterEconomy.getAPI().get(p), p.getName());
                }
            }
        }
        return false;
    }
}
