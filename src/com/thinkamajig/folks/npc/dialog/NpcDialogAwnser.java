package com.thinkamajig.folks.npc.dialog;

import java.util.ArrayList;

public class NpcDialogAwnser extends NpcDialog {
	
	private String title;
	
	public NpcDialogAwnser(String title, String text, ArrayList<NpcDialogAwnser> awnsers) {
		super(title, text, awnsers);
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
}
