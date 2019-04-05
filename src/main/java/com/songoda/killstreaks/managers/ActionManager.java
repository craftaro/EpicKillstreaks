package com.songoda.killstreaks.managers;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.songoda.killstreaks.Killstreaks;
import com.songoda.killstreaks.objects.Killstreak;
import com.songoda.killstreaks.objects.KillstreakAction;

public class ActionManager {

	private static Set<KillstreakAction> actions = new HashSet<>();
	
	public static Set<KillstreakAction> getActions() {
		return actions;
	}
	
	public static boolean registerAction(KillstreakAction action) {
		return actions.add(action);
	}
	
	public static void call(EntityDamageByEntityEvent event, Killstreak killstreak, FileConfiguration configuration) {
		for (KillstreakAction action : actions) {
			Killstreaks.debugMessage("&dCalled Action " + action.getClass().getName());
			action.onKillstreak(event, killstreak);
		}
	}
	
}
