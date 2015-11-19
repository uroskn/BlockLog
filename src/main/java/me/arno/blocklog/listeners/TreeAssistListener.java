package me.arno.blocklog.listeners;

import me.arno.blocklog.logs.LogType;
import me.itsatacoshop247.TreeAssist.events.TALeafDecay;
import me.itsatacoshop247.TreeAssist.events.TATreeBrokenEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class TreeAssistListener extends BlockLogListener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onTreeEvent(TATreeBrokenEvent event)
	{
		if (event.getPlayer() != null) 
			getQueueManager().queueBlockEdit(event.getPlayer(), event.getBlock().getState(), LogType.BREAK);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onCustomLeafEvent(TALeafDecay event)
	{
		getQueueManager().queueBlockEdit(event.getBlock().getState(), LogType.LEAVES);
	}
}
