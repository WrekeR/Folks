package com.thinkamajig.folks.gui.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.event.screen.TextFieldChangeEvent;
import org.getspout.spoutapi.gui.Screen;

import com.thinkamajig.folks.gui.Dialog;
import com.thinkamajig.folks.gui.NpcMenu;
import com.thinkamajig.folks.gui.TraderMenu;

public class GuiListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onButtonClick(ButtonClickEvent event) {
		Screen screen = event.getScreen();
		
		if(screen instanceof NpcMenu) {
			NpcMenu window = (NpcMenu)screen;
			window.getButtonActions(event.getButton(), event.getPlayer());
		} else if(screen instanceof TraderMenu) {
			TraderMenu window = (TraderMenu)screen;
			window.getButtonActions(event.getButton(), event.getPlayer());
		} else if(screen instanceof Dialog) {
			Dialog window = (Dialog)screen;
			window.getButtonActions(event.getButton(), event.getPlayer());
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onTextFieldChange(TextFieldChangeEvent event) {
	    Screen screen = event.getScreen();
		
		if(screen instanceof TraderMenu) {
			TraderMenu window = (TraderMenu)screen;
			window.getTextFieldChangeActions();
		}
    }
}