package com.songoda.killstreaks.managers;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.songoda.killstreaks.objects.KillstreakCheck;

public class CheckManager {

	private static Set<KillstreakCheck> checks = new HashSet<>();
	
	public static boolean registerCheck(KillstreakCheck check) {
		return checks.add(check);
	}
	
	public static Set<KillstreakCheck> getChecks() {
		return checks;
	}
	
	public static boolean call(EntityDamageByEntityEvent event, FileConfiguration configuration) {
		return checks.stream().allMatch(checker -> checker.check(event));
	}
	
}
