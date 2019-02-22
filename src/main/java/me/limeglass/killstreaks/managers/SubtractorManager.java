package me.limeglass.killstreaks.managers;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.limeglass.killstreaks.Killstreaks;
import me.limeglass.killstreaks.objects.Killstreak;
import me.limeglass.killstreaks.objects.KillstreakSubtractor;

public class SubtractorManager {

	private final static Map<String, Class<? extends KillstreakSubtractor>> subtractors = new HashMap<>();
	private final static List<KillstreakSubtractor> running = new ArrayList<>();
	
	public static Map<String, Class<? extends KillstreakSubtractor>> getSubtractors() {
		return subtractors;
	}
	
	public static List<KillstreakSubtractor> getRunningSubtractors() {
		return running;
	}
	
	public static Class<? extends KillstreakSubtractor> registerSubtractor(String name, Class<? extends KillstreakSubtractor> subtractor) {
		return subtractors.put(name, subtractor);
	}
	
	public static void start(EntityDamageByEntityEvent event, Killstreak killstreak, FileConfiguration configuration) {
		String type = configuration.getString("killstreaks.type", "Time");
		Entity entity = event.getDamager();
		if (!(entity instanceof Player))
			return;
		Player player = (Player) entity;
		subtractors.entrySet().parallelStream()
				.filter(entry -> entry.getKey().equalsIgnoreCase(type))
				.map(entry -> entry.getValue())
				.forEach(clazz -> {
					try {
						KillstreakSubtractor subtractor = clazz.getConstructor(Player.class).newInstance(player);
						subtractor.setKillstreak(killstreak);
						if (running.contains(subtractor)) {
							subtractor.interrupt(event);
							return;
						}
						Killstreaks.debugMessage("Starting Subtractor " + subtractor.getClass().getName() + " for " + player.getName() + " at killstreak " + subtractor.getKillstreak().getStreak());
						running.add(subtractor);
						subtractor.onStart(event);
					} catch (NoSuchMethodException e) {
						Killstreaks.getInstance().getLogger().log(Level.SEVERE, "A subtractor must have a Player.class constructor");
						e.printStackTrace();
					} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}
				});
	}
	
	public static void finish(KillstreakSubtractor subtractor) {
		if (running.contains(subtractor)) {
			running.remove(subtractor);
			Killstreaks.debugMessage("&eFinished Subtractor " + subtractor.getClass().getName() + " for " + subtractor.getPlayer().getName() + " going to killstreak " + (subtractor.getKillstreak().getStreak() - 1));
			subtractor.getKillstreak().subtract();
		}
	}
	
}
