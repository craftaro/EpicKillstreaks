package me.limeglass.killstreaks.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.limeglass.killstreaks.Killstreaks;

public class Utils {
	
	public static boolean compareArrays(String[] arg1, String[] arg2) {
		if (arg1.length != arg2.length) return false;
		Arrays.sort(arg1);
		Arrays.sort(arg2);
		return Arrays.equals(arg1, arg2);
	}
	
	public static Boolean isEmpty(Inventory inventory) {
		for (ItemStack item : inventory.getContents()) {
			if (item != null && item.getType() != Material.AIR) {
				return false;
			}
		}
		return true;
	}
	
	private static JarFile getJar(JavaPlugin plugin) {
		try {
			Method method = JavaPlugin.class.getDeclaredMethod("getFile");
			method.setAccessible(true);
			File file = (File) method.invoke(plugin);
			return new JarFile(file);
		} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void loadClasses(JavaPlugin plugin, String basePackage, String... subPackages) {
		if (subPackages == null)
			return;
		for (int i = 0; i < subPackages.length; i++) {
			subPackages[i] = subPackages[i].replace('.', '/') + "/";
		}
		JarFile jar = getJar(plugin);
		basePackage = basePackage.replace('.', '/') + "/";
		try {
			for (Enumeration<JarEntry> jarEntry = jar.entries(); jarEntry.hasMoreElements();) {
				String name = jarEntry.nextElement().getName();
				if (name.startsWith(basePackage) && name.endsWith(".class")) {
					for (String sub : subPackages) {
						if (name.startsWith(sub, basePackage.length())) {
							String clazz = name.replace("/", ".").substring(0, name.length() - 6);
							Class.forName(clazz, true, plugin.getClass().getClassLoader());
						}
					}
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				jar.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Boolean isEnum(Class<?> clazz, String object) {
		try {
			final Method method = clazz.getMethod("valueOf", String.class);
			method.setAccessible(true);
			method.invoke(clazz, object.replace("\"", "").trim().replace(" ", "_").toUpperCase());
			return true;
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException error) {
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getEnum(Class<?> clazz, String object) {
		try {
			final Method method = clazz.getMethod("valueOf", String.class);
			method.setAccessible(true);
			return (T) method.invoke(clazz, object.replace("\"", "").trim().replace(" ", "_").toUpperCase());
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException error) {
			Killstreaks.consoleMessage("&cUnknown type " + object + " in " + clazz.getName());
			return null;
		}
	}
	
	public static Set<String> getEnums(Class<?> clazz) {
		try {
			final Method method = clazz.getMethod("values");
			method.setAccessible(true);
			Set<String> enums = new HashSet<String>();
			for (Object object : (Object[]) method.invoke(clazz)) {
				enums.add(object.toString());
			}
			return enums;
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException error) {
			return null;
		}
	}
	
	public static Class<?> getArrayClass(Class<?> parameter) {
		return Array.newInstance(parameter, 0).getClass();
	}

	public static String cc(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}

}
