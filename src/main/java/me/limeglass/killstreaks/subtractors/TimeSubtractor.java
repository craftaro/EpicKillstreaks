package me.limeglass.killstreaks.subtractors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitTask;

import me.limeglass.killstreaks.objects.KillstreakSubtractor;

public class TimeSubtractor extends KillstreakSubtractor {

	static {
		registerSubtractor("Time", TimeSubtractor.class);
	}
	
	private BukkitTask timer;
	private final int time;
	
	public TimeSubtractor(Player player) {
		super(player);
		time = configuration.getInt("killstreaks.time.subtraction-time", 10);
	}

	@Override
	public void onStart(EntityDamageByEntityEvent event) {
		timer = Bukkit.getScheduler().runTaskLaterAsynchronously(instance, new Runnable() {
			@Override
			public void run() {
				finish();
			}
		}, time * 20);
	}

	@Override
	public void interrupt(EntityDamageByEntityEvent event) {
		timer.cancel();
		onStart(event);
	}

}
