package me.arno.blocklog.listeners;

import java.util.logging.Logger;

import me.arno.blocklog.BlockLog;
import me.arno.blocklog.managers.*;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class BlockLogListener implements Listener {
	public final BlockLog plugin;
	public final Logger log;
		
	public BlockLogListener() {
		this.plugin = BlockLog.plugin;
		this.log = plugin.log;
	}
	
	public void sendAdminMessage(String msg) {
		Bukkit.broadcast(msg, "blocklog.notices");
	}
	
	public SettingsManager getSettingsManager() {
		return plugin.getSettingsManager();
	}
	
	public QueueManager getQueueManager() {
		return plugin.getQueueManager();
	}
	
}
