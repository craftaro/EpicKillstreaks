package me.limeglass.killstreaks.objects;

import org.bukkit.configuration.file.FileConfiguration;

public abstract class KillstreakElement {

	private FileConfiguration configuration;
	
	public void setConfiguration(FileConfiguration configuration) {
		this.configuration = configuration;
	}
	
	public FileConfiguration getConfiguration() {
		return configuration;
	}

}
