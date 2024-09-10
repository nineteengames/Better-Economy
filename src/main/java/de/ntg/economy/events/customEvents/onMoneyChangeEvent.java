package de.ntg.economy.events.customEvents;


import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class onMoneyChangeEvent extends Event implements Cancellable {


    private double change_amount;
    private double newAmount;
    private UUID playersUUID;
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;

    public onMoneyChangeEvent(double change_amount, UUID playersUUID, double newAmount){
        this.change_amount = change_amount;
        this.newAmount = newAmount;
        this.playersUUID = playersUUID;
        this.isCancelled = false;
    }


    public double getChange_amount() {
        return change_amount;
    }

    public double getNewAmount() {
        return newAmount;
    }

    public void setNewAmount(int newAmount) {
        this.newAmount = newAmount;
    }

    public UUID getPlayersUUID() {
        return playersUUID;
    }
    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
