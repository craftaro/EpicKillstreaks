package me.limeglass.killstreaks.checks;

import java.util.logging.Level;
import java.util.stream.Stream;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import me.limeglass.killstreaks.Killstreaks;
import me.limeglass.killstreaks.objects.KillstreakCheck;
import me.limeglass.killstreaks.utils.CheckReader;

public class ArmorCheck extends KillstreakCheck {

	static {
		registerCheck(new ArmorCheck());
	}

	@Override
	public boolean check(EntityDamageByEntityEvent event) {
		CheckReader reader = new CheckReader("armor-blacklist");
		if (reader.isValid()) {
			Player attacker = (Player)event.getDamager();
			Stream<Material> stream = reader.getList().parallelStream()
					.map(string -> {
						Material material = Material.DIAMOND_CHESTPLATE;
						try {
							material = Material.valueOf(string.toUpperCase());
						} catch (Exception e) {
							Killstreaks.getInstance().getLogger().log(Level.SEVERE, "There was no Material found under the name: " + string);
						}
						return material;
					});
			if (reader.getConfigurationSection().getBoolean("match-exact", false)) {
				return stream
						.allMatch(material -> {
							for (ItemStack item : attacker.getEquipment().getArmorContents()) {
								if (item.getType() == material)
									return true;
							}
							return false;
						});
			} else {
				boolean contains = stream
						.anyMatch(material -> {
							for (ItemStack item : attacker.getEquipment().getArmorContents()) {
								if (item.getType() == material)
									return true;
							}
							return false;
						});
				return reader.isWhitelist() ? contains : !contains;
			}
		}
		return true;
	}
	
}
