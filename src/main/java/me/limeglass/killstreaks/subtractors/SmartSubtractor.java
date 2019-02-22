package me.limeglass.killstreaks.subtractors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;

import me.limeglass.killstreaks.objects.KillstreakSubtractor;

public class SmartSubtractor extends KillstreakSubtractor {

	static {
		registerSubtractor("Smart", SmartSubtractor.class);
	}
	
	private long hitTime = System.currentTimeMillis();
	private final ConfigurationSection section;
	private int time, trys, radius;
	private BukkitTask timer;
	private Check check;
	private Event last;
	
	public SmartSubtractor(Player player) {
		super(player);
		this.section = configuration.getConfigurationSection("killstreaks.smart");
		this.time = section.getInt("remember-time", 30);
		this.radius = section.getInt("radius", 20);
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
					EntityTargetEvent event = (EntityTargetEvent) e;
					LivingEntity entity = (LivingEntity) ((EntityTargetEvent) e).getEntity();
					check = new Check(entity, event.getTarget());
				}
			}
		}, EntityDamageByEntityEvent.class, EntityTargetEvent.class);
	}
	
	private class Check {
		
		private final LivingEntity entity;
		private final Entity target;
		
		public Check(LivingEntity entity, Entity target) {
			this.entity = entity;
			this.target = target;
		}
		
		public LivingEntity getEntity() {
			return entity;
		}

		public Entity getTarget() {
			return target;
		}		
		
	}

	@Override
	public void onStart(EntityDamageByEntityEvent event) {
		timer = Bukkit.getScheduler().runTaskTimerAsynchronously(instance, new Runnable() {
			@Override
			public void run() {
				if (check != null) {
					Location eye = check.getEntity().getEyeLocation();
					RayTraceResult result = eye.getWorld().rayTraceEntities(eye, check.getTarget().getLocation().toVector(), radius);
					Entity entity = result.getHitEntity();
					if (entity != null && entity == check.getTarget()) {
						interrupt(event);
					} else {
						check = null;
					}
				} else if (trys > time * 2) {
					timer.cancel();
					finish();
				}
				trys++;
			}
		}, 0, time * 20);
	}

	@Override
	public void interrupt(EntityDamageByEntityEvent event) {
		hitTime = System.currentTimeMillis();
		trys = 0;
	}

}
