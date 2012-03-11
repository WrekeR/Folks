package com.thinkamajig.folks.npc.skill;

public class Repairing extends NpcSkill {

	private int costs;
	
	/**
	 * Repairing Constructor
	 */
	public Repairing(int uid, int costs) {
		super(uid);
		id = REPAIRING;
		name = "Repairing";
		
		this.costs = costs;
	}

	public int getCosts() {
		return costs;
	}
}
