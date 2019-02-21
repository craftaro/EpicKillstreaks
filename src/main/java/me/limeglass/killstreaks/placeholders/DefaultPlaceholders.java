package me.limeglass.killstreaks.placeholders;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.limeglass.killstreaks.objects.Killstreak;

public class DefaultPlaceholders {

	public static void register() {
		Placeholders.registerPlaceholder(new Placeholder<Killstreak>("%player%", "%attacker%") {
			@Override
			public String replace(Killstreak killstreak) {
				return killstreak.getPlayer().getName();
			}
		});
		Placeholders.registerPlaceholder(new Placeholder<CommandSender>("%sender%") {
			@Override
			public String replace(CommandSender sender) {
				return sender.getName();
			}
		});
		Placeholders.registerPlaceholder(new Placeholder<Killstreak>("%streak%") {
			@Override
			public String replace(Killstreak killstreak) {
				return killstreak.getStreak() + "";
			}
		});
		Placeholders.registerPlaceholder(new Placeholder<Player>("%player%") {
			@Override
			public String replace(Player player) {
				return player.getName();
			}
		});
		Placeholders.registerPlaceholder(new Placeholder<String>("%string%") {
			@Override
			public String replace(String string) {
				return string;
			}
		});
	}
	
}
