package com.thinkamajig.folks.npc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.thinkamajig.folks.Folks;
import com.thinkamajig.folks.npc.inventory.NpcInventory;
import com.thinkamajig.folks.npc.inventory.NpcInventoryController;
import com.thinkamajig.folks.npc.skill.Fighting;
import com.thinkamajig.folks.npc.skill.Healing;
import com.thinkamajig.folks.npc.skill.Invulnerable;
import com.thinkamajig.folks.npc.skill.Movement;
import com.thinkamajig.folks.npc.skill.NpcSkill;
import com.thinkamajig.folks.npc.skill.Repairing;
import com.thinkamajig.folks.npc.skill.Talking;
import com.thinkamajig.folks.npc.skill.Trading;


public class NpcController {
	
	private static FileConfiguration npcCfg = null;
	private static File npcCfgFile = null;
	
	private static FileConfiguration npcTypeCfg = null;
	private static File npcTypeCfgFile = null;
	
	private static ArrayList<Npc> npcs = new ArrayList<Npc>();
	private static ArrayList<NpcType> npcTypes = new ArrayList<NpcType>();
	private static ArrayList<NpcSkill> npcSkills = new ArrayList<NpcSkill>();
	private static ArrayList<ArrayList<NpcSkill>> npcSkillLists = new ArrayList<ArrayList<NpcSkill>>();
	
	private static int tries = 1;
	private static int maxTries = 5;
	
	/**
	 * Initialize Npcs & NpcsTypes
	 * 
	 * @param svr
	 * 		The Server
	 */
	public static void init(Server svr) {
		NpcInventoryController.init();
		
		getNpcTypeCfg();
		// create NpcTypes
		for(String key : npcTypeCfg.getKeys(false)) {
			createNpcType(key);
		}
		
		getNpcCfg();
		// create Npcs
		for(String key : npcCfg.getKeys(false)) {
			createNpc(key, svr);
		}
	}
	
	/**
	 * Creates a Npc
	 * 
	 * @param id
	 * 		Id of the Npc
	 * @param svr
	 * 		The Server
	 * @return
	 * 		True if the Npc was created,
	 * 		otherwise false.
	 */
	private static boolean createNpc(String id, Server svr) {		
		String name = npcCfg.getString(id+".name");
		String typeId = npcCfg.getString(id+".type");
		
		NpcType type = getNpcTypeById(typeId);
		
		if(type != null) {
			World w = svr.getWorld(npcCfg.getString(id+".location.world"));
			double x = npcCfg.getDouble(id+".location.x");
			double y = npcCfg.getDouble(id+".location.y");
			double z = npcCfg.getDouble(id+".location.z");
			Location loc = new Location(w, x, y, z);
			
			int entUuid = npcCfg.getInt(id+".entity", -1);
			
			if(entUuid == -1) {
				Npc npc = new Npc(id, name, loc, type, w.spawn(loc, type.getEntityClass()));
				npcs.add(npc);

				npcCfg.set(id+".entity", npc.getEntity().getUniqueId().hashCode());
				saveNpcCfg();
				
				System.out.println("[" + Folks.pluginName.toUpperCase() + "] [SPAWNED] " + name + " as " + 
							npc.getEntity().getUniqueId().hashCode() + " at " + w.getName() + 
							"(" + x + ", " + y + "," + z + ")");
				
				// npc spawned, so reset tries
				tries = 1;				
				
				return true;
			} else {
				System.out.println("[" + Folks.pluginName.toUpperCase() + "] [SEARCH] "
						+ name + " as " + entUuid);
				
				LivingEntity entity = getLivingEntityByUuid(w.getLivingEntities(),
						entUuid);
	
				if(entity != null) {
					Npc npc = new Npc(id, name, loc, type, entity);
					npcs.add(npc);
					
					entity.teleport(loc);
					
					System.out.println("[" + Folks.pluginName.toUpperCase() + "] [FOUND] "
							+ entUuid);
					
					// npc found, so reset tries
					tries = 1;
					
					return true;
				} else if(tries <= maxTries) {
					System.out.println("[" + Folks.pluginName.toUpperCase() + "] [ERROR] " +
							"No Npc with the specified UUID found.");
					System.out.println("[" + Folks.pluginName.toUpperCase() + "] [RESPAWN] " +
							"Trying to respawn " + name + ". Try " + tries + " of " + maxTries);
					
					// clear entity in cfg
					npcCfg.set(id+".entity", null);
					// save cfg so that the plugin recognizes
					// that there's no entity
					saveNpcCfg();
					
					// recreate Npc
					createNpc(id, svr);
				} else {
					tries = 1;
					System.out.println("[" + Folks.pluginName.toUpperCase() + "] [ERROR] " +
							"Couldn't respawn " + name);
				}
			}
		} else {
			System.out.println("[" + Folks.pluginName.toUpperCase() + "] [ERROR] " +
					"Bad NpcType id for " + name + ".");
		}
		
		return false;
	}
	
	public static void removeNpcs() {
		for(Npc npc : npcs) {
			// clear entity in cfg
			npcCfg.set(npc.getId()+".entity", null);
			
			npc.getEntity().remove();
		}
		
		saveNpcCfg();
		
		System.out.println("[" + Folks.pluginName.toUpperCase() + "] [INFO] " +
				"All Npc Entities deleted.");
	}
	
	/**
	 * Creates a NpcType
	 * 
	 * @param id
	 * 		The id of NpcType
	 * @return
	 */
	private static boolean createNpcType(String id) {
		String name = npcTypeCfg.getString(id+".name");
		int entityClassId = npcTypeCfg.getInt(id+".entityclass", -1);
		
		if(entityClassId != -1) {
			ArrayList<NpcSkill> skills = getNpcTypeSkills(id);
			
			NpcType type = new NpcType(id, name, skills, entityClassId);
			
			npcTypes.add(type);
			
			System.out.println("[" + Folks.pluginName.toUpperCase() + "] [TYPE] " +
					"NpcType '" + name + "' created.");
			
			return true;
		} else {
			System.out.println("[" + Folks.pluginName.toUpperCase() + "] [ERROR] " +
					"Bad entity for NpcType " + name + ".");
		}
				
		return false;
	}
	
	/**
	 * Gets a specific NpcType by Id
	 * 
	 * @param id
	 * 		The Id of the NpcType
	 * @return
	 * 		The NpcType with the specified Id.
	 * 		If no NpcType was found, null is returned
	 */
	public static NpcType getNpcTypeById(String id) {
		for(NpcType type : npcTypes) {
			if(type.getId().equals(id)) {
				return type;
			}
		}
		
		return null;
	}
	
	/**
	 * Gets a specific NpcType by Id
	 * 
	 * @param id
	 * 		The Id of the NpcType
	 * @return
	 * 		The NpcType with the specified Id.
	 * 		If no NpcType was found, null is returned
	 */
	public static ArrayList<NpcSkill> getNpcTypeSkills(String id) {
		ArrayList<NpcSkill> skillList = new ArrayList<NpcSkill>();
		
		List<Map<String, Object>> cfgSkillList = npcTypeCfg.getMapList(id+".skills");
		
		for(Map<?, ?> cfgSkill : cfgSkillList) {
			int skillId = npcSkills.size() + 1;
			String key = cfgSkill.get("id").toString();
			
			NpcSkill skill = new NpcSkill(skillId);			
			
			if(key.contains("Repairing")) {
				int costs = Integer.parseInt(cfgSkill.get("costs").toString());
				
				skill = new Repairing(skillId, costs);
			} else if(key.contains("Healing")) {
				skill = new Healing(skillId);
			} else if(key.contains("Trading")) {
				String invId = cfgSkill.get("inventory").toString();
				
				NpcInventory inv = NpcInventoryController.getNpcInvById(invId);
				
				skill = new Trading(skillId, inv);
			} else if(key.contains("Movement")) {
				boolean loop = npcTypeCfg.getBoolean(key+".loop", true);
				int delay = npcTypeCfg.getInt(key+".delay", -1);
				
				if(delay != -1) {
					skill = new Movement(skillId, loop, delay);
				} else {
					skill = new Movement(skillId, loop);
				}				
			} else if(key.contains("Talking")) {
				String dialog = cfgSkill.get("dialog").toString();
				skill = new Talking(skillId, dialog);
			} else if(key.contains("Invulnerable")) {
				skill = new Invulnerable(skillId);
			} else if(key.contains("Fighting")) {
				boolean selfDefense = npcTypeCfg.getBoolean(key+".self-defense", true);
				skill = new Fighting(skillId, selfDefense);
			}
			
			skillList.add(skill);
			npcSkills.add(skill);
		}
		
		npcSkillLists.add(skillList);
		
		return skillList;
	}
	
	/**
	 * Gets an LivingEntity by UUID
	 * 
	 * @param le
	 * 		List of LivingEntities
	 * @param uuidHash
	 * 		The Entity's UUID Hash
	 * @return The Entity as Living Entity
	 */
	private static LivingEntity getLivingEntityByUuid(List<LivingEntity> le, int uuidHash) {
		for(LivingEntity entity : le) {
			if(entity.getUniqueId().hashCode() == uuidHash) {
				return entity;
			}
		}

		return null;
	}
		
	/**
	 * Gets a Npc by UUID
	 * 
	 * @param le
	 * 		List of LivingEntities
	 * @param uuidHash
	 * 		The Entity's UUID Hash
	 * @return The Entity as Living Entity
	 */
	public static Npc getNpcByUuid(int uuidHash) {
		for(Npc npc : npcs) {
			LivingEntity entity = npc.getEntity();
			
			if(entity.getUniqueId().hashCode() == uuidHash) {
				return npc;
			}
		}

		return null;
	}
	
	/**
	 * Gets nearest Npc to a Player
	 * @param name
	 * 		Npc name
	 * @return Npc with the specified name
	 */
	public static Npc getNearestNpc(Player player) {
		List<Entity> surr = player.getNearbyEntities(2, 2, 2);
		
		for(Entity enty : surr) {
			if(enty instanceof LivingEntity) {
				Npc npc = getNpcByUuid(enty.getUniqueId().hashCode());
				if(npc != null) {
					return npc;
				}
			}
		}
		
		return null;
	}
	
	public static ArrayList<Npc> getNpcs() {
		return npcs;
	}
	
	/**
	 * Reloads the Npc config
	 */
	private static void reloadNpcCfg() {
		if (npcCfgFile == null) {
			npcCfgFile = new File(Bukkit.getPluginManager().getPlugin(Folks.pluginName).getDataFolder(), "npc/npc.yml");
		}
	    
		npcCfg = YamlConfiguration.loadConfiguration(npcCfgFile);
		
		// Look for defaults in jar
		InputStream defaultNpcStream = Bukkit.getPluginManager().getPlugin(Folks.pluginName).getResource("config/npc/npc.yml");
	    if (defaultNpcStream != null) {
	    	YamlConfiguration defaultNpcCfg = YamlConfiguration.loadConfiguration(defaultNpcStream);
	    	npcCfg.setDefaults(defaultNpcCfg);
	    }
	}
	
	/**
	 * Gets the Npc config
	 * 
	 * @return The Npc config 
	 * 		as FileConfiguration
	 */
	private static FileConfiguration getNpcCfg() {
		if (npcCfg == null) {
			reloadNpcCfg();
		}
		
		return npcCfg;
	}
	
	/**
	 * Saves the Npc config
	 */
	private static void saveNpcCfg() {
		if (npcCfg == null || npcCfgFile == null) {
			return;
		}
		
		try {
			npcCfg.save(npcCfgFile);
		} catch (IOException e) {
			Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Could not save " + npcCfgFile, e);
		}
	}
	
	/**
	 * Reloads the NpcType config
	 */
	private static void reloadNpcTypeCfg() {
		if (npcTypeCfgFile == null) {
			npcTypeCfgFile = new File(Bukkit.getPluginManager().getPlugin(Folks.pluginName).getDataFolder(), "npc/type.yml");
		}
	    
		npcTypeCfg = YamlConfiguration.loadConfiguration(npcTypeCfgFile);
	}
	
	/**
	 * Gets the NpcType config
	 * 
	 * @return The NpcType config
	 * 		as FileConfiguration
	 */
	private static FileConfiguration getNpcTypeCfg() {
		if (npcTypeCfg == null) {
			reloadNpcTypeCfg();
		}
		
		return npcTypeCfg;
	}
}