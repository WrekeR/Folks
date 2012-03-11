package com.thinkamajig.folks.npc.inventory;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;


public class NpcInventory {
	
	private String id;
	private ArrayList<ItemStack> items;
	
	public NpcInventory(String invId, ArrayList<ItemStack> invItems) {
		id = invId;
		items = invItems;
	}
	
	public String getId() {
		return id;
	}
	
	public ArrayList<ItemStack> getItems() {
		return items;
	}
	
	public int getItemAmount(String id) {
		for(ItemStack item : getItems()) {
			if(NpcInventoryController.makeItemId(item).equals(id)) {
				return item.getAmount();
			}
		}
		return -1;
	}
}
