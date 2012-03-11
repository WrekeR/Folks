package com.thinkamajig.folks.action;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.thinkamajig.folks.gui.Text;
import com.thinkamajig.folks.npc.Npc;
import com.thinkamajig.folks.npc.NpcController;
import com.thinkamajig.folks.npc.inventory.NpcInventory;
import com.thinkamajig.folks.npc.inventory.NpcInventoryController;
import com.thinkamajig.folks.npc.skill.NpcSkill;
import com.thinkamajig.folks.npc.skill.Trading;
import com.thinkamajig.folks.bank.Bank;


public class Sell extends Action {
	
	/**
	 * Sells an item with the specified amount
	 * 
	 * @param sender
	 * 		the seller
	 * @param id
	 * 		item id
	 * @param amount
	 * 		item amount
	 */	
	public static boolean sell(Player player, int id, int amount) {		
		// negative amount or invalid id?
		if(amount <= 0 || id > 4) {
			player.sendMessage(r + "[ERROR] " + Text.getText("errorarg"));
			return false;
		}
		
		// get nearby traders
		Npc trader = NpcController.getNearestNpc(player);
		
		// is there a trader nearby?
		if(trader != null && trader.hasSkill(NpcSkill.TRADING)) {
			PlayerInventory invPlayer = player.getInventory();
			// get the inventory of the trader
			NpcInventory inv = ((Trading)trader.getType().getSkillById(NpcSkill.TRADING)).getInventory();
			
			// get item data
			ItemStack item = inv.getItems().get(id);
			int itemTypeId = item.getTypeId();
			byte itemData = item.getData().getData();
			String itemId = NpcInventoryController.makeItemId(item);
			
			// player inventory contians the specified item with the specified amount
			if(invPlayer.contains(itemTypeId, amount)) {
				// match the items in the player inventory
				HashMap<Integer, ? extends ItemStack> matchedItems = invPlayer.all(itemTypeId);
				// initialize boolean for data check
				boolean isDataMatched = false;
				
				// cycle through matched items
				for(Integer i : matchedItems.keySet()) {
					// data matches?
					if(matchedItems.get(i).getData().getData() == itemData) {
						// set boolean to true
						isDataMatched = true;
					}
				}
				
				// no data match?
				if(!isDataMatched) {
					// send error msg to player
					player.sendMessage(r + "[ERROR] " + Text.getText("errorarg"));
					
					return false;
				}
				
				int oAmount = item.getAmount();
				
				// valid original item amount?
				if(oAmount != -1) {
					// calculate new amount
					int nAmount = oAmount + amount;
					
					// get income per item
					int incPerItem = NpcInventoryController.getItemIncome(inv, itemId);
					// calculate income
					long income = incPerItem * amount;
					
					// get the original balance
					long oBalance = Bank.getPlayerBalance(player);
					// calculate new balance
					long nBalance = oBalance + income;
					
					// update iventory with new item amount
					// in this case the specified amount & save the inventory
					NpcInventoryController.updateNpcInventory(inv.getId(), itemId, nAmount);
					// set the player balance to new balance
					Bank.setPlayerBalance(player, nBalance);
					
					// remove the specified item with the specified amount
					// from the player's ivnentory
					invPlayer.removeItem(new ItemStack(itemTypeId, amount, (short)0, itemData));
					
					// send info msg to player
					player.sendMessage(dg + Text.getText("sold") + " " + g + 
							amount + "x " + NpcInventoryController.getItemName(inv, itemId));
					player.sendMessage(dg + Text.getText("newbalance") + " $" + 
							nBalance + g + " (+$" + income + ")");
					
					return true;
				}
			} else {
				// send error msg to player
				player.sendMessage(r + "[ERROR] " + Text.getText("errorarg"));
			}
		} else {
			// send error msg to player
			player.sendMessage(r + Text.getText("notrader"));
		}
		
		return false;
	}
}
