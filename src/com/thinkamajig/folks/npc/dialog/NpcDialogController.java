package com.thinkamajig.folks.npc.dialog;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.thinkamajig.folks.Folks;


public class NpcDialogController {
	
	private static FileConfiguration npcDialogCfg = null;
	private static File npcDialogCfgFile = null;
		
	public static NpcDialog createNpcDialog(String id) {
		if(npcDialogCfg == null) {
			getNpcDialogCfg();
		}
		
		if(npcDialogCfg.contains(id)) {
			String text = npcDialogCfg.getString(id+".text");
			ArrayList<NpcDialogAwnser> awnsers = createAwnsers(id);

			return new NpcDialog(id, text, awnsers);
		}
		
		return null;
	}
	
	private static ArrayList<NpcDialogAwnser> createAwnsers(String path) {
		ArrayList<NpcDialogAwnser> awnsers = new ArrayList<NpcDialogAwnser>();
		
		ConfigurationSection awnsersPath = npcDialogCfg.getConfigurationSection(path+".awnsers");
		
		if(awnsersPath != null) {
			Set<String> cfgAwnsers = awnsersPath.getKeys(false);			
			
			for(String cfgAwnser : cfgAwnsers) {
				String title = npcDialogCfg.getString(path+".awnsers."+cfgAwnser+".title");
				String text = npcDialogCfg.getString(path+".awnsers."+cfgAwnser+".text");
			
				awnsers.add(new NpcDialogAwnser(title, text, createAwnsers(path+".awnsers."+cfgAwnser)));
			}
		}		
		
		return awnsers;
	}

	/**
	 * Reloads the NpcDialog config
	 */
	private static void reloadNpcDialogCfg() {
		if (npcDialogCfgFile == null) {
			npcDialogCfgFile = new File(Bukkit.getPluginManager().getPlugin(Folks.pluginName).getDataFolder(), "npc/dialog.yml");
		}
	    
		npcDialogCfg = YamlConfiguration.loadConfiguration(npcDialogCfgFile);
		
		// Look for defaults in jar
		InputStream defaultNpcDialogStream = Bukkit.getPluginManager().getPlugin(Folks.pluginName).getResource("dialog.yml");
	    if (defaultNpcDialogStream != null) {
	    	YamlConfiguration defaultNpcDialogCfg = YamlConfiguration.loadConfiguration(defaultNpcDialogStream);
	    	npcDialogCfg.setDefaults(defaultNpcDialogCfg);
	    }
	}
	
	/**
	 * Gets the NpcDialog config
	 * 
	 * @return The Npc config 
	 * 		as FileConfiguration
	 */
	private static FileConfiguration getNpcDialogCfg() {
		if (npcDialogCfg == null) {
			reloadNpcDialogCfg();
		}
		
		return npcDialogCfg;
	}
}