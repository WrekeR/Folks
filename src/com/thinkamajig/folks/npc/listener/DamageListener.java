package com.thinkamajig.folks.npc.listener;

import org.bukkit.ChatColor;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import com.thinkamajig.folks.gui.Text;
import com.thinkamajig.folks.npc.Npc;
import com.thinkamajig.folks.npc.NpcController;
import com.thinkamajig.folks.npc.skill.NpcSkill;


public class DamageListener implements Listener {
	
	/**
	 * Fires when a Npc receives damage
	 * 
	 * @param event
	 * 		Damage event
	 */
	@EventHandler
	public void onNpcDamage(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		Npc npc = NpcController.getNpcByUuid(entity.getUniqueId().hashCode());
		
		if(npc != null) {
			LivingEntity le = (LivingEntity)entity;
			
			if(npc.hasSkill(NpcSkill.INVULNERABLE)) {
				event.setCancelled(true);
				
				EntityDamageEvent entyDmgEvent = le.getLastDamageCause();
				
				// Trader attacked by Player?
				if(entyDmgEvent instanceof EntityDamageByEntityEvent 
						&& ((EntityDamageByEntityEvent)entyDmgEvent).getDamager() 
							instanceof Player) {
					
					Player damager = (Player) ((EntityDamageByEntityEvent)entyDmgEvent).getDamager();
					
					// set player as target
					npc.setLoS(damager);
					
					// yell at player
					damager.sendMessage(" ");
					damager.sendMessage(ChatColor.RED + "["+ npc.getName() +"] " + Text.getRandomText("warnings"));
					
					// shoot arrow towards player
					le.shootArrow();
					//le.launchProjectile(Arrow.class);
				}
			}
		}
	}
}