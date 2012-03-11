package com.thinkamajig.folks.gui;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.thinkamajig.folks.Config;
import com.thinkamajig.folks.Folks;

public class Text {
	
	private static FileConfiguration textCfg = null;
	private static File textCfgFile = null;
	
	private static FileConfiguration defaultTextCfg = null;
	private static File defaultTextCfgFile = null;
	
	/**
	 * Returns a Message
	 * 
	 * @param id
	 * 		The id of the message
	 * @return 
	 * 		The Text as String
	 */
	public static String getText(String id) {
		if(textCfg == null) {
			getTextCfg();
		}
		
		String text = textCfg.getString(id);
		
		if(text == null) {
			if(defaultTextCfg == null) {
				getDefaultTextCfg();
			}
			
			text = defaultTextCfg.getString(id);
		}
		
		return text;
	}
	
	/**
	 * Gets a random Message of the specified List
	 * 
	 * @param id
	 * 		The id where the random message should be picked from
	 * @return 
	 * 		The Text as String
	 */
	public static String getRandomText(String id) {		
		if(textCfg == null) {
			getTextCfg();
		}

		List<String> list = textCfg.getStringList(id);
		
		if(list == null) {
			if(defaultTextCfg == null) {
				getDefaultTextCfg();
			}
			
			list = defaultTextCfg.getStringList(id);
		}
		
		// return random message
		return list.get((int)Math.round(Math.random()*(list.size()-1)));
	}
	
	/**
	 * Reloads the TradersMsg Configuration
	 */
	private static void reloadTextCfg() {
		if (textCfgFile == null) {
			textCfgFile = new File(Bukkit.getPluginManager().getPlugin(Folks.pluginName).getDataFolder(), "lang/"+Config.getLang()+".yml");
		}

		textCfg = YamlConfiguration.loadConfiguration(textCfgFile);
	}
	
	/**
	 * Gets the TradersMsg Cfg
	 * 
	 * @return 
	 * 		Text Configuration as FileConfiguration
	 */
	private static FileConfiguration getTextCfg() {
		if (textCfg == null) {
			reloadTextCfg();
		}
		
		return textCfg;
	}
	
	/**
	 * Reloads the TradersMsg Configuration
	 */
	private static void reloadDefaultTextCfg() {
		if (defaultTextCfgFile == null) {
			defaultTextCfgFile = new File(Bukkit.getPluginManager().getPlugin(Folks.pluginName).getDataFolder(), "lang/default.yml");
		}

		defaultTextCfg = YamlConfiguration.loadConfiguration(defaultTextCfgFile);

		// Look for defaults in the jar
		InputStream defaultTextCfgStream = Bukkit.getPluginManager().getPlugin(Folks.pluginName).getResource("config/lang/default.yml");
	    if (defaultTextCfgStream != null) {
	    	YamlConfiguration defaultTextCfg = YamlConfiguration.loadConfiguration(defaultTextCfgStream);
	    	textCfg.setDefaults(defaultTextCfg);
	    }
	}
	
	/**
	 * Gets the TradersMsg Cfg
	 * 
	 * @return 
	 * 		Text Configuration as FileConfiguration
	 */
	private static FileConfiguration getDefaultTextCfg() {
		if (defaultTextCfg == null) {
			reloadDefaultTextCfg();
		}
		
		return defaultTextCfg;
	}
}
