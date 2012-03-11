package com.thinkamajig.folks;

import java.io.File;
import java.io.InputStream;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public class Config {
	
	private static FileConfiguration cfg = null;
	private static File cfgFile = null;
	
	public static String getLang() {
		if(cfg == null) {
			getConfig();
		}
		
		return cfg.getString("lang", "default");
	}
	
	/**
	 * Reloads the config
	 */
	private static void reloadConfig() {
		if (cfgFile == null) {
			cfgFile = new File(Bukkit.getPluginManager().getPlugin(Folks.pluginName).getDataFolder(), "config.yml");
		}

		cfg = YamlConfiguration.loadConfiguration(cfgFile);

		// Look for defaults in the jar
		InputStream defaultCfgStream = Bukkit.getPluginManager().getPlugin(Folks.pluginName).getResource("config.yml");
	    if (defaultCfgStream != null) {
	    	YamlConfiguration defaultCfg = YamlConfiguration.loadConfiguration(defaultCfgStream);
	    	cfg.setDefaults(defaultCfg);
	    }
	}
	
	/**
	 * Gets the config
	 * 
	 * @return The traders configuration as FileConfiguration
	 */
	private static FileConfiguration getConfig() {
		if (cfg == null) {
			reloadConfig();
		}
		
		return cfg;
	}
}
