package com.thinkamajig.folks.npc.dialog;

import java.util.ArrayList;


public class NpcDialog {
	
	private String id;
	private String text;
	private ArrayList<NpcDialogAwnser> awnsers;
	
	public NpcDialog(String id, String text, ArrayList<NpcDialogAwnser> awnsers) {
		this.id = id;
		this.text = text;
		this.awnsers = awnsers;
	}
	
	public String getId() {
		return id;
	}
	
	public String getText() {
		return text;
	}
	
	public ArrayList<NpcDialogAwnser> getAwnsers() {
		return awnsers;
	}
}
