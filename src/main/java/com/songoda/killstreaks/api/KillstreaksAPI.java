package com.songoda.killstreaks.api;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.songoda.killstreaks.managers.ActionManager;
import com.songoda.killstreaks.managers.CheckManager;
import com.songoda.killstreaks.managers.KillstreakManager;
import com.songoda.killstreaks.managers.SubtractorManager;
import com.songoda.killstreaks.objects.Killstreak;
import com.songoda.killstreaks.objects.KillstreakAction;
import com.songoda.killstreaks.objects.KillstreakCheck;
import com.songoda.killstreaks.objects.KillstreakSubtractor;

public class KillstreaksAPI {

	private final Plugin plugin;
	
	public KillstreaksAPI(Plugin plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Register a subtractor to Killstreaks.
	 * Subtractors are what subtract the killstreaks over time or any way needed.
	 * It will be called and when it's ready it calls the finish() method.
	 * When the finish method is called, the killstreaks will get subtracted by one.
	 * 
	 * @param name The name to be set in the configuration to use that Subtractor.
	 * @param subtractor The class that extends KillstreakSubtractor
	 * @return The registered Class<? extends KillstreakSubtractor>
	 */
	public Class<? extends KillstreakSubtractor> registerSubtractor(String name, Class<? extends KillstreakSubtractor> subtractor) {
		return SubtractorManager.registerSubtractor(name, subtractor);
	}
	
	/**
	 * Register an action to Killstreaks
	 * Actions are extendible effects that get triggered on Killstreaks.
	 * 
	 * @param action The KillstreakAction object to register.
	 */
	public boolean registerAction(KillstreakAction action) {
		return ActionManager.registerAction(action);
	}
	
	/**
	 * Register a check to Killstreaks
	 * Checks are conditions that allows a Killstreak to levelup.
	 * 
	 * @param check The KillstreakCheck object to register.
	 */
	public boolean registerCheck(KillstreakCheck check) {
		return CheckManager.registerCheck(check);
	}
	
	/**
	 * @return Collection<Class<? extends KillstreakSubtractor>> of all the registered classes of KillstreakSubtractor.
	 */
	public Collection<Class<? extends KillstreakSubtractor>> getSubtractors() {
		return Collections.unmodifiableCollection(SubtractorManager.getSubtractors().values());
	}
	
	/**
	 * @return List<KillstreakSubtractor> of all the running KillstreakSubtractor.
	 */
	public List<KillstreakSubtractor> getRunningSubtractors() {
		return SubtractorManager.getRunningSubtractors();
	}
	
	/**
	 * Grabs a player's current Killstreak.
	 * 
	 * @param player The Player to grab a Killstreak of.
	 * @return The Killstreak object.
	 */
	public Killstreak getKillstreak(Player player) {
		return KillstreakManager.getKillstreak(player);
	}
	
	/**
	 * @return Set<KillstreakAcion> of all the registered KillstreakActions.
	 */
	public Set<KillstreakAction> getActions() {
		return ActionManager.getActions();
	}
	
	/**
	 * @return Set<KillstreakCheck> of all the registered KillstreakChecks.
	 */
	public Set<KillstreakCheck> getChecks() {
		return CheckManager.getChecks();
	}

	/**
	 * Grabs the plugin this KillstreakAPI is registered towards.
	 * 
	 * @return The Plugin registered to this instance.
	 */
	public Plugin getPlugin() {
		return plugin;
	}
	
}
