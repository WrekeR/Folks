package com.thinkamajig.folks.action;

import org.bukkit.entity.Player;

import com.thinkamajig.folks.bank.Bank;
import com.thinkamajig.folks.gui.Text;


public class Send extends Action {
	
	/**
	 * Transfers money from one player to another
	 * 
	 * @param sender
	 * 		the sender
	 * @param receiver
	 * 		the receiver
	 * @param amount
	 * 		amount of money
	 */	
	public static void send(Player sender, Player receiver, int amount) {
		// does the receiver exist?
		if(receiver != null) {
			// is the amount positive?
			if(amount > 0) {
				// get orignal & new balance of the sender
				long oBalanceSend = Bank.getPlayerBalance(sender);
				long nBalanceSend = oBalanceSend - amount;
				
				// does the sender have enough money?
				if(nBalanceSend < 0) {
					// send error msg
					sender.sendMessage(r + Text.getText("nomoney"));
				} else {
					// get original / new balance of receiver
					long oBalanceRec = Bank.getPlayerBalance(receiver);
					long nBalanceRec = oBalanceRec + amount;
					
					// set the balance of both players
					Bank.setPlayerBalance(sender, nBalanceSend);
					Bank.setPlayerBalance(receiver, nBalanceRec);
	
					// send info msg to sender
					sender.sendMessage(g + "$" + amount + " " + dg + 
							Text.getText("send") + " " + g + receiver.getDisplayName());
					sender.sendMessage(dg + Text.getText("newbalance") + " $" + 
							nBalanceSend + " " + p + "(-$" + amount + ")");
	
					// send info msg to receiver
					receiver.sendMessage(g + "$" + amount + " " + dg + 
							Text.getText("received") + " " + g + sender.getDisplayName());
					receiver.sendMessage(dg + Text.getText("newbalance") + " $" + 
							nBalanceRec + " " + g + "(+$" + amount + ")");
				}
			} else {
				// send error msg
				sender.sendMessage(r + "[ERROR] " + Text.getText("errorarg"));
			}
		} else {
			// send error msg
			sender.sendMessage(r + "[ERROR] " + Text.getText("errorarg"));
		}
	}
}
