package me.limeglass.killstreaks.managers;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.limeglass.killstreaks.objects.Killstreak;
import me.limeglass.killstreaks.objects.KillstreakAction;

public class ActionManager {

	private static Set<KillstreakAction> actions = new HashSet<>();
	
	public static Set<KillstreakAction> getActions() {
		return actions;
	}
	
	public static boolean registerAction(KillstreakAction action) {
		return actions.add(action);
	}
	
	public static void call(EntityDamageByEntityEvent event, Killstreak killstreak, FileConfiguration configuration) {
		actions.parallelStream()
				.map(action -> {
					if (action.getConfiguration() == null) {
						action.setConfiguration(configuration);
					}
					return action;
				})
				.forEach(action -> action.onKillstreak(event, killstreak));
	}
	
}
