package me.limeglass.killstreaks.objects;

import org.bukkit.entity.Player;

public class Killstreak {

	private final Player player;
	private int streak = 0;
	
	public Killstreak(Player player) {
		this.player = player;
	}

	/**
	 * Set the level this Killstreak is at.
	 * 
	 * @param streak The int level this Killstreak is at.
	 */
	public void setStreak(int streak) {
		this.streak = streak;
	}
	
	/**
	 * Increase the Killstreak level by 1.
	 */
	public Killstreak increment() {
		streak++;
		return this;
	}
	
	/**
	 * Decrease the Killstreak level by 1.
	 */
	public Killstreak subtract() {
		streak--;
		if (streak < 0)
			streak = 0;
		return this;
	}
	
	/**
	 * @return The int level this Killstreak is at.
	 */
	public int getStreak() {
		return streak;
	}
	
	/**
	 * @return The Player of the Killstreak.
	 */
	public Player getPlayer() {
		return player;
	}

}
