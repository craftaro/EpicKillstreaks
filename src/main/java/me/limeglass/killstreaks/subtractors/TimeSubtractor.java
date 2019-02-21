package me.limeglass.killstreaks.subtractors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitTask;

import me.limeglass.killstreaks.Killstreaks;
import me.limeglass.killstreaks.objects.KillstreakSubtractor;

public class TimeSubtractor extends KillstreakSubtractor {

	static {
		time = Killstreaks.getInstance().getConfig().getInt("killstreaks.time.subtraction-time", 10);
		registerSubtractor("Time", TimeSubtractor.class);
	}
	
	private BukkitTask timer;
	private static int time;
	
	public TimeSubtractor(Player player) {
		super(player);
	}

	@Override
	public void onStart(EntityDamageByEntityEvent event) {
		timer = Bukkit.getScheduler().runTaskLaterAsynchronously(Killstreaks.getInstance(), new Runnable() {
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
