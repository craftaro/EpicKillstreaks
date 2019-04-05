package com.songoda.killstreaks.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import com.songoda.killstreaks.Killstreaks;

public class Formatting {
	
	public static String messages(ConfigurationSection section, String... nodes) {
		FileConfiguration configuration = Killstreaks.getInstance().getConfig();
		String complete = "";
		List<String> list = Arrays.asList(nodes);
		Collections.reverse(list);
		for (String node : list.toArray(new String[list.size()])) {
			String string = configuration.getString(node, "Not set");
			if (section != null)
				string = section.getString(node, "Not set");
			complete = string + " " + complete;
		}
		return Formatting.color(complete);
	}

	
	public static String color(String input) {
		return ChatColor.translateAlternateColorCodes('&', input);
	}
	
	public static String colorAndStrip(String input) {
		return stripColor(color(input));
	}
	
	public static String stripColor(String input) {
		return ChatColor.stripColor(input);
	}
	
}
