package me.limeglass.killstreaks.api;

import java.util.Set;

import org.bukkit.entity.Player;

import me.limeglass.killstreaks.managers.ActionManager;
import me.limeglass.killstreaks.managers.CheckManager;
import me.limeglass.killstreaks.managers.KillstreakManager;
import me.limeglass.killstreaks.objects.Killstreak;
import me.limeglass.killstreaks.objects.KillstreakAction;
import me.limeglass.killstreaks.objects.KillstreakCheck;

public class KillstreaksAPI {

	/**
	 * Register an action to Killstreaks
	 * Actions are extendible effects that get triggered on Killstreaks.
	 * 
	 * @param action The KillstreakAction object to register.
	 */
	public static boolean registerAction(KillstreakAction action) {
		return ActionManager.registerAction(action);
	}
	
	/**
	 * Register a check to Killstreaks
	 * Checks are conditions that allows a Killstreak to levelup.
	 * 
	 * @param check The KillstreakCheck object to register.
	 */
	public static boolean registerCheck(KillstreakCheck check) {
		return CheckManager.registerCheck(check);
	}
	
	/**
	 * Grabs a player's current Killstreak.
	 * 
	 * @param player The Player to grab a Killstreak of.
	 * @return The Killstreak object.
	 */
	public static Killstreak getKillstreak(Player player) {
		return KillstreakManager.getKillstreak(player);
	}
	
	/**
	 * @return Set<KillstreakAcion> of all the registered KillstreakActions.
	 */
	public static Set<KillstreakAction> getActions() {
		return ActionManager.getActions();
	}
	
	/**
	 * @return Set<KillstreakCheck> of all the registered KillstreakChecks.
	 */
	public static Set<KillstreakCheck> getChecks() {
		return CheckManager.getChecks();
	}
	
}
