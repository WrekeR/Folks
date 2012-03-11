package com.thinkamajig.folks.scheduler;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.thinkamajig.folks.event.entity.EntityMoveEvent;


public class EntityMoveScheduler implements Runnable {
	private static Map<Entity, Location> prevPos = new HashMap<Entity, Location>();
	
	private static void setPrevPos() {
		for(World w : Bukkit.getServer().getWorlds()) {
			for(LivingEntity le : w.getLivingEntities()) {
				prevPos.put(le, le.getLocation());
			}
		}
	}
	
	private static void checkPos() {
		for(Entity enty : prevPos.keySet()) {
			if(!prevPos.get(enty).equals(enty.getLocation())) {
				// Create the event
				EntityMoveEvent event = new EntityMoveEvent(enty, prevPos.get(enty), enty.getLocation());
				// Call the event
				Bukkit.getServer().getPluginManager().callEvent(event);
			}
		}
	}

	public void run() {
		checkPos();
		setPrevPos();
	}
}
