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
import org.bukkit.Sound;
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
	
	/**
	 * Tests whether a given class exists in the classpath.
	 * 
	 * @author Skript team.
	 * @param className The {@link Class#getCanonicalName() canonical name} of the class
	 * @return Whether the given class exists.
	 */
	public static boolean classExists(String className) {
		try {
			Class.forName(className);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	/**
	 * Tests whether a method exists in the given class.
	 * 
	 * @author Skript team.
	 * @param c The class
	 * @param methodName The name of the method
	 * @param parameterTypes The parameter types of the method
	 * @return Whether the given method exists.
	 */
	public static boolean methodExists(Class<?> c, String methodName, Class<?>... parameterTypes) {
		try {
			c.getDeclaredMethod(methodName, parameterTypes);
			return true;
		} catch (NoSuchMethodException | SecurityException e) {
			return false;
		}
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
	
	public static Sound soundAttempt(String attempt, String fallback) {
		Sound sound = null;
		try {
			sound = Sound.valueOf(attempt);
		} catch (Exception e) {
			try {
				sound = Sound.valueOf(fallback);
			} catch (Exception e1) {}
		}
		if (sound == null)
			sound = Sound.ENTITY_PLAYER_LEVELUP;
		return sound;
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
