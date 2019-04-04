package me.limeglass.killstreaks.actions;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.limeglass.killstreaks.objects.Killstreak;
import me.limeglass.killstreaks.objects.KillstreakAction;
import me.limeglass.killstreaks.utils.MessageBuilder;

public class TitleAction extends KillstreakAction {

	static {
		registerAction(new TitleAction());
	}
	
	public TitleAction() {
		super("Title");
	}

	@Override
	public void onKillstreak(EntityDamageByEntityEvent event, Killstreak killstreak) {
		if (!configuration.isConfigurationSection("effects.title"))
			return;
		Player attacker = killstreak.getPlayer();
		Entity victim = event.getEntity();
		ConfigurationSection section = configuration.getConfigurationSection("effects.title");
		int streak = killstreak.getStreak();
		if (!section.getIntegerList("streaks").contains(streak))
			return;
		if (section.getBoolean("radius.enabled", false)) {
			int x = section.getInt("radius.x-radius", 20);
			int y = section.getInt("radius.y-radius", 20);
			int z = section.getInt("radius.z-radius", 20);
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (!attacker.getNearbyEntities(x, y, z).contains(player))
					continue;
				MessageBuilder builder = new MessageBuilder(section, "title-others")
						.replace("%attacker%", attacker.getName())
						.replace("%victim%", victim.getName())
						.setKillstreak(killstreak);
				if (player == attacker)
					builder.setNodes("title-self")
							.replace("%receiver%", player.getName())
							.sendTitle(player);
				else
					builder.replace("%receiver%", attacker.getName())
							.sendTitle(attacker);
			}
		} else {
			for (Player player : Bukkit.getOnlinePlayers()) {
				MessageBuilder builder = new MessageBuilder(section, "title-others")
						.replace("%attacker%", attacker.getName())
						.replace("%receiver%", player.getName())
						.replace("%victim%", victim.getName())
						.setKillstreak(killstreak);
				if (player == attacker && section.getBoolean("title-self.enabled", false))
					builder.setNodes("title-self").sendTitle(attacker);
				else if (section.getBoolean("title-others.enabled", false))
					builder.sendTitle(player);
			}
		}
	}
	
}