package me.limeglass.killstreaks.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.limeglass.killstreaks.Killstreaks;
import me.limeglass.killstreaks.utils.Utils;

public class CommandHandler implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			Killstreaks.consoleMessage("Only players may execute this command!");
			return true;
		}
		Player player = (Player) sender;
		/*if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("admin") && player.hasPermission("champions.admin")) {
				//player.sendMessage(Utils.getMessage(false, "adminhelp", player));
			} else if (args[0].equalsIgnoreCase("setspawn") && player.hasPermission("champions.admin")) {
				//player.sendMessage(Utils.getMessage(true, "setspawn", player));
			} else {
				player.performCommand("/killstreaks");
			}
		} else {
			//player.sendMessage(Utils.getMessage(false, "commandhelp", player));
		}*/
		player.sendMessage(Utils.cc("&6Coming soon."));
		return true;
	}

}
