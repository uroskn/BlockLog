package me.arno.blocklog.listeners;

import me.arno.blocklog.logs.LogType;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import com.sk89q.worldedit.blocks.BlockType;

public class BlockListener extends BlockLogListener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockPlace(BlockPlaceEvent event) {
		BlockState block = event.getBlock().getState();
		Player player = event.getPlayer();
		
		Boolean cancel = !getSettingsManager().isLoggingEnabled(player.getWorld(), LogType.PLACE);
		
		boolean WandEnabled = plugin.users.contains(event.getPlayer().getName());
		
		if(event.getPlayer().getItemInHand().getType() == getSettingsManager().getWand() && WandEnabled)
			cancel = true;
		
		if(!event.isCancelled() && !cancel) {
			getQueueManager().queueBlockEdit(player, block, EntityType.PLAYER, LogType.PLACE);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent event) {
		BlockState block = event.getBlock().getState();
		Player player = event.getPlayer();
		
		if(!event.isCancelled() && getSettingsManager().isLoggingEnabled(player.getWorld(), LogType.BREAK)) {
			getQueueManager().queueBlockEdit(player, block, LogType.BREAK);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockFromTo(BlockFromToEvent event) {
		if(!event.isCancelled()) {
			if(event.getBlock().getType() == Material.DRAGON_EGG) {
				BlockState blockState = event.getToBlock().getState();
				blockState.setType(Material.DRAGON_EGG);
				
				if(getSettingsManager().isLoggingEnabled(event.getBlock().getWorld(), LogType.BREAK, LogType.PLACE)) {
					getQueueManager().queueBlockEdit(event.getBlock().getState(), LogType.BREAK);
					getQueueManager().queueBlockEdit(blockState, LogType.PLACE);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBurn(BlockBurnEvent event) {
		if(!event.isCancelled() && getSettingsManager().isLoggingEnabled(event.getBlock().getWorld(), LogType.FIRE)) {
			getQueueManager().queueBlockEdit(event.getBlock().getState(), LogType.FIRE);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockIgnite(BlockIgniteEvent event) {
		if(!event.isCancelled() && event.getPlayer() != null) {
			if(event.getBlock().getType() == Material.TNT && getSettingsManager().isLoggingEnabled(event.getPlayer().getWorld(), LogType.BREAK)) {
				getQueueManager().queueBlockEdit(event.getPlayer(), event.getBlock().getState(), LogType.BREAK);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onLeavesDecay(LeavesDecayEvent event) {
		if(!event.isCancelled()) {
			if(getSettingsManager().isLoggingEnabled(event.getBlock().getWorld(), LogType.LEAVES)) {
				getQueueManager().queueBlockEdit(event.getBlock().getState(), LogType.LEAVES);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockForm(BlockFormEvent event) {
		if(!event.isCancelled()) {
			if(getSettingsManager().isLoggingEnabled(event.getNewState().getWorld(), LogType.FORM)) {
				getQueueManager().queueBlockEdit(event.getNewState(), LogType.FORM);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void EntityChangeBlockEvent(EntityChangeBlockEvent event)
	{
		if ((event.isCancelled()) || (event.getEntity().getType() == EntityType.FALLING_BLOCK)) return;
		BlockState state = event.getBlock().getState();
		LogType type = null;
		if (event.getEntity().getType() == EntityType.SHEEP) type = LogType.SHEEP;		
		//else if (event.getEntity().getType() == EntityType.WITHER) type = LogType.WITHER;
		else if (event.getEntity().getType() == EntityType.ENDERMAN)
		{
			if (event.getTo() == Material.AIR) type = LogType.ENDERMEN_PICKUP;
			else 
			{
				type = LogType.ENDERMEN_PLACE;
				state = event.getBlock().getState();
				state.setType(event.getTo());
			}
		}
		else if (event.getEntity().getType() == EntityType.SILVERFISH)
		{
			if (event.getTo() == Material.AIR) type = LogType.SILVERFISH_BREAK;
			else 
			{
				type = LogType.SILVERFISH_EGG;
				state = event.getBlock().getState();
				state.setType(event.getTo());
			}
		}
		else if (event.getEntity().getType() == EntityType.BOAT)
		{
			type = LogType.BOAT;
			state = event.getBlock().getState();
			state.setType(event.getTo());
		}
		if ((state == null) || (type == null)) return;
		getQueueManager().queueBlockEdit(state, event.getEntityType(), type);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockSpread(BlockSpreadEvent event) {
		if(!event.isCancelled()) {
			if(getSettingsManager().isLoggingEnabled(event.getNewState().getWorld(), LogType.SPREAD)) {
				getQueueManager().queueBlockEdit(event.getNewState(), LogType.SPREAD);
			}
		}
	}
	
	
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockFade(BlockFadeEvent event) {
		if((!event.isCancelled()) && (event.getNewState().getType() != Material.GRASS)){
			BlockState result = event.getNewState();
			if ((event.getNewState().getType() == Material.AIR) && 
				((event.getBlock().getType() == Material.SNOW) || (event.getBlock().getType() == Material.FIRE)))
			   result = event.getBlock().getState();
			if(getSettingsManager().isLoggingEnabled(event.getNewState().getWorld(), LogType.FADE)) {
				getQueueManager().queueBlockEdit(result, LogType.FADE);
			}
		}
	}
}
