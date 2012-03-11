package com.thinkamajig.folks.gui;

import org.getspout.spoutapi.player.SpoutPlayer;

import com.thinkamajig.folks.npc.Npc;


public class GuiController {
	
	public static final int GUI = 0;
	public static final int TRADERMENU = 1;
	public static final int NPCMESSAGE = 2;
	public static final int QUESTMENU = 3;
	public static final int QUESTBOOKMENU = 4;
	
	public static void createGui(int type, SpoutPlayer target, Npc source) {
		new NpcMenu(source, target);
	}
}