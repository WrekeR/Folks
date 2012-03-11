package com.thinkamajig.folks.action;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.thinkamajig.folks.gui.Text;
import com.thinkamajig.folks.npc.Npc;
import com.thinkamajig.folks.npc.NpcController;
import com.thinkamajig.folks.npc.inventory.NpcInventory;
import com.thinkamajig.folks.npc.inventory.NpcInventoryController;
import com.thinkamajig.folks.npc.skill.NpcSkill;
import com.thinkamajig.folks.npc.skill.Trading;
import com.thinkamajig.folks.bank.Bank;


public class Buy extends Action {
	
	/**
	 * Buys an item with the specified amount
	 * 
	 * @param sender
	 * 		the purchaser
	 * @param id
	 * 		item id
	 * @param amount
	 * 		item amount
	 */
	public static boolean buy(Player player, int id, int amount) {		
		// negative amount or invalid id?
		if(amount <= 0 || id > 4) {
			// send error msg to player
			player.sendMessage(r + "[ERROR] " + Text.getText("errorarg"));
			return false;
		}
		
		// get nearby traders
		Npc trader = NpcController.getNearestNpc(player);
		
		// is there a trader nearby?
		if(trader != null && trader.hasSkill(NpcSkill.TRADING)) {
			// get the inventory of the trader
			NpcInventory inv = ((Trading)trader.getType().getSkillById(NpcSkill.TRADING)).getInventory();
			
			// get item data
			ItemStack item = inv.getItems().get(id);
			int itemTypeId = item.getTypeId();
			byte itemData = item.getData().getData();
			String itemId = NpcInventoryController.makeItemId(item);
			
			// original item amount in inventory
			int oAmount = item.getAmount();
			
			// valid original item amount?
			if(oAmount != -1) {
				// calculate new amount
				int nAmount = oAmount-amount;
				
				// get costs per item
				int costsPerItem = NpcInventoryController.getItemCosts(inv, itemId);
				// get player balance
				long oBalance = Bank.getPlayerBalance(player);
				
				// no items in the inventory?
				if(oAmount == 0) {
					// send error message
					player.sendMessage(p + Text.getText("noitems") + " " + 
							NpcInventoryController.getItemName(inv, itemId));
					return false;
				}
				
				// new amount smaller 0?
				// if so, sale the original item amount
				if(nAmount < 0) {
					// calculate costs for original amount
					long loss = costsPerItem * oAmount;
					// calculate new balance
					long nBalance = oBalance - loss;
					
					// negative new balance?
					if(nBalance < 0) {
						// send error msg to player
						player.sendMessage(r + Text.getText("nomoney"));
					} else {
						// update iventory with new item amount
						// in this case zero & save the inventory
						NpcInventoryController.updateNpcInventory(inv.getId(), itemId, 0);
						// set the player balance to new balance
						Bank.setPlayerBalance(player, nBalance);
						
						// add items to the player's inventory
						player.getInventory().addItem(new ItemStack(itemTypeId, oAmount, (short)0, itemData));
						
						// send info msg to player
						player.sendMessage(dg + Text.getText("bought") + " " + 
								g + oAmount + "x " + NpcInventoryController.getItemName(inv, itemId));
						player.sendMessage(dg + Text.getText("newbalance") + " $" + 
								nBalance + p + "(-$" + loss + ")");
						
						return true;
					}
				// sale the specified amount
				} else {
					// calculate costs for specified amount
					long loss = costsPerItem * amount;
					// calculate new balance
					long nBalance = oBalance - loss;
					
					// negative new balance?
					if(nBalance < 0) {
						// send error msg to player
						player.sendMessage(r + Text.getText("nomoney"));
					} else {
						// update iventory with new item amount
						// in this case the new amount & save the inventory
						NpcInventoryController.updateNpcInventory(inv.getId(), itemId, nAmount);
						// set the player balance to new balance
						Bank.setPlayerBalance(player, nBalance);
						
						// add items to the player's inventory
						player.getInventory().addItem(new ItemStack(itemTypeId, amount, (short)0, itemData));
						
						// send info msg to player
						player.sendMessage(dg + Text.getText("bought") + " " + g + 
								amount + "x " + NpcInventoryController.getItemName(inv, itemId));
						player.sendMessage(dg + Text.getText("newbalance") + " $" + 
								nBalance + p + " (-$" + loss + ")");
						
						return true;
					}
				}				
			}			
		} else {
			// send error msg to player that no trader is nearby
			player.sendMessage(r + Text.getText("notrader"));
		}
		
		return false;		
	}
}
