package com.thinkamajig.folks.npc.skill;

public class Fighting extends NpcSkill {

	/**
	 * Fighting Constructor
	 * 
	 * @param id
	 * @param selfDefense
	 */
	public Fighting(int uid, boolean selfDefense) {
		super(uid);
		id = FIGHTING;
		name = "Fighting";
	}

}
