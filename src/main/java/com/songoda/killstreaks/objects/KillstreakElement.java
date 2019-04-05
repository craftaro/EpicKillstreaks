package com.songoda.killstreaks.objects;

import org.bukkit.configuration.file.FileConfiguration;

import com.songoda.killstreaks.Killstreaks;

public abstract class KillstreakElement {

	protected final FileConfiguration configuration;
	protected final Killstreaks instance;
	
	public KillstreakElement() {
		this.instance = Killstreaks.getInstance();
		this.configuration = instance.getConfig();
	}
	
	public FileConfiguration getConfiguration() {
		return configuration;
	}

}
