package com.songoda.killstreaks.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.songoda.killstreaks.Killstreaks;
import com.songoda.killstreaks.utils.MessageBuilder;

public class CommandHandler implements CommandExecutor {

	private final ConfigurationSection section;
	private final Killstreaks instance;

	public CommandHandler(Killstreaks instance) {
		this.section = instance.getConfig().getConfigurationSection("messages");
		this.instance = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			if (!(sender instanceof Player))
				new MessageBuilder(section, "player-only")
						.setPlaceholderObject(sender)
						.send(sender);
			if (!sender.hasPermission("epickillstreaks.check")) {
				new MessageBuilder(section, "no-permission")
						.setPlaceholderObject(sender)
						.send(sender);
				return true;
			}
			Player player = (Player) sender;
			new MessageBuilder(section, "killstreak")
					.replace("%killstreak%", instance.getKillstreakManager().getKillstreak(player).getStreak())
					.setPlaceholderObject(sender)
					.send(sender);
		} else {
			Player player = Bukkit.getPlayer(args[0]);
			if (player == null) {
				new MessageBuilder(section, "no-player-found")
						.replace("%player%", args[0])
						.send(sender);
				return true;
			}
			if (!sender.hasPermission("epickillstreaks.other")) {
				new MessageBuilder(section, "no-permission")
						.setPlaceholderObject(sender)
						.send(sender);
				return true;
			}
			new MessageBuilder(section, "killstreak-other")
					.replace("%killstreak%", instance.getKillstreakManager().getKillstreak(player).getStreak())
					.replace("%player%", player.getName())
					.send(sender);
		}
		return true;
	}

}
