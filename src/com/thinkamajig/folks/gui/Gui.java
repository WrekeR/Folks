package com.thinkamajig.folks.gui;

import org.bukkit.ChatColor;

import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.thinkamajig.folks.Folks;
import com.thinkamajig.folks.npc.Npc;


public class Gui extends GenericPopup {
	
	protected Folks plugin = Folks.getInstance();
	
	protected static final ChatColor GOLD = ChatColor.GOLD;
	protected static final ChatColor YELLOW = ChatColor.YELLOW;
	protected static final ChatColor GRAY = ChatColor.GRAY;
	protected static final ChatColor GREEN = ChatColor.GREEN;
	protected static final ChatColor PURPLE = ChatColor.DARK_PURPLE;
	
	private Npc npc;
	private SpoutPlayer target;
	
	public Gui(Npc npc, SpoutPlayer target) {
		this.npc = npc;
		this.target = target;
	}
	
	public Npc getNpc() {
		return npc;
	}
	
	public SpoutPlayer getTarget() {
		return target;
	}
}