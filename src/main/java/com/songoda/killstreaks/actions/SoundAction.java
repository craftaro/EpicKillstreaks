package com.songoda.killstreaks.actions;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.songoda.killstreaks.objects.Killstreak;
import com.songoda.killstreaks.objects.KillstreakAction;
import com.songoda.killstreaks.utils.Utils;

public class SoundAction extends KillstreakAction {

	static {
		registerAction(new SoundAction());
	}

	public SoundAction() {
		super("Sound");
	}

	@Override
	public void onKillstreak(EntityDamageByEntityEvent event, Killstreak killstreak) {
		if (!configuration.isConfigurationSection("effects.sound"))
			return;
		Player attacker = killstreak.getPlayer();
		ConfigurationSection section = configuration.getConfigurationSection("effects.sound");
		int streak = killstreak.getStreak();
		if (!section.getBoolean("enabled", false))
			return;
		if (!section.getIntegerList("streaks").contains(streak))
			return;
		Sound sound = Utils.soundAttempt(section.getString("sound"), "click");
		float volume = section.getInt("volume") + (section.getInt("volume-addition") * streak);
		float pitch = section.getInt("pitch") + (section.getInt("pitch-addition") * streak);
		if (section.getBoolean("location", false)) {
			attacker.getWorld().playSound(attacker.getLocation(), sound, volume, pitch);
			return;
		}
		if (section.getBoolean("attacker-only", false)) {
			attacker.playSound(attacker.getLocation(), sound, volume, pitch);
			return;
		}
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.playSound(player.getLocation(), sound, volume, pitch);
		}
	}

}
