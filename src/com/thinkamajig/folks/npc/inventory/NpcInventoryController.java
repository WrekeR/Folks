package com.thinkamajig.folks.npc.inventory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;

import com.thinkamajig.folks.Folks;


public class NpcInventoryController {
	
	private static FileConfiguration npcInvCfg = null;
	private static File npcInvCfgFile = null;
	
	private static ArrayList<NpcInventory> npcInventories = new ArrayList<NpcInventory>();
	
	/**
	 * Initialize Npcs & NpcsTypes
	 * 
	 * @param svr
	 * 		The Server
	 */
	public static void init() {
		getNpcInvCfg();
		
		// create NpcInventories
		for(String key : npcInvCfg.getKeys(false)) {
			boolean created = createNpcInventory(key);
			
			if(created) {
				System.out.println("[" + Folks.pluginName.toUpperCase() + "] [INVENTORY] '" +
						key + "' created.");
			} else {
				System.out.println("[" + Folks.pluginName.toUpperCase() + "] [ERROR] " +
						"Could not create Inventory '" + key + "'.");
			}
		}
	}
	
	public static boolean createNpcInventory(String id) {
		List<Map<String, Object>> items = npcInvCfg.getMapList(id);
		
		if(items != null) {
			ArrayList<ItemStack> itemsPacked = new ArrayList<ItemStack>();
			ItemStack itemstack;
			int itemId;
			int amount;
			byte byteData;
			
			for(Map<?, ?> item : items) {
				// split up the item string, so that we can get values
				String[] itemIdStringArr = item.get("id").toString().split("-");
				
				itemId = Integer.parseInt(itemIdStringArr[0]);
				amount = Integer.parseInt(item.get("amount").toString());
				
				if(itemIdStringArr.length == 2) {
					byteData = (byte) Byte.valueOf(itemIdStringArr[1]);
					
					itemstack = new ItemStack(itemId, amount, (short)0, byteData);
				} else {
					itemstack = new ItemStack(itemId, amount);
				}
				
				// add to our array
				itemsPacked.add(itemstack);
			}
			
			NpcInventory inv = new NpcInventory(id, itemsPacked);
			
			npcInventories.add(inv);

			return true;
		}
		
		return false;
	}
	
	public static boolean updateNpcInventory(String id, String itemId, int amount) {
		ArrayList<ItemStack> items = getNpcInvById(id).getItems();
		
		for(ItemStack item : items) {
			if(item.getData().equals(getItemMaterial(itemId))) {
				item.setAmount(amount);
				
				saveNpcInventory(id);
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Saves the Inventory
	 */
	private static void saveNpcInventory(String id) {		
		List<Map<String, Object>> cfgInv = npcInvCfg.getMapList(id);
		
		for(Map<String, Object> item : cfgInv) {
			String itemId = item.get("id").toString();
			
			String amount = String.valueOf(getNpcInvById(id).getItemAmount(itemId));
			
			item.put("amount", amount);
		}
			
		npcInvCfg.set(id, cfgInv);
		
		saveNpcInvCfg();
	}
	
	public static NpcInventory getNpcInvById(String id) {
		for(NpcInventory inv : npcInventories) {
			if(inv.getId().equals(id)) {
				return inv;
			}
		}
		
		return null;
	}
	
	public static String makeItemId(ItemStack item) {
		int id = item.getTypeId();
		byte data = item.getData().getData();
		
		if(data != 0) {
			return id + "-" + data;
		}
		
		return String.valueOf(id);
	}
	
	public static String[] getItemIdValues(String item) {
		return item.split("-");
	}
	
	public static MaterialData getItemMaterial(String item) {
		String[] itemIdVals = getItemIdValues(item);
		
		if(itemIdVals.length == 2) {
			return new MaterialData(Integer.parseInt(itemIdVals[0]), Byte.parseByte(itemIdVals[1]));
		}
		
		return new MaterialData(Integer.parseInt(itemIdVals[0]));
	}
	
	/**
	 * Returns the item name
	 * 
	 * @param inv
	 * 		The inventory
	 * @param id
	 * 		The id
	 * @return 
	 * 		The Name as String
	 */
	public static String getItemName(NpcInventory inv, String id) {
		if(npcInvCfg == null) {
			getNpcInvCfg();
		}
		
		List<Map<String, Object>> items = npcInvCfg.getMapList(inv.getId());
		
		if(items != null) {
			for(Map<String, Object> item : items) {
				String itemId = item.get("id").toString();
				
				if(itemId.equals(id)) {
					return item.get("name").toString();
				}
			}
		}

		return null;
	}
	
	/**
	 * Gets the item costs
	 * 
	 * @param inv
	 * 		The inventory
	 * @param id
	 * 		The id
	 * @return 
	 * 		The costs or -1 when an error occurs
	 */
	public static int getItemCosts(NpcInventory inv, String id) {
		if(npcInvCfg == null) {
			getNpcInvCfg();
		}
		
		List<Map<String, Object>> items = npcInvCfg.getMapList(inv.getId());
		
		if(items != null) {
			for(Map<String, Object> item : items) {
				String itemId = item.get("id").toString();
				
				if(itemId.equals(id)) {
					return Integer.parseInt(item.get("costs").toString());
				}
			}
		}

		return -1;
	}
	
	/**
	 * Gets the item income
	 * 
	 * @param inv
	 * 		The inventory
	 * @param id
	 * 		The id
	 * @return 
	 * 		The income
	 */
	public static int getItemIncome(NpcInventory inv, String id) {
		int costs = getItemCosts(inv, id);
		
		return costs - (int)Math.ceil(costs * 0.1);
	}
	
	/**
	 * Reloads the Npc config
	 */
	public static void reloadNpcInvCfg() {
		if (npcInvCfgFile == null) {
			npcInvCfgFile = new File(Bukkit.getPluginManager().getPlugin(Folks.pluginName).getDataFolder(), "npc/inventory.yml");
		}
	    
		npcInvCfg = YamlConfiguration.loadConfiguration(npcInvCfgFile);
		
		// Look for defaults in jar
		InputStream defaultNpcStream = Bukkit.getPluginManager().getPlugin(Folks.pluginName).getResource("config/npc/inventory.yml");
	    if (defaultNpcStream != null) {
	    	YamlConfiguration defaultNpcCfg = YamlConfiguration.loadConfiguration(defaultNpcStream);
	    	npcInvCfg.setDefaults(defaultNpcCfg);
	    }
	}
	
	/**
	 * Gets the Npc config
	 * 
	 * @return The Npc config 
	 * 		as FileConfiguration
	 */
	public static FileConfiguration getNpcInvCfg() {
		if (npcInvCfg == null) {
			reloadNpcInvCfg();
		}
		
		return npcInvCfg;
	}
	
	/**
	 * Saves the Npc config
	 */
	public static void saveNpcInvCfg() {
		if (npcInvCfg == null || npcInvCfgFile == null) {
			return;
		}
		
		try {
			npcInvCfg.save(npcInvCfgFile);
		} catch (IOException e) {
			Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Could not save " + npcInvCfgFile, e);
		}
	}
}