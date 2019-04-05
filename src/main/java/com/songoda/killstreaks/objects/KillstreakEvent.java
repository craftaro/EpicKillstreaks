package com.songoda.killstreaks.objects;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KillstreakEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private final Killstreak killstreak;
	private final Player player;
	private boolean cancelled;
	private final int streak;
	
	public KillstreakEvent(Player player, Killstreak killstreak, int streak) {
		this.killstreak = killstreak;
		this.player = player;
		this.streak = streak;
	}
	
	public Killstreak getKillstreak() {
		return killstreak;
	}
	
	public int getNextStreak() {
		return streak;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
