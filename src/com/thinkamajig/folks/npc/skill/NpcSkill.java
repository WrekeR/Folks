package com.thinkamajig.folks.npc.skill;

public class NpcSkill {
	
	public static final int DEFAULT = 0;
	public static final int FIGHTING = 1;
	public static final int HEALING = 2;
	public static final int INVULNERABLE = 3;
	public static final int MOVEMENT = 4;
	public static final int REPAIRING = 5;
	public static final int TALKING = 6;
	public static final int TRADING = 7;	
	
	protected int uid;
	protected String name;
	protected int id;
	
	/**
	 * NpcSkill Constructor
	 * 
	 * @param skillId
	 */
	public NpcSkill(int uid) {
		this.uid = uid;
		id = DEFAULT;
		name = "Default";
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}
	
	public int getUid() {
		return uid;
	}
}
