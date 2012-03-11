package com.thinkamajig.folks.bank;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.thinkamajig.folks.Folks;


public class Bank {
	
	private static FileConfiguration bankCfg = null;
	private static File bankCfgFile = null;
	
	/**
	 * Gets the current balance of a player
	 * 
	 * @param player
	 * 		Entity of the player
	 * @return The balance of his bank account or 0 when
	 * 		the player dont have a bank account yet
	 */
	public static long getPlayerBalance(Player player) {
		if(bankCfg == null) {
			getBankCfg();
		}
		// return his balance (default = 0)
		return bankCfg.getInt(player.getDisplayName(), 0);
	}
	
	/**
	 * Sets the balance of a player
	 * 
	 * @param player
	 * 		Entity of the Player
	 * @param balance
	 * 		new balance value of the players bank account
	 */
	public static void setPlayerBalance(Player player, long balance) {
		if(bankCfg == null) {
			getBankCfg();
		}
		// set balance
		bankCfg.set(player.getDisplayName(), balance);
		// save bank
		saveBankCfg();
	}
	
	/**
	 * Reloads the Bank from file system
	 */
	public static void reloadBankCfg() {
		// no bank file?
		if (bankCfgFile == null) {
			// create it
			bankCfgFile = new File(Bukkit.getPluginManager().getPlugin(Folks.pluginName).getDataFolder(), "player/bank.yml");
		}
	    
		// load file
		bankCfg = YamlConfiguration.loadConfiguration(bankCfgFile);
	}
	
	/**
	 * Gets the Bank
	 * 
	 * @return The Bank
	 */
	public static FileConfiguration getBankCfg() {
		// no bank?
		if(bankCfg == null) {
			// reload
			reloadBankCfg();
		}
		
		// return bank
		return bankCfg;
	}

	/**
	 * Saves the Bank
	 */
	public static void saveBankCfg() {
		if (bankCfg == null || bankCfgFile == null) {
			return;
		}
		
		try {
			// save bank to file
	        bankCfg.save(bankCfgFile);
		} catch (IOException e) {
			// print error
			Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Could not save bank to " + bankCfgFile, e);
		}
	}
}