package com.thinkamajig.folks.action;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.thinkamajig.folks.gui.Text;
import com.thinkamajig.folks.npc.Npc;
import com.thinkamajig.folks.npc.NpcController;
import com.thinkamajig.folks.npc.skill.NpcSkill;
import com.thinkamajig.folks.npc.skill.Repairing;
import com.thinkamajig.folks.bank.Bank;


public class Repair extends Action {
	
	private static int[]
		/*
		 * Tools	
		 */
		tools = {
			/*
			 * Wood Tools	
			 */
			// sword
			268,
			// shovel
			269,
			// pickaxe
			270,
			// axe
			271,
			// hoe
			290,
			/*
			 * Stone Tools	
			 */
			// sword
			272,
			// shovel
			273,
			// pickaxe
			274,
			// axe
			275,
			// hoe
			291,
			/*
			 * Iron Tools	
			 */
			// sword
			267,
			// shovel
			256,
			// pickaxe
			257,
			// axe
			258,
			// hoe
			292,
			/*
			 * Gold Tools	
			 */
			// sword
			283,
			// shovel
			284,
			// pickaxe
			285,
			// axe
			286,
			// hoe
			294,
			/*
			 * Diamond Tools	
			 */
			// sword
			276,
			// shovel
			277,
			// pickaxe
			278,
			// axe
			279,
			// hoe
			293
		},
		/*
		 * Armor
		 */
		armor = {
			/*
			 * Leather Armor
			 */
			// head
			298,
			// chest
			299,
			// pant
			300,
			// boot
			301,
			/*
			 * Iron Armor
			 */
			// head
			306,
			// chest
			307,
			// pant
			308,
			// boot
			309,
			/*
			 * Gold Armor
			 */
			// head
			314,
			// chest
			315,
			// pant
			316,
			// boot
			317,
			/*
			 * Diamond Armor
			 */
			// head
			310,
			// chest
			311,
			// pant
			312,
			// boot
			313
		};

	/**
	 * Repairs items with the specified type
	 * 
	 * @param sender
	 * 		requestor
	 * @param type
	 * 		item type
	 */
	public static void repair(CommandSender sender, String type) {
		// get the player
		String senderName = sender.getName();
		Player player = Bukkit.getServer().getPlayerExact(senderName);
		
		// invalid type?
		if(!type.equals("armor") && !type.equals("tools") && !type.equals("all")) {
			// send error msg to player
			player.sendMessage(r + "[ERROR] " + Text.getText("errorarg"));
			return;
		}
		
		// get nearby npcs
		Npc npc = NpcController.getNearestNpc(player);
		
		// is there a npc nearby?
		if(npc != null && npc.hasSkill(NpcSkill.REPAIRING)) {
			int costs = ((Repairing)npc.getType().getSkillById(NpcSkill.REPAIRING)).getCosts();
			
			// get player's inventory
			PlayerInventory inv = player.getInventory();
			
			if(type.equals("tools")) {
				// repair tools
				repairTools(player, costs, inv);
			} else if(type.equals("armor")) {
				// repair armor
				repairArmor(player, costs, inv);
			} else if(type.equals("all")) {
				// repair tools
				repairTools(player, costs, inv);
				// repair armor
				repairArmor(player, costs, inv);
			}
		} else {
			// send error msg to player that no npc is nearby
			player.sendMessage(r + Text.getText("notrader"));
		}
	}
	
	public static boolean repairTools(Player player, int costs, PlayerInventory inv) {
		ArrayList<ItemStack> toolsToBeRepaired = new ArrayList<ItemStack>();
		int count = 0;
		int loss = 0;
		
		// cycle through tools
		for(int tool : tools) {
			for(ItemStack item : inv.getContents()) {				
				// if tool item is contained in player's inventory
				if(item != null) {
					if(item.getTypeId() == tool) {
						if(item.getDurability() != 0) {
							toolsToBeRepaired.add(item);
							loss += Math.ceil((float)item.getDurability()
									/ Material.getMaterial(tool).getMaxDurability()) * costs;
						}
					}
				}
			}
		}
		
		count = toolsToBeRepaired.size();
		
		// sth to repair?
		if(count > 0) {
			long oBalance = Bank.getPlayerBalance(player);
			long nBalance = oBalance - loss;
			
			// enough money?
			if(nBalance > 0) {
				// cycle through inv items
				for(ItemStack toolTBR : toolsToBeRepaired) {
					// repair tool
					toolTBR.setDurability((short) 0);
				}
				
				// set the player balance to new balance
				Bank.setPlayerBalance(player, nBalance);
				
				// send info msg to player
				player.sendMessage(dg + Text.getText("repaired"));
				player.sendMessage(dg + Text.getText("newbalance") + " $" + 
						nBalance + p + " (-$" + loss + ")");
			} else {
				// send error msg to player
				player.sendMessage(r + Text.getText("nomoney"));
				return false;
			}
		} else {
			// send info msg to player
			player.sendMessage(dg + Text.getText("noequipdmg"));
		}

		return true;
	}
	
	public static boolean repairArmor(Player player, int costs, PlayerInventory inv) {
		ArrayList<ItemStack> armorToBeRepaired = new ArrayList<ItemStack>();
		int count = 0;
		int loss = 0;
		
		// cycle through tools
		for(int arm : armor) {
			for(ItemStack item : inv.getContents()) {				
				// if tool item is contained in player's inventory
				if(item != null) {
					if(item.getTypeId() == arm) {
						if(item.getDurability() != 0) {
							armorToBeRepaired.add(item);
							loss += Math.ceil((float)item.getDurability()
									/ Material.getMaterial(arm).getMaxDurability()) * costs;
						}
					}
				}
			}
		}
		
		count = armorToBeRepaired.size();
		
		// sth to repair?
		if(count > 0) {
			long oBalance = Bank.getPlayerBalance(player);
			long nBalance = oBalance - loss;
			
			// enough money?
			if(nBalance > 0) {
				// cycle through inv items
				for(ItemStack armTBR : armorToBeRepaired) {
					// repair tool
					armTBR.setDurability((short) 0);
				}
				
				// set the player balance to new balance
				Bank.setPlayerBalance(player, nBalance);
				
				// send info msg to player
				player.sendMessage(dg + Text.getText("repaired"));
				player.sendMessage(dg + Text.getText("newbalance") + " $" + 
						nBalance + p + " (-$" + loss + ")");
			} else {
				// send error msg to player
				player.sendMessage(r + Text.getText("nomoney"));
				return false;
			}
		} else {
			// send info msg to player
			player.sendMessage(dg + Text.getText("noequipdmg"));
		}

		return true;
	}
}