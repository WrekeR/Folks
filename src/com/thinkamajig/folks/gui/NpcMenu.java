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
import com.thinkamajig.folks.npc.dialog.NpcDialogController;
import com.thinkamajig.folks.npc.skill.NpcSkill;
import com.thinkamajig.folks.npc.skill.Talking;


public class NpcMenu extends Gui {
	
	private Texture bg, closeTex;
	private GenericLabel npcName, npcProfession, text;
	private Button close, trade, heal, repair, talk;
	
	public NpcMenu(Npc npc, SpoutPlayer target) {
		super(npc, target);
		
		String greeting = Text.getRandomText("greetings");
		String intro = Text.getRandomText("intros");
		String name = npc.getName();
		String profession = npc.getType().getName();
		
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
			
			text = new GenericLabel(greeting + "\n" + intro + " " + name);
			text.setAnchor(WidgetAnchor.TOP_LEFT).setHeight(GenericLabel.getStringHeight(text.getText())).setWidth(195)
					.setX(bg.getX()+15).setY(bg.getY()+55);
			
			ArrayList<NpcSkill> oSkills = npc.getType().getSkills();
			ArrayList<NpcSkill> skills = new ArrayList<NpcSkill>();
			
			for(NpcSkill skill : oSkills) {
				if(skill.getId() == NpcSkill.INVULNERABLE) {
					continue;
				} else if(skill.getId() == NpcSkill.MOVEMENT) {
					continue;
				} else if(skill.getId() == NpcSkill.FIGHTING) {
					continue;
				}
				skills.add(skill);
			}
			
			int marginRight = 45;
			
			for(int i = 0; i < skills.size(); i++ ) {
				NpcSkill skill = skills.get(i);
				
				if(skill.getId() == NpcSkill.TRADING) {
					trade = new GenericButton(Text.getText("trade"));
					trade.setAnchor(WidgetAnchor.TOP_LEFT).setHeight(20).setWidth(45)
							.setX(bg.getX()+bg.getWidth()-marginRight).setY(bg.getY()+bg.getHeight()-20);
					attachWidget(plugin, trade);
				}
				
				if(skill.getId() == NpcSkill.REPAIRING) {
					repair = new GenericButton(Text.getText("repair"));
					repair.setAnchor(WidgetAnchor.TOP_LEFT).setHeight(20).setWidth(45)
							.setX(bg.getX()+bg.getWidth()-marginRight).setY(bg.getY()+bg.getHeight()-20);
					attachWidget(plugin, repair);
				}
				
				if(skill.getId() == NpcSkill.HEALING) {
					heal = new GenericButton(Text.getText("heal"));
					heal.setAnchor(WidgetAnchor.TOP_LEFT).setHeight(20).setWidth(45)
							.setX(bg.getX()+bg.getWidth()-marginRight).setY(bg.getY()+bg.getHeight()-20);
					attachWidget(plugin, heal);
				}
				
				if(skill.getId() == NpcSkill.TALKING) {
					talk = new GenericButton(Text.getText("talk"));
					talk.setAnchor(WidgetAnchor.TOP_LEFT).setHeight(20).setWidth(45)
							.setX(bg.getX()+bg.getWidth()-marginRight).setY(bg.getY()+bg.getHeight()-20);
					attachWidget(plugin, talk);
				}
				
				marginRight += 50;
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
		} else if(button.equals(trade)) {
			target.getMainScreen().closePopup();
			new TraderMenu(getNpc(), target);
		} else if(button.equals(talk)) {
			String dialogId = ((Talking)getNpc().getType().getSkillById(NpcSkill.TALKING)).getDialog();
			NpcDialog dialog = NpcDialogController.createNpcDialog(dialogId);
			
			target.getMainScreen().closePopup();
			new Dialog(getNpc(), target, dialog);
		}		
	}
}
