package com.songoda.killstreaks.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.entity.Player;

public class Actionbar {
	
	// Caching
	private final boolean classes;
	
	public Actionbar() {
		this.classes = Utils.classExists("net.md_5.bungee.api.ChatMessageType") && Utils.classExists("net.md_5.bungee.api.chat.TextComponent");
	}
	
	public void sendActionBar(Player player, String... messages) {
		if (classes) {
			for (String message : messages)
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Formatting.color(message)));
		}
	}

}
