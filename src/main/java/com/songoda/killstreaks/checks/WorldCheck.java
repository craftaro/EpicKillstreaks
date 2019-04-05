package com.songoda.killstreaks.checks;

import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.songoda.killstreaks.objects.KillstreakCheck;
import com.songoda.killstreaks.utils.CheckReader;

public class WorldCheck extends KillstreakCheck {

	static {
		registerCheck(new WorldCheck());
	}

	@Override
	public boolean check(EntityDamageByEntityEvent event) {
		CheckReader reader = new CheckReader("world-blacklist", configuration);
		if (reader.isValid() && reader.isEnabled()) {
			boolean contains = reader.getList().parallelStream()
					.anyMatch(name -> event.getDamager().getWorld().getName().equalsIgnoreCase(name));
			return reader.isWhitelist() ? contains : !contains;
		}
		return true;
	}
	
}
