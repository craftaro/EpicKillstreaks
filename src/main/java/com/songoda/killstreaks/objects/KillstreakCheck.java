package com.songoda.killstreaks.objects;

import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.songoda.killstreaks.managers.CheckManager;

/**
 * 
 * Useful for adding checks to the Killstreak plugin.
 * A check will allow a Killstreak to levelup if all have been passed.
 *
 */
public abstract class KillstreakCheck extends KillstreakElement {
	
	protected static void registerCheck(KillstreakCheck check) {
		CheckManager.registerCheck(check);
	}
	
	/**
	 * The check method to determine if the Killstreak should be leveled up.
	 * 
	 * @param event The EntityDamageByEntityEvent that has caused a Killstreak levelup.
	 * @return boolean If the Killstreak levelup should be allowed.
	 */
	public abstract boolean check(EntityDamageByEntityEvent event);

}
