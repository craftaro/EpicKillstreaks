package com.songoda.killstreaks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.songoda.killstreaks.commands.CommandHandler;
import com.songoda.killstreaks.listeners.EventListener;
import com.songoda.killstreaks.managers.ActionManager;
import com.songoda.killstreaks.managers.CheckManager;
import com.songoda.killstreaks.managers.KillstreakManager;
import com.songoda.killstreaks.managers.VaultManager;
import com.songoda.killstreaks.placeholders.DefaultPlaceholders;
import com.songoda.killstreaks.utils.Actionbar;
import com.songoda.killstreaks.utils.Formatting;
import com.songoda.killstreaks.utils.Utils;

/*
 * Main class for the Killstreaks plugin.
 * Developed by LimeGlass
*/

public class Killstreaks extends JavaPlugin {

	private KillstreakManager killstreakManager;
	private static Killstreaks instance;
	private ActionManager actionManager;
	private CheckManager checkManager;
	private VaultManager vaultManager;
	private Actionbar actionbar; // Used for caching reflection.

	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		checkManager = new CheckManager();
		actionManager = new ActionManager();
		killstreakManager = new KillstreakManager();
		getCommand("killstreaks").setExecutor(new CommandHandler(this));
		getServer().getPluginManager().registerEvents(new EventListener(this), this);
		loadObjects("com.songoda.killstreaks", "actions", "checks", "subtractors");
		actionbar = new Actionbar();
		DefaultPlaceholders.register();
		if (!instance.getServer().getPluginManager().isPluginEnabled("Vault"))
			vaultManager = new VaultManager(this);
		consoleMessage("&a==================================");
		consoleMessage("&7EpicKillstreaks " + getDescription().getVersion() + " by &5Songoda <3&7!");
		consoleMessage("&7EpicKillstreaks has been &aEnabled.");
		consoleMessage("&a==================================");
	}

	/**
	 * Load any Killstreak objects. Register within the static block at the top of the Killstreak object.
	 * 
	 * @param mainPackage Main package to look into.
	 * @param subpackages The sub packages and any sub packages of that to scan through.
	 */
	public void loadObjects(String mainPackage, String... subpackages) {
		Utils.loadClasses(instance, "com.songoda.killstreaks", "actions", "checks", "subtractors");
	}

	public static void consoleMessage(String... messages) {
		for (String text : messages)
			Bukkit.getConsoleSender().sendMessage(Formatting.color("[EpicKillstreaks] " + text));
	}

	/**
	 * @return The killstreak manager used by Killstreaks.
	 */
	public KillstreakManager getKillstreakManager() {
		return killstreakManager;
	}

	/**
	 * Grab the main action manager and also another way to register Actions to EpicKillstreaks.
	 * @return The action manager used by Killstreaks.
	 */
	public ActionManager getActionManager() {
		return actionManager;
	}

	public VaultManager getVaultManager() {
		return vaultManager;
	}

	/**
	 * Grab the main check manager and also another way to register Checks to EpicKillstreaks.
	 * @return The check manager used by Killstreaks.
	 */
	public CheckManager getCheckManager() {
		return checkManager;
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
