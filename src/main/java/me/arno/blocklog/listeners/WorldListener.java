package me.arno.blocklog.listeners;

import me.arno.blocklog.logs.LogType;

import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.StructureGrowEvent;

public class WorldListener extends BlockLogListener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onStructureGrow(StructureGrowEvent event) {
		if(!event.isCancelled()) {
			if(getSettingsManager().isLoggingEnabled(event.getWorld(), LogType.GROW)) {
				Player player = event.getPlayer();
				for(BlockState block : event.getBlocks()) {
					getQueueManager().queueBlockEdit(player, block, LogType.GROW);
				}
			}
		}
	}
	
}
