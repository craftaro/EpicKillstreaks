package com.songoda.killstreaks.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.songoda.killstreaks.Killstreaks;
import com.songoda.killstreaks.utils.Formatting;

public class CommandHandler implements CommandExecutor {

	private final Killstreaks instance;

	public CommandHandler(Killstreaks instance) {
		this.instance = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length <= 0) {
			sender.sendMessage("");
			sender.sendMessage("&7[&6Killstreaks&7]");
			sender.sendMessage(Formatting.color("	&6/killstreaks reload"));
			sender.sendMessage("");
		} else if (!sender.hasPermission("epickillstreaks.reload")) {
			sender.sendMessage(Formatting.color("&cYou don't have to correct permissions to execute this command."));
		} else {
			sender.sendMessage(Formatting.color("&6Reloading Killstreaks."));
			instance.getServer().getPluginManager().disablePlugin(instance);
			instance.getServer().getPluginManager().enablePlugin(instance);
		}
		return true;
	}

}
