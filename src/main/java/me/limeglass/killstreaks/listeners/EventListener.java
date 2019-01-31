package me.limeglass.killstreaks.listeners;

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

import me.limeglass.killstreaks.Killstreaks;
import me.limeglass.killstreaks.managers.ActionManager;
import me.limeglass.killstreaks.managers.CheckManager;
import me.limeglass.killstreaks.managers.KillstreakManager;
import me.limeglass.killstreaks.managers.SubtractorManager;
import me.limeglass.killstreaks.objects.Killstreak;
import me.limeglass.killstreaks.objects.KillstreakEvent;

public class EventListener implements Listener {
	
	@EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
		KillstreakManager.clear(event.getPlayer());
    }
	
	// We don't need to worry about priority. Just a Killstreak plugin.
	@EventHandler(priority = EventPriority.LOWEST)
    public void onKillstreak(EntityDeathEvent e) {
		EntityDamageEvent damage = e.getEntity().getLastDamageCause();
		if (damage instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent event = (EntityDamageByEntityEvent)damage;
			Entity attacker = event.getDamager();
			if (attacker.getType() == EntityType.PLAYER) {
				Killstreaks instance = Killstreaks.getInstance();
				FileConfiguration configuration = instance.getConfig();
				if (CheckManager.call(event, configuration)) {
					Player player = (Player)attacker;
					Killstreak killstreak = KillstreakManager.getKillstreak(player);
					KillstreakEvent killstreakEvent = new KillstreakEvent(player, killstreak, killstreak.getStreak() + 1);
					instance.getServer().getPluginManager().callEvent(killstreakEvent);
					if (!killstreakEvent.isCancelled()) {
						ActionManager.call(event, killstreak.increment(), configuration);
						SubtractorManager.start(event, killstreak, configuration);
						Killstreaks.debugMessage("KillstreakEvent was fired for "
								+ player.getName() + " with a " + killstreak.getStreak() + " killstreak");
					}
				}
			}
		}
    }

}
