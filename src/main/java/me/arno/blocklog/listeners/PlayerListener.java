package me.arno.blocklog.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.arno.blocklog.logs.LogType;

public class PlayerListener extends BlockLogListener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		BlockState block = event.getBlockClicked().getRelative(event.getBlockFace()).getState();
		Player player = event.getPlayer();
		
		if(!event.isCancelled() && getSettingsManager().isLoggingEnabled(player.getWorld(), LogType.PLACE)) {
			if(event.getBucket() == Material.WATER_BUCKET)
				block.setType(Material.WATER);
			else if(event.getBucket() == Material.LAVA_BUCKET)
				block.setType(Material.LAVA);
			
			getQueueManager().queueBlockEdit(player, block, LogType.PLACE);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(!event.isCancelled() && getSettingsManager().isLoggingEnabled(event.getPlayer().getWorld(), LogType.FIRE)) {
			Block block;
			block = event.getClickedBlock().getRelative(BlockFace.UP);
			if (block != null)
			{
				if(block.getType() != Material.FIRE)
					block = event.getClickedBlock().getRelative(BlockFace.NORTH);
				if(block.getType() != Material.FIRE)
					block = event.getClickedBlock().getRelative(BlockFace.EAST);
				if(block.getType() != Material.FIRE)
					block = event.getClickedBlock().getRelative(BlockFace.SOUTH);
				if(block.getType() != Material.FIRE)
					block = event.getClickedBlock().getRelative(BlockFace.WEST);
				if(block.getType() == Material.FIRE) {
					getQueueManager().queueBlockEdit(event.getPlayer(), block.getState(), LogType.BREAK);
				}
			}
		}
	}
	
}
