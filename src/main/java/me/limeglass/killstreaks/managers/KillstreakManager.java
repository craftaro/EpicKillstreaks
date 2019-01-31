package me.limeglass.killstreaks.managers;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

import com.google.common.collect.Sets;

import me.limeglass.killstreaks.objects.Killstreak;

public class KillstreakManager {

	private static Set<Killstreak> killstreaks = new HashSet<>();
	
	public static boolean registerKillstreak(Killstreak killstreak) {
		return killstreaks.add(killstreak);
	}
	
	public static void clear(Player player) {
		Sets.newConcurrentHashSet(killstreaks).parallelStream()
				.filter(killstreak -> killstreak.getPlayer() == player)
				.forEach(killstreak -> killstreaks.remove(killstreak));
	}
	
	public static Killstreak getKillstreak(Player player) {
		return killstreaks.parallelStream()
				.filter(killstreak -> killstreak.getPlayer() == player)
				.findFirst()
				.orElseGet(() -> {
					Killstreak killstreak = new Killstreak(player);
					registerKillstreak(killstreak);
					return killstreak;
				});
	}
	
	public static Set<Killstreak> getKillstreaks() {
		return killstreaks;
	}
	
}
