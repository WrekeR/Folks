package com.thinkamajig.folks.listener;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.thinkamajig.folks.npc.NpcController;

public class PlayerQuitListener implements Listener {
	
	/**
	 * Fires when a Player quits
	 * 
	 * @param event
	 * 		Quit event
	 */
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Server svr = Bukkit.getServer();
		
		if(svr.getOnlinePlayers().length == 1) {
			NpcController.removeNpcs();
		}
	}
}
