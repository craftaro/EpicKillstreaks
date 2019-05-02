package com.songoda.killstreaks.actions;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.songoda.killstreaks.managers.VaultManager;
import com.songoda.killstreaks.objects.Killstreak;
import com.songoda.killstreaks.objects.KillstreakAction;

public class EconomyAction extends KillstreakAction {

	static {
		registerAction(new EconomyAction());
	}

	private final VaultManager vaultManager;

	public EconomyAction() {
		super("Economy");
		vaultManager = instance.getVaultManager();
	}

	@Override
	public void onKillstreak(EntityDamageByEntityEvent event, Killstreak killstreak) {
		if (vaultManager == null || vaultManager.getEconomy() == null)
			return;
		if (!configuration.isConfigurationSection("effects.economy"))
			return;
		ConfigurationSection section = configuration.getConfigurationSection("effects.economy");
		if (!section.getBoolean("enabled", false))
			return;
		Player attacker = killstreak.getPlayer();
		Entity victim = event.getEntity();
		for (String node : section.getConfigurationSection("attacker-streaks").getKeys(false)) {
			int streak = Integer.parseInt(node);
			if (streak <= 0)
				continue;
			if (killstreak.getStreak() != streak)
				continue;
			int money = section.getInt("attacker-streaks." + node, 25);
			vaultManager.deposit(attacker, money);
			if (section.getBoolean("victim-remove", false) && victim instanceof Player) {
				Player victimPlayer = (Player) victim;
				money = section.getInt("victim-streaks." + node, 25);
				vaultManager.withdraw(victimPlayer, money);
			}
		}
	}

}
