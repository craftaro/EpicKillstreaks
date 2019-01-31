package me.limeglass.killstreaks.utils;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import me.limeglass.killstreaks.Killstreaks;

public class CheckReader {

	private final FileConfiguration configuration;
	private final ConfigurationSection section;
	private final boolean valid;
	private final String node;
	
	public CheckReader(String node) {
		this.configuration = Killstreaks.getInstance().getConfig();
		this.section = configuration.getConfigurationSection(node);
		this.valid = configuration.isConfigurationSection(node);
		this.node = node;
	}
	
	public ConfigurationSection getConfigurationSection() {
		return section;
	}
	
	public FileConfiguration getConfiguration() {
		return configuration;
	}
	
	public List<String> getList() {
		return section.getStringList("list");
	}
	
	public boolean isWhitelist() {
		return section.getBoolean("whitelist", false);
	}
	
	public boolean isEnabled() {
		return section.getBoolean("enabled", false);
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public String getNode() {
		return node;
	}
	
}
