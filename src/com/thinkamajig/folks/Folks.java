package com.thinkamajig.folks;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.thinkamajig.folks.action.Balance;
import com.thinkamajig.folks.action.Buy;
import com.thinkamajig.folks.action.Repair;
import com.thinkamajig.folks.action.Sell;
import com.thinkamajig.folks.action.Send;
import com.thinkamajig.folks.gui.listener.GuiListener;
import com.thinkamajig.folks.listener.PlayerJoinListener;
import com.thinkamajig.folks.listener.PlayerQuitListener;
import com.thinkamajig.folks.npc.NpcController;
import com.thinkamajig.folks.npc.listener.ClickListener;
import com.thinkamajig.folks.npc.listener.DamageListener;
import com.thinkamajig.folks.npc.listener.MoveListener;
import com.thinkamajig.folks.scheduler.EntityMoveScheduler;


public class Folks extends JavaPlugin {
	
	public static String pluginName = "Folks";
	private static Folks instance;
	public PluginDescriptionFile pdfFile;
	
	/**
	 * get console log
	 */
	Logger log = Logger.getLogger("Minecraft");
	
	/**
	 * Runs when the plugin loads
	 */
	public void onLoad() {
		pdfFile = this.getDescription();
		pluginName = pdfFile.getName();
	}
	
	/**
	 * Runs when the plugin powers up
	 */
	public void onEnable() {				
		// write init message into log
		System.out.println("[" + pluginName.toUpperCase() + "] [INIT] " +
				"Trying to enable " + pdfFile.getName() + " v" + pdfFile.getVersion());
		
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(new PlayerJoinListener(), this);
		pm.registerEvents(new PlayerQuitListener(), this);
		pm.registerEvents(new DamageListener(), this);
		pm.registerEvents(new ClickListener(), this);
		pm.registerEvents(new MoveListener(), this);
		pm.registerEvents(new GuiListener(), this);
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new EntityMoveScheduler(), 0, 1L);
		
		System.out.println("[" + pluginName.toUpperCase() + "] [ENABLED] " +
				pdfFile.getName() + " v" + pdfFile.getVersion() + " successfully enabled.");
	}
	
	/**
	 * Runs when the plugin shuts down
	 */
	public void onDisable() {
		NpcController.removeNpcs();
		
		// write disabled message into log
		System.out.println("[" + pluginName.toUpperCase() + "] [DISABLED] " +
				pdfFile.getName() + " v" + pdfFile.getVersion() + " successfully disabled.");
	}
	
	/**
	 * Commands
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command has to be called by a player!");
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("buy")) {
			if(args.length == 2) {
				int id = Integer.parseInt(args[0]);
				int amount = Integer.parseInt(args[1]);
				Player player = Bukkit.getServer().getPlayerExact(sender.getName());
				
				Buy.buy(player, id, amount);
			} else {
				sender.sendMessage(cmd.getUsage().replace("<command>", cmd.getName()));
			}
			
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("sell")) {
			if(args.length == 2) {
				int id = Integer.parseInt(args[0]);
				int amount = Integer.parseInt(args[1]);
				Player player = Bukkit.getServer().getPlayerExact(sender.getName());
				
				Sell.sell(player, id, amount);
			} else {
				sender.sendMessage(cmd.getUsage().replace("<command>", cmd.getName()));
			}
			
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("balance")) {
			Balance.balance(sender);
			
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("send")) {
			if(args.length == 2) {
				Player player = Bukkit.getServer().getPlayerExact(sender.getName());
				Player receiver = Bukkit.getPlayer(args[0]);
				int amount = Integer.parseInt(args[1]);
				
				Send.send(player, receiver, amount);
			} else {
				sender.sendMessage(cmd.getUsage().replace("<command>", cmd.getName()));
			}
			
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("repair")) {
			if(args.length == 1) {
				Repair.repair(sender, args[0]);
			} else {
				sender.sendMessage(cmd.getUsage().replace("<command>", cmd.getName()));
			}
			
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("getpos")) {
			System.out.println(getServer().getPlayer(sender.getName()).getLocation());
			sender.sendMessage(getServer().getPlayer(sender.getName()).getLocation().toString());
			
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("tppos")) {
			if(args.length == 3) {
				getServer().getPlayer(sender.getName()).teleport(new Location(
						getServer().getPlayer(sender.getName()).getWorld(), 
						Integer.parseInt(args[0]), 
						Integer.parseInt(args[1]), 
						Integer.parseInt(args[2])				
				));
			}
			
			return true;
		}
		
		return false;
	}
	
	public static Folks getInstance() {
		return instance;
	}
}