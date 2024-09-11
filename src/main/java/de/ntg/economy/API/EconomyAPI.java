package de.ntg.economy.API;

import de.ntg.economy.enums.EconomyResponse;
import de.ntg.economy.events.customEvents.onMoneyChangeEvent;
import de.ntg.economy.main.BetterEconomy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.UUID;

public class EconomyAPI {

    // Helper method to round and format amounts to 2 decimal places
    private double roundToTwoDecimals(double amount) {
        return Math.round(amount * 100.0) / 100.0;
    }


    public EconomyResponse give(Player player, double amount) {
        if (isPlayerExisting(player.getUniqueId())) {
            double newAmount = BetterEconomy.getInstance().getData().getBalance(player) + roundToTwoDecimals(amount);
            BetterEconomy.getInstance().getData().setBalance(player, roundToTwoDecimals(newAmount));
            onMoneyChangeEvent onMoneyChangeEvent = new onMoneyChangeEvent(roundToTwoDecimals(amount),player.getUniqueId(), roundToTwoDecimals(newAmount));
            Bukkit.getPluginManager().callEvent(onMoneyChangeEvent);
            return EconomyResponse.SUCCESSFUL;
        } else {
            return EconomyResponse.PLAYER_DOES_NOT_EXISTS;
        }
    }

    public double get(Player player) {
        if (isPlayerExisting(player.getUniqueId())) {
            return roundToTwoDecimals(BetterEconomy.getInstance().getData().getBalance(player.getUniqueId()));
        } else {
            return 0;
        }
    }

    public EconomyResponse remove(Player player, double amount) {
        if (isPlayerExisting(player.getUniqueId())) {
            if (has(player, amount)) {
                double newAmount = BetterEconomy.getInstance().getData().getBalance(player) - roundToTwoDecimals(amount);
                BetterEconomy.getInstance().getData().setBalance(player, roundToTwoDecimals(newAmount));
                onMoneyChangeEvent onMoneyChangeEvent = new onMoneyChangeEvent(roundToTwoDecimals(amount),player.getUniqueId(), roundToTwoDecimals(newAmount));
                Bukkit.getPluginManager().callEvent(onMoneyChangeEvent);

            if(onMoneyChangeEvent.isCancelled()){
                return EconomyResponse.CANCELLED_BY_EVENT;
            }

            BetterEconomy.getInstance().getData().setBalance(player, onMoneyChangeEvent.getNewAmount());


                return EconomyResponse.SUCCESSFUL;
            } else {
                return EconomyResponse.PLAYER_DOES_NOT_HAVE_ENOUGH_MONEY;
            }

        } else {
            return EconomyResponse.PLAYER_DOES_NOT_EXISTS;
        }
    }

    public EconomyResponse set(Player player, double amount) {
        if (isPlayerExisting(player.getUniqueId())) {
            BetterEconomy.getInstance().getData().setBalance(player, roundToTwoDecimals(amount));
            onMoneyChangeEvent onMoneyChangeEvent = new onMoneyChangeEvent(roundToTwoDecimals(amount),player.getUniqueId(), roundToTwoDecimals(amount));
            Bukkit.getPluginManager().callEvent(onMoneyChangeEvent);
            return EconomyResponse.SUCCESSFUL;
        } else {
            return EconomyResponse.PLAYER_DOES_NOT_EXISTS;
        }
    }

    public EconomyResponse pay(Player player, Player target, double amount) {
        amount = roundToTwoDecimals(roundToTwoDecimals(amount));
        if (isPlayerExisting(player.getUniqueId()) && isPlayerExisting(target.getUniqueId())) {
            if (has(player, amount)) {
                remove(player, amount);
                give(target, amount);
                return EconomyResponse.SUCCESSFUL;
            } else {
                return EconomyResponse.PLAYER_DOES_NOT_HAVE_ENOUGH_MONEY;
            }

        } else {
            return EconomyResponse.PLAYER_DOES_NOT_EXISTS;
        }
    }

    public boolean has(Player player, double amount) {
        if (isPlayerExisting(player.getUniqueId())) {
            return BetterEconomy.getInstance().getData().getBalance(player) >= roundToTwoDecimals(amount);
        } else {
            return false;
        }
    }

    public EconomyResponse give(UUID uuid, double amount) {
        if (isPlayerExisting(uuid)) {
            double newAmount = BetterEconomy.getInstance().getData().getBalance(uuid) + roundToTwoDecimals(amount);
            BetterEconomy.getInstance().getData().setBalance(uuid, roundToTwoDecimals(newAmount));
            onMoneyChangeEvent onMoneyChangeEvent = new onMoneyChangeEvent(roundToTwoDecimals(amount),uuid, roundToTwoDecimals(newAmount));
            Bukkit.getPluginManager().callEvent(onMoneyChangeEvent);
            return EconomyResponse.SUCCESSFUL;
        } else {
            return EconomyResponse.PLAYER_DOES_NOT_EXISTS;
        }
    }

    public EconomyResponse remove(UUID uuid, double amount) {
        if (isPlayerExisting(uuid)) {
            if (has(uuid, amount)) {
                double newAmount = BetterEconomy.getInstance().getData().getBalance(uuid) - roundToTwoDecimals(amount);
                BetterEconomy.getInstance().getData().setBalance(uuid, roundToTwoDecimals(newAmount));
                onMoneyChangeEvent onMoneyChangeEvent = new onMoneyChangeEvent(roundToTwoDecimals(amount),uuid, roundToTwoDecimals(newAmount));
                Bukkit.getPluginManager().callEvent(onMoneyChangeEvent);
                return EconomyResponse.SUCCESSFUL;
            } else {
                return EconomyResponse.PLAYER_DOES_NOT_HAVE_ENOUGH_MONEY;
            }
        } else {
            return EconomyResponse.PLAYER_DOES_NOT_EXISTS;
        }
    }

    public EconomyResponse set(UUID uuid, double amount) {
        if (isPlayerExisting(uuid)) {
            BetterEconomy.getInstance().getData().setBalance(uuid, roundToTwoDecimals(amount));
            onMoneyChangeEvent onMoneyChangeEvent = new onMoneyChangeEvent(roundToTwoDecimals(amount),uuid, roundToTwoDecimals(amount));
            Bukkit.getPluginManager().callEvent(onMoneyChangeEvent);
            return EconomyResponse.SUCCESSFUL;
        } else {
            return EconomyResponse.PLAYER_DOES_NOT_EXISTS;
        }
    }

    public EconomyResponse pay(UUID uuidPlayer, UUID uuidTarget, double amount) {
        amount = roundToTwoDecimals(amount);
        if (isPlayerExisting(uuidPlayer) && isPlayerExisting(uuidTarget)) {
            if (has(uuidPlayer, amount)) {
                remove(uuidPlayer, amount);
                give(uuidTarget, amount);
                return EconomyResponse.SUCCESSFUL;
            } else {
                return EconomyResponse.PLAYER_DOES_NOT_HAVE_ENOUGH_MONEY;
            }

        } else {
            return EconomyResponse.PLAYER_DOES_NOT_EXISTS;
        }
    }

    public boolean has(UUID uuid, double amount) {
        if (isPlayerExisting(uuid)) {
            return BetterEconomy.getInstance().getData().getBalance(uuid) >= roundToTwoDecimals(amount);
        } else {
            return false;
        }
    }

    public double get(UUID uuid) {
        if (isPlayerExisting(uuid)) {
            return roundToTwoDecimals(BetterEconomy.getInstance().getData().getBalance(uuid));
        } else {
            return 0;
        }
    }

    public boolean isPlayerExisting(UUID uuid) {
        return BetterEconomy.getInstance().getData().isPlayerExisting(uuid);
    }
}
