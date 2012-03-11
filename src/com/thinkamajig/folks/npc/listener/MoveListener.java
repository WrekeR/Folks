package com.thinkamajig.folks.npc.listener;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.thinkamajig.folks.event.entity.EntityMoveEvent;
import com.thinkamajig.folks.npc.Npc;
import com.thinkamajig.folks.npc.NpcController;
import com.thinkamajig.folks.npc.skill.NpcSkill;

public class MoveListener implements Listener {
	
	/**
	 * Fires when a Npc moves
	 * 
	 * @param event
	 * 		Move event
	 */
	@EventHandler
	public void onNpcMove(EntityMoveEvent event) {
		Entity entity = event.getEntity();
		
		if(entity instanceof LivingEntity) {
			
			Npc npc = NpcController.getNpcByUuid(entity.getUniqueId().hashCode());
			
			if(npc != null) {
				if(!npc.hasSkill(NpcSkill.MOVEMENT)) {
					Location npcLoc = npc.getPosition();					
					npc.getEntity().teleport(npcLoc);
				}
			}
		}
	}
}