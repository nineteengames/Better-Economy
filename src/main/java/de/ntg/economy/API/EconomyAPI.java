package de.ntg.economy.API;

import de.ntg.economy.enums.EconomyResponse;
import de.ntg.economy.main.BetterEconomy;
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

    // Helper method to format amounts correctly using a dot as the decimal separator
    private String formatAmount(double amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US); // Ensures "." is used as a decimal separator
        DecimalFormat df = new DecimalFormat("#0.00", symbols);
        return df.format(roundToTwoDecimals(amount)); // Round to two decimal places before formatting
    }

    public EconomyResponse give(Player player, double amount) {
        if (isPlayerExisting(player.getUniqueId())) {
            double newAmount = BetterEconomy.getInstance().getData().getBalance(player) + roundToTwoDecimals(amount);
            BetterEconomy.getInstance().getData().setBalance(player, roundToTwoDecimals(newAmount));
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
            BetterEconomy.getInstance().getData().setBalance(player, roundToTwoDecimals(Double.parseDouble(formatAmount(amount))));
            return EconomyResponse.SUCCESSFUL;
        } else {
            return EconomyResponse.PLAYER_DOES_NOT_EXISTS;
        }
    }

    public EconomyResponse pay(Player player, Player target, double amount) {
        amount = roundToTwoDecimals(Double.parseDouble(formatAmount(amount)));
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
            return BetterEconomy.getInstance().getData().getBalance(player) >= roundToTwoDecimals(Double.parseDouble(formatAmount(amount)));
        } else {
            return false;
        }
    }

    public EconomyResponse give(UUID uuid, double amount) {
        if (isPlayerExisting(uuid)) {
            double newAmount = BetterEconomy.getInstance().getData().getBalance(uuid) + roundToTwoDecimals(Double.parseDouble(formatAmount(amount)));
            BetterEconomy.getInstance().getData().setBalance(uuid, roundToTwoDecimals(newAmount));
            return EconomyResponse.SUCCESSFUL;
        } else {
            return EconomyResponse.PLAYER_DOES_NOT_EXISTS;
        }
    }

    public EconomyResponse remove(UUID uuid, double amount) {
        if (isPlayerExisting(uuid)) {
            if (has(uuid, amount)) {
                double newAmount = BetterEconomy.getInstance().getData().getBalance(uuid) - roundToTwoDecimals(Double.parseDouble(formatAmount(amount)));
                BetterEconomy.getInstance().getData().setBalance(uuid, roundToTwoDecimals(newAmount));
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
            BetterEconomy.getInstance().getData().setBalance(uuid, roundToTwoDecimals(Double.parseDouble(formatAmount(amount))));
            return EconomyResponse.SUCCESSFUL;
        } else {
            return EconomyResponse.PLAYER_DOES_NOT_EXISTS;
        }
    }

    public EconomyResponse pay(UUID uuidPlayer, UUID uuidTarget, double amount) {
        amount = roundToTwoDecimals(Double.parseDouble(formatAmount(amount)));
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
            return BetterEconomy.getInstance().getData().getBalance(uuid) >= roundToTwoDecimals(Double.parseDouble(formatAmount(amount)));
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
