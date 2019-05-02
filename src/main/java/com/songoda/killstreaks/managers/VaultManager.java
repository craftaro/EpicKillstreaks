package com.songoda.killstreaks.managers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.songoda.killstreaks.Killstreaks;

import net.milkbowl.vault.economy.Economy;

public class VaultManager {

	private Economy economy;

	public VaultManager(Killstreaks instance) {
		RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
		if (rsp != null)
			economy = rsp.getProvider();
	}

	public void withdraw(OfflinePlayer player, double amount) {
		if (economy != null)
			economy.withdrawPlayer(player, amount);
	}

	public void deposit(OfflinePlayer player, double amount) {
		if (economy != null)
			economy.depositPlayer(player, amount);
	}

	public double getBalance(OfflinePlayer player) {
		if (economy == null)
			return 0;
		return economy.getBalance(player);
	}

	public Economy getEconomy() {
		return economy;
	}

}
