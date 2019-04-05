package com.songoda.killstreaks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.songoda.killstreaks.commands.CommandHandler;
import com.songoda.killstreaks.listeners.EventListener;
import com.songoda.killstreaks.placeholders.DefaultPlaceholders;
import com.songoda.killstreaks.utils.Actionbar;
import com.songoda.killstreaks.utils.Formatting;
import com.songoda.killstreaks.utils.Utils;

/*
 * Main class for the Killstreaks plugin.
 * Developed by LimeGlass
*/

public class Killstreaks extends JavaPlugin {
	
	private static Killstreaks instance;
	private Actionbar actionbar; // Used for caching reflection.
	
	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		getCommand("killstreaks").setExecutor(new CommandHandler(this));
		getServer().getPluginManager().registerEvents(new EventListener(this), this);
		loadObjects("com.songoda.killstreaks", "actions", "checks", "subtractors");
		this.actionbar = new Actionbar();
		DefaultPlaceholders.register();
		consoleMessage("&a=============================");
		consoleMessage("&7EpicKillstreaks " + getDescription().getVersion() + " by &5Songoda <3&7!");
		consoleMessage("&7EpicKillstreaks has been &aEnabled.");
		consoleMessage("&a=============================");
	}

	public static void loadObjects(String mainPackage, String... subpackages) {
		Utils.loadClasses(instance, "com.songoda.killstreaks", "actions", "checks", "subtractors");
	}

	public static void consoleMessage(String... messages) {
		for (String text : messages)
			Bukkit.getConsoleSender().sendMessage(Formatting.color("[EpicKillstreaks] " + text));
	}

	public static void debugMessage(String text) {
		if (instance.getConfig().getBoolean("debug"))
			consoleMessage("&b" + text);
	}

	public static Killstreaks getInstance() {
		return instance;
	}

	public Actionbar getActionbar() {
		return actionbar;
	}

}
