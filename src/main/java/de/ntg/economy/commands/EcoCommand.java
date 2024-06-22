package de.ntg.economy.commands;

import de.ntg.economy.enums.ConfigMessages;
import de.ntg.economy.enums.EconomyResponse;
import de.ntg.economy.main.BetterEconomy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EcoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // /eco <Playername> <set/give/remove> <amount>
        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (args.length == 0) {
                //Message PLAYERS_MONEY
                BetterEconomy.getInstance().getMessageManager().sendMessage(p, ConfigMessages.PLAYERS_MONEY, BetterEconomy.getAPI().get(p), p.getName());

            } else if (args.length == 1) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                if (target == null) {
                    //message PLAYER_NOT_FOUND
                    BetterEconomy.getInstance().getMessageManager().sendMessage(p, ConfigMessages.PLAYER_NOT_FOUND, 0, args[0]);
                    return true;
                }
                if (!p.hasPermission("BetterEconomy.see-others")) {
                    BetterEconomy.getInstance().getMessageManager().sendMessage(p, ConfigMessages.NO_PERMS, 0, args[0]);
                    return true;
                }
                BetterEconomy.getInstance().getMessageManager().sendMessage(p, ConfigMessages.OTHER_PLAYERS_MONEY, BetterEconomy.getAPI().get(target.getUniqueId()), target.getName());
            } else if (args.length == 3) {

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

                int amount = Integer.parseInt(args[2]);


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
                switch (args[1].toLowerCase()) {
                    case "set":
                        EconomyResponse economyResponse = BetterEconomy.getAPI().set(target.getUniqueId(), amount);

                        if (economyResponse == EconomyResponse.SUCCESSFUL) {
                            //Message SET_MONEY_PLAYER
                            BetterEconomy.getInstance().getMessageManager().sendMessage(p, ConfigMessages.SET_MONEY_PLAYER, amount, target.getName());
                        } else if (economyResponse == EconomyResponse.PLAYER_DOES_NOT_EXISTS) {
                            //message PLAYER_NOT_FOUND
                            BetterEconomy.getInstance().getMessageManager().sendMessage(p, ConfigMessages.PLAYER_NOT_FOUND, amount, target.getName());
                        } else if (economyResponse == EconomyResponse.PLAYER_DOES_NOT_HAVE_ENOUGH_MONEY) {
                            //message PLAYER_NOT_ENOUGH_MONEY
                            BetterEconomy.getInstance().getMessageManager().sendMessage(p, ConfigMessages.PLAYER_NOT_ENOUGH_MONEY, amount, target.getName());
                        }
                        break;
                    case "give":
                        economyResponse = BetterEconomy.getAPI().give(target.getUniqueId(), amount);

                        if (economyResponse == EconomyResponse.SUCCESSFUL) {
                            //Message GAVE_MONEY_PLAYER
                            BetterEconomy.getInstance().getMessageManager().sendMessage(p, ConfigMessages.GAVE_MONEY_PLAYER, amount, target.getName());
                        } else if (economyResponse == EconomyResponse.PLAYER_DOES_NOT_EXISTS) {
                            //message PLAYER_NOT_FOUND
                            BetterEconomy.getInstance().getMessageManager().sendMessage(p, ConfigMessages.PLAYER_NOT_FOUND, amount, target.getName());
                        } else if (economyResponse == EconomyResponse.PLAYER_DOES_NOT_HAVE_ENOUGH_MONEY) {
                            //message PLAYER_NOT_ENOUGH_MONEY
                            BetterEconomy.getInstance().getMessageManager().sendMessage(p, ConfigMessages.PLAYER_NOT_ENOUGH_MONEY, amount, target.getName());
                        }
                        break;
                    case "remove":
                        economyResponse = BetterEconomy.getAPI().remove(target.getUniqueId(), amount);
                        if (economyResponse == EconomyResponse.SUCCESSFUL) {
                            //Message REMOVED_MONEY_PLAYER
                            BetterEconomy.getInstance().getMessageManager().sendMessage(p, ConfigMessages.REMOVED_MONEY_PLAYER, amount, target.getName());
                        } else if (economyResponse == EconomyResponse.PLAYER_DOES_NOT_EXISTS) {
                            //message PLAYER_NOT_FOUND
                            BetterEconomy.getInstance().getMessageManager().sendMessage(p, ConfigMessages.PLAYER_NOT_FOUND, amount, target.getName());
                        } else if (economyResponse == EconomyResponse.PLAYER_DOES_NOT_HAVE_ENOUGH_MONEY) {
                            //message PLAYER_NOT_ENOUGH_MONEY
                            BetterEconomy.getInstance().getMessageManager().sendMessage(p, ConfigMessages.PLAYER_NOT_ENOUGH_MONEY, amount, target.getName());
                        }

                        break;
                    default:
                        BetterEconomy.getInstance().getMessageManager().sendMessage((Player) sender, ConfigMessages.ECO_USAGE, 0, null);
                        break;
                }
            }else {
                BetterEconomy.getInstance().getMessageManager().sendMessage((Player) sender, ConfigMessages.ECO_USAGE, 0, null);
            }


        } else {
            //Message NOT_A_PLAYER
            BetterEconomy.getInstance().getMessageManager().sendMessage((Player) sender, ConfigMessages.NOT_A_PLAYER, 0, null);
        }
        return false;
    }
}