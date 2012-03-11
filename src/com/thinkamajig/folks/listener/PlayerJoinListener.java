package com.thinkamajig.folks.listener;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.thinkamajig.folks.npc.NpcController;

public class PlayerJoinListener implements Listener {
	
	/**
	 * Fires when a Player joins
	 * 
	 * @param event
	 * 		Join event
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Server svr = Bukkit.getServer();
		
		if(svr.getOnlinePlayers().length == 1) {
			NpcController.init(svr);
		}
	}
}
