package me.limeglass.killstreaks.subtractors;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.scheduler.BukkitTask;

import me.limeglass.killstreaks.Killstreaks;
import me.limeglass.killstreaks.objects.KillstreakSubtractor;

public class SmartSubtractor extends KillstreakSubtractor {

	static {
		ConfigurationSection section = Killstreaks.getInstance().getConfig().getConfigurationSection("killstreaks.smart");
		time = section.getInt("remember-time", 30);
		/*x = section.getInt("x-radius", 20);
		y = section.getInt("y-radius", 20);
		z = section.getInt("z-radius", 20);
		*/registerSubtractor("Smart", SmartSubtractor.class);
	}
	
	private long hitTime = System.currentTimeMillis();
	private static int time, trys;//, x, y, z;
	private BukkitTask timer;
	private boolean finish;
	private Event last;
	
	public SmartSubtractor(Player player) {
		super(player);
		registerExecutor(new EventExecutor() {
			@Override
			public void execute(Listener listener, Event e) throws EventException {
				if (e == null)
					return;
				if (last == e)
					return;
				if (timer == null || timer.isCancelled())
					return;
				last = e;
				if (e instanceof EntityDamageByEntityEvent) {
					EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
					Entity attacker = event.getDamager();
					if (attacker.getType() == EntityType.PLAYER) {
						if (player == null)
							return;
						if (player.equals(event.getDamager())) {
							if ((System.currentTimeMillis() - hitTime) / 60 < time) {
								return;
							}
							hitTime = System.currentTimeMillis();
							interrupt(event);
						}
					}
				} else if (e instanceof EntityTargetEvent) {
					//EntityTargetEvent event = (EntityTargetEvent) e;
				}
			}
		}, EntityDamageByEntityEvent.class, EntityTargetEvent.class);
	}

	@Override
	public void onStart(EntityDamageByEntityEvent event) {
		timer = Bukkit.getScheduler().runTaskTimerAsynchronously(Killstreaks.getInstance(), new Runnable() {
			@Override
			public void run() {
				if (finish || trys > time * 2) {
					timer.cancel();
					finish();
				}
				trys++;
			}
		}, 0, 20); // Every second.
	}

	@Override
	public void interrupt(EntityDamageByEntityEvent event) {
		hitTime = System.currentTimeMillis();
	}

}
