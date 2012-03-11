package com.thinkamajig.folks.gui;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.gui.Button;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericItemWidget;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericTextField;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.Texture;
import org.getspout.spoutapi.gui.WidgetAnchor;

import org.getspout.spoutapi.player.SpoutPlayer;

import com.thinkamajig.folks.action.Buy;
import com.thinkamajig.folks.action.Sell;
import com.thinkamajig.folks.bank.Bank;
import com.thinkamajig.folks.npc.inventory.NpcInventory;
import com.thinkamajig.folks.npc.inventory.NpcInventoryController;
import com.thinkamajig.folks.npc.Npc;
import com.thinkamajig.folks.npc.skill.NpcSkill;
import com.thinkamajig.folks.npc.skill.Trading;


public class TraderMenu extends Gui {
	
	private Texture bg, closeTex;
	private GenericLabel npcName, npcProfession, balanceCurr, balanceDiff;
	private Button close, buy, sell;
	
	private ArrayList<GenericLabel> amountLabels = new ArrayList<GenericLabel>();
	private ArrayList<GenericTextField> amountTextFields = new ArrayList<GenericTextField>();
	
	public TraderMenu(Npc trader, SpoutPlayer target) {
		super(trader, target);
		
		NpcInventory inv = ((Trading)trader.getType().getSkillById(NpcSkill.TRADING)).getInventory();
		
		if(inv != null) {
			ArrayList<ItemStack> items = inv.getItems();
			
			if(target.isSpoutCraftEnabled()) {
				bg = new GenericTexture("http://thinkamajig.net/mc/folks/texture/traderbg.png");
				
				int screenWidth = target.getMainScreen().getWidth();
				int screenHeight = target.getMainScreen().getHeight();
				
				bg.setAnchor(WidgetAnchor.TOP_LEFT).setHeight(175).setWidth(225)
						.setX((screenWidth-bg.getWidth())/2).setY((screenHeight-bg.getHeight())/2);
				bg.setPriority(RenderPriority.Highest);
				
				npcName = new GenericLabel(trader.getName());
				npcName.setAnchor(WidgetAnchor.TOP_LEFT)
						.setHeight(10).setWidth(GenericLabel.getStringWidth(npcName.getText()))
						.setX(bg.getX()+15).setY(bg.getY()+7);
				//npcName.setTextColor(new Color(77, 56, 30));
				
				npcProfession = new GenericLabel(trader.getType().getName());
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
				
				balanceCurr = new GenericLabel(Text.getText("balance") + ": $" +
						Bank.getPlayerBalance(target));
				balanceCurr.setAnchor(WidgetAnchor.TOP_LEFT)
						.setHeight(10).setWidth(GenericLabel.getStringWidth(balanceCurr.getText()))
						.setX(npcName.getX()).setY(bg.getY()+bg.getHeight()-balanceCurr.getHeight()-14);
				
				balanceDiff = new GenericLabel(Text.getText("worth") + ": $0");
				balanceDiff.setAnchor(WidgetAnchor.TOP_LEFT)
						.setHeight(10).setWidth(GenericLabel.getStringWidth(balanceDiff.getText()))
						.setX(npcName.getX()).setY(bg.getY()+bg.getHeight()-balanceCurr.getHeight()-4);
				
				buy = new GenericButton(Text.getText("buy"));
				buy.setAnchor(WidgetAnchor.TOP_LEFT).setHeight(20).setWidth(35)
						.setX(bg.getX()+bg.getWidth()-buy.getWidth()).setY(bg.getY()+bg.getHeight()-20);
				
				sell = new GenericButton(Text.getText("sell"));
				sell.setAnchor(WidgetAnchor.TOP_LEFT).setHeight(20).setWidth(35)
						.setX(buy.getX()-sell.getWidth()-5).setY(buy.getY());
				
				for(int i = 0; i < 4; i++) {
					ItemStack item = items.get(i);
					String itemId = NpcInventoryController.makeItemId(item);
					String name = NpcInventoryController.getItemName(inv, itemId);
					String costs = String.valueOf(NpcInventoryController.getItemCosts(inv, itemId));
					String income = String.valueOf(NpcInventoryController.getItemIncome(inv, itemId));
												
					GenericItemWidget itemWidget = new GenericItemWidget(item);						
					itemWidget.setData(item.getData().getData());
					itemWidget.setAnchor(WidgetAnchor.TOP_LEFT);
					itemWidget.setHeight(8).setWidth(8).setDepth(8).setX(bg.getX()+15)
							.setY(bg.getY()+27*i+44);
					itemWidget.setTooltip(name);
					
					GenericLabel itemLabel = new GenericLabel(name);
					itemLabel.setAnchor(WidgetAnchor.TOP_LEFT)
							.setHeight(10).setWidth(GenericLabel.getStringWidth(itemLabel.getText()))
							.setX(itemWidget.getX()+itemWidget.getWidth()+15).setY(itemWidget.getY()-1);
					//itemLabel.setTextColor(new Color(77, 56, 30));
					
					GenericLabel itemAmount = new GenericLabel(String.valueOf(item.getAmount()));
					itemAmount.setAnchor(WidgetAnchor.TOP_LEFT)
							.setHeight(10).setWidth(GenericLabel.getStringWidth(itemAmount.getText()))
							.setX(itemLabel.getX()).setY(itemLabel.getY()+10);
					//itemAmount.setTextColor(new Color(77, 56, 30));
					amountLabels.add(itemAmount);
					
					GenericLabel itemCosts = new GenericLabel("-$"+costs);
					itemCosts.setAnchor(WidgetAnchor.TOP_LEFT)
							.setHeight(10).setWidth(GenericLabel.getStringWidth(itemCosts.getText()))
							.setX(itemLabel.getX()+100).setY(itemLabel.getY());
					//itemCosts.setTextColor(new Color(205, 32, 32));
					
					GenericLabel itemIncome = new GenericLabel("+$"+income);
					itemIncome.setAnchor(WidgetAnchor.TOP_LEFT)
							.setHeight(10).setWidth(GenericLabel.getStringWidth(itemIncome.getText()))
							.setX(itemCosts.getX()).setY(itemAmount.getY());
					//itemIncome.setTextColor(new Color(87, 191, 56));
					
					GenericTextField tfBuyAmount = new GenericTextField();
					tfBuyAmount.setHeight(18).setWidth(30).setX(bg.getX()+bg.getWidth()-tfBuyAmount.getWidth()-15)
							.setY(itemWidget.getY());
					tfBuyAmount.setPlaceholder("0").setBorderColor(new Color(111, 82, 43))
							.setFieldColor(new Color(227, 215, 177)).setMaximumCharacters(3);
					
					amountTextFields.add(tfBuyAmount);
					
					attachWidget(plugin, itemWidget);
					attachWidget(plugin, itemLabel);
					attachWidget(plugin, itemAmount);
					attachWidget(plugin, itemCosts);
					attachWidget(plugin, itemIncome);
					attachWidget(plugin, tfBuyAmount);
				}
			
				attachWidget(plugin, buy);
				attachWidget(plugin, sell);
				attachWidget(plugin, closeTex);
				attachWidget(plugin, close);				
				attachWidget(plugin, npcName);
				attachWidget(plugin, npcProfession);
				attachWidget(plugin, balanceCurr);
				attachWidget(plugin, balanceDiff);
				attachWidget(plugin, bg);
				
				target.getMainScreen().attachPopupScreen(this);
			} else {
				String greeting = Text.getRandomText("greetings");
				String intro = Text.getRandomText("intros");
				String name = trader.getName();
				String profession = trader.getType().getName();
				String request = Text.getRandomText("requests");
				String offers = Text.getRandomText("offers");
				
				target.sendMessage(GOLD + greeting + " " + 
						intro + " " + YELLOW + "[" + profession + "] " + name + GOLD + ".");
				target.sendMessage(GOLD + request);
				target.sendMessage(GOLD + offers);
				
				for(int i = 0; i < 6; i++) {
					if(i < inv.getItems().size()) {
						String itemId = NpcInventoryController.makeItemId(items.get(i));
						
						target.sendMessage(GRAY + "[" + i + "] " + 
								NpcInventoryController.getItemName(inv, itemId) + 
								" (" + items.get(i).getAmount() + "x)" + PURPLE +
								" ($" + NpcInventoryController.getItemCosts(inv, itemId) + " K)" + GREEN +
								" ($" + NpcInventoryController.getItemIncome(inv, itemId) + " VK)");
					} else {
						target.sendMessage(" ");
					}
				}
			}
		}
	}
	
	public void getButtonActions(Button button, SpoutPlayer target) {
		if(button.equals(close)) {
			target.getMainScreen().closePopup();
		} else if(button.equals(buy)) {
			for(int i = 0; i < amountTextFields.size(); i++) {
				String text = amountTextFields.get(i).getText();
				
				if(!text.equals("")) {
					int amount = Integer.parseInt(text);
					
					boolean bought = Buy.buy((Player)target, i, amount);
					
					if(bought) {
						GenericLabel label = amountLabels.get(i);
						int diff = Integer.parseInt(label.getText()) - amount;
						label.setText(String.valueOf(diff));
						
						balanceCurr.setText(Text.getText("balance") + ": $" +
								Bank.getPlayerBalance(target));
					}					
				}
			}
		} else if(button.equals(sell)) {
			for(int i = 0; i < amountTextFields.size(); i++) {
				String text = amountTextFields.get(i).getText();
				
				if(!text.equals("")) {
					int amount = Integer.parseInt(text);
					
					boolean sold = Sell.sell((Player)target, i, amount);
					
					if(sold) {
						GenericLabel label = amountLabels.get(i);
						int diff = Integer.parseInt(label.getText()) + amount;
						label.setText(String.valueOf(diff));
						
						balanceCurr.setText(Text.getText("balance") + ": $" +
								Bank.getPlayerBalance(target));
					}					
				}
			}
		}
	}
	
	public void getTextFieldChangeActions() {
		int balanceDiffAmount = 0;
		
		for(GenericTextField tf : amountTextFields) {
			String text = tf.getText();
			int amount = 0;
			
			if(!text.equals("")) {
				amount = Integer.parseInt(text);
			}
			
			balanceDiffAmount += amount;
		}
		
		balanceDiff.setText(Text.getText("worth") + ": $" + balanceDiffAmount);
	}
}