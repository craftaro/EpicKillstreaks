package com.songoda.killstreaks.actions;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.songoda.killstreaks.objects.Killstreak;
import com.songoda.killstreaks.objects.KillstreakAction;
import com.songoda.killstreaks.utils.MessageBuilder;

public class BroadcastAction extends KillstreakAction {

	static {
		registerAction(new BroadcastAction());
	}
	
	public BroadcastAction() {
		super("Broadcast");
	}

	@Override
	public void onKillstreak(EntityDamageByEntityEvent event, Killstreak killstreak) {
		if (!configuration.isConfigurationSection("effects.chat"))
			return;
		Player attacker = killstreak.getPlayer();
		Entity victim = event.getEntity();
		ConfigurationSection section = configuration.getConfigurationSection("effects.chat");
		int streak = killstreak.getStreak();
		if (!section.getBoolean("broadcast.enabled", false)) {
			section = section.getConfigurationSection("broadcast");
			if (!section.getIntegerList("streaks").contains(streak))
				return;
			for (Player player : Bukkit.getOnlinePlayers()) {
				new MessageBuilder(section, "message")
						.replace("%attacker%", attacker.getName())
						.replace("%receiver%", player.getName())
						.replace("%victim%", victim.getName())
						.setKillstreak(killstreak)
						.send(player);
			}
		} else if (section.getBoolean("message.enabled", false)) {
			section = section.getConfigurationSection("message");
			if (!section.getIntegerList("streaks").contains(streak))
				return;
			MessageBuilder builder = new MessageBuilder(section, "message")
					.replace("%attacker%", attacker.getName())
					.replace("%victim%", victim.getName())
					.setKillstreak(killstreak);
			boolean victimSent = false, attackerSent = false;
			if (section.getBoolean("message-attacker", false)) {
				builder.replace("%receiver%", attacker.getName())
						.send(attacker);
				attackerSent = true;
			}
			if (section.getBoolean("message-victim", false) && attacker != victim) {
				if (victim instanceof Player) {
					builder.replace("%receiver%", victim.getName())
							.send((Player) victim);
					victimSent = true;
				}
			}
			boolean radius = section.getBoolean("radius.enabled", false);
			int x = section.getInt("radius.x-radius", 20);
			int y = section.getInt("radius.y-radius", 20);
			int z = section.getInt("radius.z-radius", 20);
			for (Player player : Bukkit.getOnlinePlayers()) {
				if ((radius && !attacker.getNearbyEntities(x, y, z).contains(player)) ||
						(attackerSent && player == attacker) ||
						(victimSent && player == victim)) {
					continue;
				}
				builder.replace("%receiver%", player.getName())
						.send(player);
			}
		}
	}

}
