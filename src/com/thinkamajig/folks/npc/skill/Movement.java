package com.thinkamajig.folks.npc.skill;

public class Movement extends NpcSkill {

	/**
	 * Movement Constructor
	 * 
	 * @param loop
	 */
	public Movement(int uid, boolean loop) {
		super(uid);
		id = MOVEMENT;
		name = "Movement";
	}
	
	/**
	 * Movement Constructor
	 * 
	 * @param loop
	 * @param delay
	 */
	public Movement(int id, boolean loop, int delay) {
		super(id);

		name = "Movement";
	}

}
