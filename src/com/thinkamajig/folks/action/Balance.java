package com.thinkamajig.folks.action;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.thinkamajig.folks.bank.Bank;
import com.thinkamajig.folks.gui.Text;


public class Balance extends Action {

	/**
	 * Sends the balance to the command sender
	 * @param sender
	 * 		The Entity which sent the command
	 */
	public static void balance(CommandSender sender) {
		String senderName = sender.getName();
		Player player = Bukkit.getServer().getPlayerExact(senderName);
		
		long balance = Bank.getPlayerBalance(player);
		
		player.sendMessage(dg + Text.getText("balance") + 
				g + " $" + balance);
	}
}
