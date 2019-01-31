package me.limeglass.killstreaks;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.limeglass.killstreaks.commands.CommandHandler;
import me.limeglass.killstreaks.listeners.EventListener;
import me.limeglass.killstreaks.utils.Utils;

/*
 * Main class for the Killstreaks plugin.
 * Developed by LimeGlass
*/

public class Killstreaks extends JavaPlugin {
	
	private static Killstreaks instance;
	
	public void onEnable() {
		instance = this;
		getCommand("killstreaks").setExecutor(new CommandHandler());
		getServer().getPluginManager().registerEvents(new EventListener(), this);
		File configFile = new File(getDataFolder(), "config.yml");
		// If newer version was found, update configuration.
		if (!getDescription().getVersion().equals(getConfig().getString("version", "old"))) {
			if (configFile.exists())
				configFile.delete();
		}
		saveDefaultConfig();
		Utils.loadClasses(this, "me.limeglass.killstreaks", "actions", "checks", "subtractors");
		consoleMessage("has been enabled!");
	}
	
	public static Killstreaks getInstance() {
		return instance;
	}
	
	public static void debugMessage(String text) {
		if (instance.getConfig().getBoolean("debug"))
			consoleMessage("&b" + text);
	}

	public static void consoleMessage(String... messages) {
		for (String text : messages)
			Bukkit.getConsoleSender().sendMessage(Utils.cc("[Killstreaks] " + text));
	}

}
