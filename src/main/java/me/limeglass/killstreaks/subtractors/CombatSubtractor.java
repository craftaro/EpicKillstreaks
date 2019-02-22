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
import org.bukkit.plugin.EventExecutor;
import org.bukkit.scheduler.BukkitTask;

import me.limeglass.killstreaks.objects.KillstreakSubtractor;

public class CombatSubtractor extends KillstreakSubtractor {

	static {
		registerSubtractor("Combat", CombatSubtractor.class);
	}
	
	private long hitTime = System.currentTimeMillis();
	private final ConfigurationSection section;
	private int time, rememberTime;
	private boolean remember;
	private BukkitTask timer;
	private Event last; 
	
	public CombatSubtractor(Player player) {
		super(player);
		this.section = configuration.getConfigurationSection("killstreaks.combat");
		this.remember = section.getBoolean("remember-hits", false);
		this.rememberTime = section.getInt("remember-time", 5);
		time = section.getInt("combat-add-time", 10);
		registerExecutor(new EventExecutor() {
			@Override
			public void execute(Listener listener, Event event) throws EventException {
				if (event == null)
					return;
				if (last == event)
					return;
				if (timer == null || timer.isCancelled())
					return;
				if (!(event instanceof EntityDamageByEntityEvent))
					return;
				last = event;
				EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
				Entity attacker = e.getDamager();
				if (attacker.getType() == EntityType.PLAYER) {
					if (player == null)
						return;
					if (player.equals(e.getDamager())) {
						if (remember) {
							if ((System.currentTimeMillis() - hitTime) / 60 < rememberTime) {
								return;
							}
							hitTime = System.currentTimeMillis();
						}
						interrupt(e);
					}
				}
			}
		}, EntityDamageByEntityEvent.class);
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
		hitTime = System.currentTimeMillis();
	}

}
