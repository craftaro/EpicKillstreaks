package com.songoda.killstreaks.actions;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.songoda.killstreaks.objects.Killstreak;
import com.songoda.killstreaks.objects.KillstreakAction;
import com.songoda.killstreaks.utils.MessageBuilder;

public class ActionbarAction extends KillstreakAction {

	static {
		registerAction(new ActionbarAction());
	}
	
	public ActionbarAction() {
		super("Action");
	}

	@Override
	public void onKillstreak(EntityDamageByEntityEvent event, Killstreak killstreak) {
		if (!configuration.isConfigurationSection("effects.actionbar"))
			return;
		Player attacker = killstreak.getPlayer();
		Entity victim = event.getEntity();
		ConfigurationSection section = configuration.getConfigurationSection("effects.actionbar");
		int streak = killstreak.getStreak();
		if (!section.getBoolean("enabled", false))
			return;
		if (!section.getIntegerList("streaks").contains(streak))
			return;
		if (section.getBoolean("radius.enabled", false)) {
			int x = section.getInt("radius.x-radius", 20);
			int y = section.getInt("radius.y-radius", 20);
			int z = section.getInt("radius.z-radius", 20);
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (!attacker.getNearbyEntities(x, y, z).contains(player))
					continue;
				MessageBuilder builder = new MessageBuilder(section, "message-others")
						.replace("%attacker%", attacker.getName())
						.replace("%victim%", victim.getName())
						.setKillstreak(killstreak);
				if (player == attacker)
					builder.setNodes("message-self")
							.replace("%receiver%", player.getName())
							.sendActionbar(player);
				else
					builder.replace("%receiver%", attacker.getName())
							.sendActionbar(attacker);
			}
			return;
		}
		for (Player player : Bukkit.getOnlinePlayers()) {
			MessageBuilder builder = new MessageBuilder(section, "message-others")
					.replace("%attacker%", attacker.getName())
					.replace("%receiver%", player.getName())
					.replace("%victim%", victim.getName())
					.setKillstreak(killstreak);
			if (player == attacker)
				builder.setNodes("message-self").sendActionbar(attacker);
			else
				builder.sendActionbar(player);
		}
	}
	
}
