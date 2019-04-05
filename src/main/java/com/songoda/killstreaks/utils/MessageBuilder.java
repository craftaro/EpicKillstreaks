package com.songoda.killstreaks.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.google.common.collect.Sets;
import com.songoda.killstreaks.Killstreaks;
import com.songoda.killstreaks.objects.Killstreak;
import com.songoda.killstreaks.placeholders.Placeholder;
import com.songoda.killstreaks.placeholders.Placeholders;
import com.songoda.killstreaks.placeholders.SimplePlaceholder;

public class MessageBuilder {

	private Map<Placeholder<?>, Object> placeholders = new HashMap<>();
	private final List<CommandSender> senders = new ArrayList<>();
	private Object defaultPlaceholderObject;
	private ConfigurationSection section;
	private final Killstreaks instance;
	private Killstreak killstreak;
	private String complete;
	private String[] nodes;
	
	/**
	 * Creates a MessageBuilder with the defined nodes and ConfigurationSection.
	 * 
	 * @param nodes The configuration nodes from the config.yml
	 */
	public MessageBuilder(ConfigurationSection section, String... nodes) {
		this.instance = Killstreaks.getInstance();
		this.section = section;
		this.nodes = nodes;
	}
	
	/**
	 * Set the players to send this message to.
	 *
	 * @param senders The Players... to send the message to.
	 * @return The MessageBuilder for chaining.
	 */
	public MessageBuilder toSenders(CommandSender... senders) {
		this.senders.addAll(Sets.newHashSet(senders));
		return this;
	}
	
	/**
	 * Set the players to send this message to.
	 *
	 * @param senders The Collection<Player> to send the message to.
	 * @return The MessageBuilder for chaining.
	 */
	public MessageBuilder toSenders(Collection<? extends CommandSender> senders) {
		this.senders.addAll(senders);
		return this;
	}
	
	/**
	 * Add a placeholder to the MessageBuilder.
	 * 
	 * @param placeholderObject The object to be determined in the placeholder.
	 * @param placeholder The actual instance of the Placeholder.
	 * @return The MessageBuilder for chaining.
	 */
	public MessageBuilder withPlaceholder(Object placeholderObject, Placeholder<?> placeholder) {
		this.defaultPlaceholderObject = placeholderObject;
		placeholders.put(placeholder, placeholderObject);
		return this;
	}
	
	/**
	 * Created a single replacement and ignores the placeholder object.
	 * 
	 * @param syntax The syntax to check within the messages e.g: %command%
	 * @param replacement The replacement e.g: the command.
	 * @return The MessageBuilder for chaining.
	 */
	public MessageBuilder replace(String syntax, Object replacement) {
		placeholders.put(new SimplePlaceholder(syntax) {
			@Override
			public String get() {
				return replacement.toString();
			}
		}, replacement.toString());
		return this;
	}
	
	/**
	 * Set the configuration nodes from messages.yml
	 *
	 * @param nodes The nodes to use.
	 * @return The MessageBuilder for chaining.
	 */
	public MessageBuilder setNodes(String... nodes) {
		this.nodes = nodes;
		return this;
	}
	
	/**
	 * Set the placeholder object, good if you want to allow multiple placeholders.
	 * 
	 * @param object The object to set
	 * @return The MessageBuilder for chaining.
	 */
	public MessageBuilder setPlaceholderObject(Object object) {
		this.defaultPlaceholderObject = object;
		return this;
	}
	
	/**
	 * Set the Killstreak option to be used for placeholders later.
	 * 
	 * @param killstreak The Killstreak to set as.
	 * @return The MessageBuilder for chaining.
	 */
	public MessageBuilder setKillstreak(Killstreak killstreak) {
		this.killstreak = killstreak;
		return this;
	}
	
	/**
	 * Sends the message as an actionbar to the defined players.
	 * 
	 * @param players the players to send to
	 */
	public void sendActionbar(Player... players) {
		toSenders(players).sendActionbar();
	}
	
	/**
	 * Sends the message as a title to the defined players.
	 * 
	 * @param players the players to send to
	 */
	public void sendTitle(Player... players) {
		toSenders(players).sendTitle();
	}
	
	/**
	 * Sends the final product of the builder.
	 */
	public void send(CommandSender... senders) {
		toSenders(senders).send();
	}
	
	/**
	 * Completes and returns the final product of the builder.
	 */
	public String get() {
		complete = Formatting.messages(section, nodes);
		complete = applyPlaceholders(complete);
		return complete;
	}
	
	private String applyPlaceholders(String input) {
		// Default Placeholders
		for (Placeholder<?> placeholder : Placeholders.getPlaceholders()) {
			for (String syntax : placeholder.getSyntaxes()) {
				if (placeholder instanceof SimplePlaceholder) {
					SimplePlaceholder simple = (SimplePlaceholder) placeholder;
					input = input.replaceAll(Pattern.quote(syntax), simple.get());
				} else if (defaultPlaceholderObject != null) {
					if (placeholder.getType().isAssignableFrom(defaultPlaceholderObject.getClass()))
						input = input.replaceAll(Pattern.quote(syntax), placeholder.replace_i(defaultPlaceholderObject));
				}
				if (killstreak != null) {
					if (placeholder.getType().isAssignableFrom(Killstreak.class))
						input = input.replaceAll(Pattern.quote(syntax), placeholder.replace_i(killstreak));
				}
			}
		}
		// Registered Placeholders
		for (Entry<Placeholder<?>, Object> entry : placeholders.entrySet()) {
			Placeholder<?> placeholder = entry.getKey();
			for (String syntax : placeholder.getSyntaxes()) {
				if (placeholder instanceof SimplePlaceholder) {
					SimplePlaceholder simple = (SimplePlaceholder) placeholder;
					input = input.replaceAll(Pattern.quote(syntax), simple.get());
				} else {
					input = input.replaceAll(Pattern.quote(syntax), placeholder.replace_i(entry.getValue()));
				}
			}
		}
		return input;
	}
	
	/**
	 * Sends the final product of the builder as a title if the players using toPlayers are set.
	 * 
	 * WARNING: The title method needs to have the following as a configuration, this is special.
	 * title:
	 * 	  enabled: false
	 * 	  title: "&2Example"
	 * 	  subtitle: "&5&lColors work too."
	 * 	  fadeOut: 20
	 * 	  fadeIn: 20
	 * 	  stay: 200
	 */
	public void sendTitle() {
		if (nodes.length != 1)
			return;
		if (!section.getBoolean(nodes[0] + ".enabled", false))
			return;
		String subtitle = section.getString(nodes[0] + ".subtitle", "");
		String title = section.getString(nodes[0] + ".title", "");
		int fadeOut = section.getInt(nodes[0] + ".fadeOut", 20);
		int fadeIn = section.getInt(nodes[0] + ".fadeIn", 20);
		int stay = section.getInt(nodes[0] + ".stay", 200);
		title = applyPlaceholders(title).replaceAll("\n", "");
		subtitle = applyPlaceholders(subtitle).replaceAll("\n", "");
		Player[] players = this.senders.parallelStream()
				.filter(sender -> sender instanceof Player)
				.toArray(Player[]::new);
		if (players != null && players.length > 0) {
			new Title.Builder()
					.subtitle(subtitle)
					.fadeOut(fadeOut)
					.fadeIn(fadeIn)
					.title(title)
					.stay(stay)
					.send(players);
		}
	}
	
	/**
	 * Sends the final product of the builder as an actionbar if the players using toPlayers are set.
	 */
	public void sendActionbar() {
		get();
		complete = complete.replaceAll("\n", "");
		Actionbar actionbar = instance.getActionbar();
		if (!senders.isEmpty()) {
			for (CommandSender sender : senders) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					actionbar.sendActionBar(player, complete);
				}
			}
		}
	}
	
	/**
	 * Sends the final product of the builder if the senders are set.
	 */
	public void send() {
		get();
		if (!senders.isEmpty())
			senders.forEach(player -> player.sendMessage(complete));
	}
	
}
