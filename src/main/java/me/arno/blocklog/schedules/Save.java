package me.arno.blocklog.schedules;

import java.sql.SQLException;
import me.arno.blocklog.BlockLog;
import me.arno.blocklog.managers.QueueManager;

public class Save extends Thread {
	private final BlockLog plugin;
	
	public boolean halt;
	public boolean asave;

	public Save() {
		this.halt = false;
		this.asave = true;
		this.plugin = BlockLog.plugin;
	}
	
	@Override
	public void run() {
		this.halt = false;
		while(true) {
			try {
				if(plugin.conn == null) plugin.conn = plugin.getDatabaseManager().getConnection();
				if(plugin.conn.isClosed()) plugin.conn = plugin.getDatabaseManager().getConnection();
				while ((!getQueueManager().getEditQueue().isEmpty()) && (this.asave))
				{
					getQueueManager().getEditQueue().poll().save();
					getQueueManager().TickSave();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (this.halt) return;
			/* Ugly hack */
			try { Thread.sleep(100, 0); }
			catch(Exception ie) { }; 
		}
	}
	
	private QueueManager getQueueManager() {
		return BlockLog.plugin.getQueueManager();
	}
}
