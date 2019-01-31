package me.limeglass.killstreaks.actions;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.limeglass.killstreaks.objects.Killstreak;
import me.limeglass.killstreaks.objects.KillstreakAction;
import me.limeglass.killstreaks.utils.Utils;

public class BroadcastAction extends KillstreakAction {

	static {
		registerAction(new BroadcastAction());
	}
	
	public BroadcastAction() {
		super("Broadcast");
	}

	@Override
	public void onKillstreak(EntityDamageByEntityEvent event, Killstreak killstreak) {
		//#WorthTheAmountOfConfigurations?
		if (getConfiguration().isConfigurationSection("effects.chat")) {
			Player attacker = killstreak.getPlayer();
			Entity victim = event.getEntity();
			ConfigurationSection section = getConfiguration().getConfigurationSection("effects.chat");
			int streak = killstreak.getStreak();
			if (section.getBoolean("broadcast.enabled", false)) {
				section = section.getConfigurationSection("broadcast");
				if (section.getIntegerList("streaks").contains(streak)) {
					String message = section.getString("message", "&6%attacker% &cis on a &6%streak% &ckillstreak!");
					message = message.replaceAll("%attacker%", attacker.getName());
					message = message.replaceAll("%victim%", victim.getName());
					message = message.replaceAll("%streak%", streak + "");
					boolean all = section.getBoolean("all-worlds", true);
					boolean whitelist = section.getBoolean("world-whitelist", false);
					List<String> worlds = section.getStringList("worlds");
					for (Player player : Bukkit.getOnlinePlayers()) {
						String clone = message.replaceAll("%receiver%", player.getName());
						String world = player.getWorld().getName();
						if (all) {
							player.sendMessage(Utils.cc(clone));
						} else if (whitelist) {
							if (worlds.contains(world)) {
								player.sendMessage(Utils.cc(clone));
							}
						} else if (!worlds.contains(world)) {
							player.sendMessage(Utils.cc(clone));
						}
					}
				}
			} else if (section.getBoolean("message.enabled", false)) {
				section = section.getConfigurationSection("message");
				if (section.getIntegerList("streaks").contains(streak)) {
					String message = section.getString("message", "&6%attacker% &cis on a &6%streak% &ckillstreak!");
					message = message.replaceAll("%attacker%", attacker.getName());
					message = message.replaceAll("%victim%", victim.getName());
					message = message.replaceAll("%streak%", streak + "");
					boolean victimSent = false, attackerSent = false;
					if (section.getBoolean("message-attacker", false)) {
						String clone = message.replaceAll("%receiver%", attacker.getName());
						attacker.sendMessage(Utils.cc(clone));
						attackerSent = true;
					}
					if (section.getBoolean("message-victim", false) && attacker != victim) {
						String clone = message.replaceAll("%receiver%", victim.getName());
						victim.sendMessage(Utils.cc(clone));
						victimSent = true;
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
						String clone = message.replaceAll("%receiver%", player.getName());
						player.sendMessage(Utils.cc(clone));
					}
				}
			}
		}
	}
	
}
