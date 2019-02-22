package me.limeglass.killstreaks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.limeglass.killstreaks.commands.CommandHandler;
import me.limeglass.killstreaks.listeners.EventListener;
import me.limeglass.killstreaks.placeholders.DefaultPlaceholders;
import me.limeglass.killstreaks.utils.Actionbar;
import me.limeglass.killstreaks.utils.Utils;

/*
 * Main class for the Killstreaks plugin.
 * Developed by LimeGlass
*/

public class Killstreaks extends JavaPlugin {
	
	private static Killstreaks instance;
	private Actionbar actionbar; // Used for caching reflection.
	
	public void onEnable() {
		instance = this;
		getCommand("killstreaks").setExecutor(new CommandHandler());
		getServer().getPluginManager().registerEvents(new EventListener(this), this);
		this.actionbar = new Actionbar();
		/*File configFile = new File(getDataFolder(), "config.yml");
		//If newer version was found, update configuration.
		if (!getDescription().getVersion().equals(getConfig().getString("version", "old"))) {
			if (configFile.exists())
				configFile.delete();
		}
		*/
		saveDefaultConfig();
		DefaultPlaceholders.register();
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

	public Actionbar getActionbar() {
		return actionbar;
	}

}
