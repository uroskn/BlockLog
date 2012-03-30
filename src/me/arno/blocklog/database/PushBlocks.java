package me.arno.blocklog.database;

import me.arno.blocklog.BlockLog;
import me.arno.blocklog.Config;
import me.arno.blocklog.log.LoggedBlock;
import me.arno.blocklog.log.LoggedInteraction;

public class PushBlocks {
	BlockLog plugin;
	Config cfg;
	
	public PushBlocks(BlockLog plugin) {
		this.plugin = plugin;
		this.cfg = plugin.cfg;
		startPush();
	}
	
	public void startPush() {
		plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				if(plugin.blocks.size() > 0) {
					LoggedBlock block = plugin.blocks.get(0);
			    	block.save();
			    	plugin.blocks.remove(0);
		    	}
				if(plugin.interactions.size() > 0) {
		    		LoggedInteraction interaction = plugin.interactions.get(0);
		    		interaction.save();
			    	plugin.interactions.remove(0);
		    	}
		    }
		}, 100L, cfg.getConfig().getInt("database.delay") * 20L);
	}
}