package com.songoda.killstreaks.listeners;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;

import com.songoda.killstreaks.Killstreaks;
import com.songoda.killstreaks.managers.SubtractorManager;
import com.songoda.killstreaks.objects.Killstreak;
import com.songoda.killstreaks.objects.KillstreakEvent;

public class EventListener implements Listener {

	private final PluginManager pluginManager;
	private final Killstreaks instance;
	
	public EventListener(Killstreaks instance) {
		this.pluginManager = instance.getServer().getPluginManager();
		this.instance = instance;
	}
	
	@EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
		Killstreaks.getKillstreakManager().clear(event.getPlayer());
    }
	
	// We don't need to worry about priority. Just a Killstreak plugin.
	@EventHandler(priority = EventPriority.LOWEST)
    public void onKillstreak(EntityDeathEvent e) {
		EntityDamageEvent damage = e.getEntity().getLastDamageCause();
		if (damage instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent event = (EntityDamageByEntityEvent)damage;
			Entity attacker = event.getDamager();
			if (attacker.getType() == EntityType.PLAYER) {
				FileConfiguration configuration = instance.getConfig();
				if (Killstreaks.getCheckManager().call(event, configuration)) {
					Player player = (Player)attacker;
					Killstreak killstreak = Killstreaks.getKillstreakManager().getKillstreak(player);
					KillstreakEvent killstreakEvent = new KillstreakEvent(player, killstreak, killstreak.getStreak() + 1);
					pluginManager.callEvent(killstreakEvent);
					if (!killstreakEvent.isCancelled()) {
						Killstreaks.getActionManager().call(event, killstreak.increment(), configuration);
						SubtractorManager.start(event, killstreak, configuration);
						Killstreaks.debugMessage("KillstreakEvent was fired for "
								+ player.getName() + " with a " + killstreak.getStreak() + " killstreak");
					}
				}
			}
		}
    }

}
