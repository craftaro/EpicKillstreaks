package me.limeglass.killstreaks.objects;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.PluginManager;

import me.limeglass.killstreaks.managers.SubtractorManager;

public abstract class KillstreakSubtractor extends KillstreakElement {

	private final PluginManager pluginManager;
	protected Killstreak killstreak;
	private final Player player;
	
	public KillstreakSubtractor(Player player) {
		this.pluginManager = instance.getServer().getPluginManager();
		this.player = player;
	}
	
	protected static void registerSubtractor(String name, Class<? extends KillstreakSubtractor> subtractor) {
		SubtractorManager.registerSubtractor(name, subtractor);
	}
	
	@SafeVarargs
	protected final void registerExecutor(EventExecutor executor, Class<? extends Event>... events) {
		for (Class<? extends Event> event : events) {
			pluginManager.registerEvent(event, new Listener(){}, EventPriority.LOW, executor, instance);
		}
	}
	
	/**
	 * Interrupt the ongoing subtractor. Called to refresh the timer.
	 * May happen when the subtractor hasn't returned as finished yet.
	 * 
	 * @param event The event where the Killstreak took place.
	 */
	public abstract void interrupt(EntityDamageByEntityEvent event);
	
	/**
	 * Called when a subtractor should start monitoring a Killstreak.
	 * Keep in mind this is called asynchronously.
	 * 
	 * @param event The event where the Killstreak took place.
	 */
	public abstract void onStart(EntityDamageByEntityEvent event);
	
	public void setKillstreak(Killstreak killstreak) {
		this.killstreak = killstreak;
	}
	
	public Killstreak getKillstreak() {
		return killstreak;
	}
	
	/**
	 * The player this subtractor is allocated to.
	 * 
	 * @return Player involved with this subtractor.
	 */
	public Player getPlayer() {
		return player;
	}
	
	protected void finish() {
		SubtractorManager.finish(this);
	}

}
