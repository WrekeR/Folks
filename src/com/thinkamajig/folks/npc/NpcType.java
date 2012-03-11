package com.thinkamajig.folks.npc;

import java.util.ArrayList;

import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;

import com.thinkamajig.folks.npc.skill.NpcSkill;


public class NpcType {
	
	private String id;
	private String name;
	private ArrayList<NpcSkill> skills;
	private int entityClassId;
		
	/**
	 * NpcType Constructor
	 * 
	 * @param typeId
	 * 		The id of the NpcType
	 * @param typeName
	 * 		The name of the NpcType
	 * @param typeSkills
	 * 		The skills of the NpcType
	 * @param typeClassId
	 * 		The class id of the NpcType
	 */
	public NpcType(String typeId, String typeName, ArrayList<NpcSkill> typeSkills, int typeEntityClassId) {
		id = typeId;
		name = typeName;
		skills = typeSkills;
		entityClassId = typeEntityClassId;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<NpcSkill> getSkills() {
		return skills;
	}
	
	/**
	 * Gets a specific NpcSkill a Npc has
	 * 
	 * @param id
	 * 		The Id of the NpcSkill
	 * @return
	 * 		The NpcSkill with the specified Id.
	 * 		If no NpcSkill was found, null is returned
	 */
	public NpcSkill getSkillById(int id) {
		for(NpcSkill skill : this.getSkills()) {
			if(skill.getId() == id) {
				return skill;
			}
		}
		
		return null;
	}
	
	
	public Class<? extends LivingEntity> getEntityClass() {
		switch(entityClassId) {
			case NpcTypeEntity.BLAZE:
				return Blaze.class;
			case NpcTypeEntity.CAVESPIDER:
				return CaveSpider.class;
			case NpcTypeEntity.CREEPER:
				return Creeper.class;
			case NpcTypeEntity.ENDERDRAGON:
				return EnderDragon.class;
			case NpcTypeEntity.ENDERMAN:
				return Enderman.class;
			case NpcTypeEntity.GHAST:
				return Ghast.class;
			case NpcTypeEntity.SKELETON:
				return Skeleton.class;
			case NpcTypeEntity.SLIME:
				return Slime.class;
			case NpcTypeEntity.SPIDER:
				return Spider.class;
			case NpcTypeEntity.ZOMBIE:
				return Zombie.class;
			case NpcTypeEntity.PIGZOMBIE:
				return PigZombie.class;
			case NpcTypeEntity.VILLAGER:
				return Villager.class;
			default:
				return Villager.class;
		}
	}
}
