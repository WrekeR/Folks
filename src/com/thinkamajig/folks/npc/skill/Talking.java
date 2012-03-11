package com.thinkamajig.folks.npc.skill;

public class Talking extends NpcSkill {

	private String dialog;
	
	/**
	 * Talking Constructor
	 * 
	 * @param id
	 * @param text
	 */
	public Talking(int uid, String dialog) {
		super(uid);
		id = TALKING;
		name = "Talking";
		
		this.dialog = dialog;
	}
	
	public String getDialog() {
		return dialog;
	}
}
