package com.thinkamajig.folks.gui;

import java.util.ArrayList;

import org.getspout.spoutapi.gui.Button;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.Texture;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.thinkamajig.folks.npc.Npc;
import com.thinkamajig.folks.npc.dialog.NpcDialog;
import com.thinkamajig.folks.npc.dialog.NpcDialogAwnser;

public class Dialog extends Gui {
	
	private NpcDialog dialog;
	
	private Texture bg, closeTex;
	private GenericLabel npcName, npcProfession, text;
	private Button close;
	
	public Dialog(Npc npc, SpoutPlayer target, NpcDialog dialog) {
		super(npc, target);
		this.dialog = dialog;
		
		String name = npc.getName();
		String profession = npc.getType().getName();
		
		String initText = dialog.getText();
		ArrayList<NpcDialogAwnser> awnsers = dialog.getAwnsers();
		
		if(target.isSpoutCraftEnabled()) {
			bg = new GenericTexture("http://thinkamajig.net/mc/folks/texture/guibg.png");
			
			int screenWidth = target.getMainScreen().getWidth();
			int screenHeight = target.getMainScreen().getHeight();
			
			bg.setAnchor(WidgetAnchor.TOP_LEFT).setHeight(175).setWidth(225)
					.setX((screenWidth-bg.getWidth())/2).setY((screenHeight-bg.getHeight())/2);
			bg.setPriority(RenderPriority.Highest);
			
			npcName = new GenericLabel(name);
			npcName.setAnchor(WidgetAnchor.TOP_LEFT)
					.setHeight(10).setWidth(GenericLabel.getStringWidth(npcName.getText()))
					.setX(bg.getX()+15).setY(bg.getY()+7);
			//npcName.setTextColor(new Color(77, 56, 30));
			
			npcProfession = new GenericLabel("[" + profession + "]");
			npcProfession.setAnchor(WidgetAnchor.TOP_LEFT)
					.setHeight(10).setWidth(GenericLabel.getStringWidth(npcProfession.getText()))
					.setX(npcName.getX()).setY(npcName.getY()+10);
			//npcProfession.setTextColor(new Color(77, 56, 30));
			
			close = new GenericButton("");
			close.setAnchor(WidgetAnchor.TOP_LEFT).setHeight(10).setWidth(10)
					.setX(bg.getX()+bg.getWidth()-25).setY(bg.getY()+6);
			close.setTooltip(Text.getText("close"));
			
			closeTex = new GenericTexture("http://thinkamajig.net/mc/folks/texture/close.png");
			closeTex.setAnchor(WidgetAnchor.TOP_LEFT).setHeight(22).setWidth(22)
					.setX(bg.getX()+bg.getWidth()-29).setY(bg.getY());
			closeTex.setPriority(RenderPriority.Lowest);
			
			text = new GenericLabel(initText);
			text.setAnchor(WidgetAnchor.TOP_LEFT).setHeight(GenericLabel.getStringHeight(text.getText()))
					.setWidth(195).setX(bg.getX()+15).setY(bg.getY()+55);
			
			for(int i = 0; i < awnsers.size(); i++) {
				int textWidth = GenericLabel.getStringWidth(awnsers.get(i).getTitle())+15;
				int marginRight = textWidth;
				
				for(int j = 0; j < i; j++) {
					marginRight += GenericLabel.getStringWidth(awnsers.get(j).getTitle())+15;
				}
				
				GenericButton btAwnser = new GenericButton(awnsers.get(i).getTitle());
				btAwnser.setAnchor(WidgetAnchor.TOP_LEFT).setHeight(20).setWidth(textWidth)
						.setX(bg.getX()+bg.getWidth()-marginRight-5*i).setY(bg.getY()+bg.getHeight()-20);
				attachWidget(plugin, btAwnser);
			}
			
			attachWidget(plugin, closeTex);
			attachWidget(plugin, close);				
			attachWidget(plugin, npcName);
			attachWidget(plugin, npcProfession);
			attachWidget(plugin, text);
			attachWidget(plugin, bg);
			
			target.getMainScreen().attachPopupScreen(this);
		}
	}
	
	public void getButtonActions(Button button, SpoutPlayer target) {
		if(button.equals(close)) {
			target.getMainScreen().closePopup();
			return;
		}
		
		ArrayList<NpcDialogAwnser> awnsers = dialog.getAwnsers();
		
		for(NpcDialogAwnser awnser : awnsers) {
			if(button.getText().equals(awnser.getTitle())) {
				target.getMainScreen().closePopup();
				new Dialog(getNpc(), target, awnser);
				return;
			}
		}		
	}
}
