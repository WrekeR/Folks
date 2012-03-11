package com.thinkamajig.folks.event.entity;

import org.bukkit.Location;

import org.bukkit.entity.Entity;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class EntityMoveEvent extends Event implements Cancellable {
	
	private static final long serialVersionUID = -7191607921307229497L;
	private static final HandlerList handlers = new HandlerList();
	private boolean cancel = false;
	private Entity entity;
	private Location origin;
	private Location target;

	public EntityMoveEvent(final Entity entity, final Location origin, final Location target) {
		super("EntityMoveEvent");
		this.entity = entity;
		this.origin = origin;
		this.target = target;
	}

	public boolean isCancelled() {
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
	
	public Entity getEntity() {
		return entity;
	}

	public Location getOrigin() {
		return origin;
	}

	public void setOrigin(Location origin) {
		this.origin = origin;
	}

	public Location getTarget() {
		return target;
	}

	public void setTarget(Location target) {
		this.target = target;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}