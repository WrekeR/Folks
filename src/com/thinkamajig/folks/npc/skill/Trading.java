package com.thinkamajig.folks.npc.skill;

import com.thinkamajig.folks.npc.inventory.NpcInventory;

public class Trading extends NpcSkill {

	private NpcInventory inventory;
	
	/**
	 * Trading Constructor
	 * 
	 * @param id
	 * @param inventoryId
	 */
	public Trading(int uid, NpcInventory inventory) {
		super(uid);
		id = TRADING;
		name = "Trading";
		this.inventory = inventory;
	}
	
	/**
	 * Get the Inventory
	 * 
	 * @return
	 * 		The Inventory
	 */
	public NpcInventory getInventory() {
		return this.inventory;
	}
}
