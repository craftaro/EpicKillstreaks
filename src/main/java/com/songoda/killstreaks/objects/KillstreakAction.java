package com.songoda.killstreaks.objects;

import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.songoda.killstreaks.Killstreaks;

public abstract class KillstreakAction extends KillstreakElement {

	private final String name;
	
	public KillstreakAction(String name) {
		this.name = name;
	}
	
	protected static void registerAction(KillstreakAction action) {
		Killstreaks.getInstance().getActionManager().registerAction(action);
	}
	
	/**
	 * Called when a player has gained a killstreak.
	 * Keep in mind this is called asynchronously.
	 * 
	 * @param event The event where the Killstreak took place.
	 * @param killstreak The Killstreak object called.
	 */
	public abstract void onKillstreak(EntityDamageByEntityEvent event, Killstreak killstreak);
	
	/**
	 * @return The name of the Action.
	 */
	public String getName() {
		return name;
	}

}
