package com.thinkamajig.folks.npc.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.thinkamajig.folks.gui.GuiController;
import com.thinkamajig.folks.npc.Npc;
import com.thinkamajig.folks.npc.NpcController;


public class ClickListener implements Listener {
	
	/**
	 * Fires when a Npc is clicked
	 * 
	 * @param event
	 * 		Interaction event
	 */
	@EventHandler
	public void onEntityClick(PlayerInteractEntityEvent event) {
		Entity entity = event.getRightClicked();
		
		if(entity instanceof LivingEntity) {
			
			Npc npc = NpcController.getNpcByUuid(entity.getUniqueId().hashCode());
			
			if(npc != null) {
				Player sender = event.getPlayer();
				
				npc.setLoS(sender);
				
				GuiController.createGui(GuiController.GUI, (SpoutPlayer)sender, npc);
			}
		}
	}
}
