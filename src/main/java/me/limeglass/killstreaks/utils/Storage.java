package me.limeglass.killstreaks.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.bukkit.Bukkit;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import me.limeglass.killstreaks.Killstreaks;

public class Storage {

	private final Map<String, Object> data = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	private final String DELIMITER = ": ";
	private final String NEW_LINE = "\n";
	private final File file, backups;
	private FileWriter writer;
	private final Gson gson;
	private boolean loading;

	public Storage(Killstreaks instance) {
		this.backups = new File(instance.getDataFolder() + File.separator + "backups");
		if (!backups.exists())
			backups.mkdir();
		this.file = new File(instance.getDataFolder(), "storage.csv");
		this.gson = new GsonBuilder()
				.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
				.create();
		Bukkit.getScheduler().runTaskTimerAsynchronously(instance, new Runnable() {
			@Override
			public void run() {
				Killstreaks.consoleMessage("Data has been saved!");
				save(true);
			}
		}, 1, (20 * 60) * instance.getConfig().getInt("database.backup-timer", 60)); //60 minutes by default
		if (file.exists()) {
			load();
			return;
		}
		try {
			writer = new FileWriter(file);
			writer.append(NEW_LINE);
			writer.append("# Killstreaks flat file database.");
			writer.append(NEW_LINE);
			writer.append("# Please do not modify this file manually, thank you!");
			writer.append(NEW_LINE);
			writer.append(NEW_LINE);
			Killstreaks.debugMessage("Successfully created CSV storage database!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.flush();
			} catch (IOException e) {
				Killstreaks.debugMessage("Error flushing data during setup!");
				e.printStackTrace();
			}
		}
	}

	public void close() {
		save(false);
	}

	public Integer getSize() {
		return data.size();
	}

	public Object get(String ID) {
		return data.get(ID);
	}

	public FileWriter getWriter() {
		return writer;
	}

	public void save(boolean reboot) {
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Date date = new Date();
		File newFile = new File(backups, date.toString().replaceAll(":", "-") + ".csv");
		try {
			Files.copy(file.toPath(), newFile.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (reboot) load();
	}
	
	private void load() {
		String line = "";
		BufferedReader reader = null;
		try {
			List<String[]> data = new ArrayList<>();
			reader = new BufferedReader(new FileReader(file));
			for (int i = 0; i < 4; i ++) {
				reader.readLine();
			}
			while ((line = reader.readLine()) != null) {
				String[] values = line.split(DELIMITER, 2);
				data.add(values);
			}
			writer = new FileWriter(file);
			writer.append(NEW_LINE);
			writer.append("# Killstreaks flat file database.");
			writer.append(NEW_LINE);
			writer.append("# Please do not modify this file manually, thank you!");
			writer.append(NEW_LINE);
			writer.append(NEW_LINE);
			for (String[] varaibleData : data) {
				write(varaibleData[0], gson.fromJson(varaibleData[1], Object.class));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				Killstreaks.debugMessage("Error closing reader while loading!");
				e.printStackTrace();
			}
		}
	}
	
	private void loadFromHash() {
		loading = true;
		try {
			writer = new FileWriter(file);
			writer.append(NEW_LINE);
			writer.append("# Killstreaks flat file database.");
			writer.append(NEW_LINE);
			writer.append("# Please do not modify this file manually, thank you!");
			writer.append(NEW_LINE);
			writer.append(NEW_LINE);
			if (!data.isEmpty()) {
				for (String ID : data.keySet()) {
					write(ID, data.get(ID));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.flush();
			} catch (IOException e) {
				Killstreaks.debugMessage("Error flushing writer while loading from hash!");
				e.printStackTrace();
			}
		}
		loading = false;
	}
	
	public void remove(String ID) {
		if (!data.containsKey(ID))
			return;
		if (loading)
			return;
		try {
			getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		data.remove(ID);
		loadFromHash();
	}
	
	public void write(String ID, Object value) {
		if (ID == null || value == null)
			return;
		if (data.containsKey(ID)) {
			if (!loading) {
				try {
					getWriter().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				data.remove(ID);
				loadFromHash();
			}
		}
		data.put(ID, value);
		try {
			writer.append(ID);
			writer.append(DELIMITER);
			writer.append(gson.toJson(value));
			writer.append(NEW_LINE);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.flush();
			} catch (IOException e) {
				Killstreaks.debugMessage("Error flushing data while writing!");
				e.printStackTrace();
			}
		}
	}

}
