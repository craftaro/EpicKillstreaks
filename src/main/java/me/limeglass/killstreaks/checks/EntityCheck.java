package me.limeglass.killstreaks.checks;

import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.limeglass.killstreaks.objects.KillstreakCheck;
import me.limeglass.killstreaks.utils.CheckReader;

public class EntityCheck extends KillstreakCheck {

	static {
		registerCheck(new EntityCheck());
	}

	@Override
	public boolean check(EntityDamageByEntityEvent event) {
		CheckReader reader = new CheckReader("entity-blacklist");
		if (reader.isValid() && reader.isEnabled()) {
			boolean contains = reader.getList().parallelStream()
					.map(string -> EntityType.valueOf(string.toUpperCase()))
					.anyMatch(type -> event.getEntityType() == type);
			return reader.isWhitelist() ? contains : !contains;
		}
		return true;
	}
	
}
