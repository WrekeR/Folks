package com.thinkamajig.folks.npc;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.thinkamajig.folks.npc.skill.NpcSkill;


public class Npc {

	private String id;
	private String name;
	private Location position;
	private NpcType type;
	private LivingEntity entity;

	/**
	 * Npc Constructor
	 * 
	 * @param NpcId
	 * 		The id of the Npc
	 * @param NpcName
	 * 		The display name of the Npc
	 * @param NpcPos
	 * 		Position of the Npc
	 * @param NpcType
	 * 		Type of the Npc
	 * @param NpcEntity
	 * 		Entity of the Npc
	 */
	public Npc(String npcId, String npcName, Location npcPos, 
			NpcType npcType, LivingEntity npcEntity) {
		id = npcId;
		name = npcName;
		position = npcPos;
		type = npcType;
		entity = npcEntity;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public Location getPosition() {
		return position;
	}
	
	public void setPosition(Location position) {
		this.position = position;
	}
	
	public NpcType getType() {
		return type;
	}
	
	public LivingEntity getEntity() {
		return entity;
	}
	
	public boolean hasSkill(int skill) {
		ArrayList<NpcSkill> npcSkills = type.getSkills();
		
		for(NpcSkill npcSkill : npcSkills) {
			if(npcSkill.getId() == skill) {
				return true;
			}
		}
		
		return false;
	}
	
	public void setLoS(Player target) {
		this.getPosition().setYaw(target.getLocation().getYaw() - 180);
		this.getEntity().getLocation().setYaw(target.getLocation().getYaw() - 180);
		
		// creates head wobbling
		// no solution found yet
		// traderLoc.setPitch(targetLoc.getPitch() - 180);
		
		this.getEntity().teleport(this.getPosition());
	}
	
	public void teleport(Location target) {
		this.setPosition(target);
		this.getEntity().teleport(target);
	}
}