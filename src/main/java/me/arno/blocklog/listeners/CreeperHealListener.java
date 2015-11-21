package me.arno.blocklog.listeners;

import java.util.ArrayList;
import java.util.List;

import me.arno.blocklog.logs.LogType;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.nitnelave.CreeperHeal.events.CHBlockHealEvent;
import com.nitnelave.CreeperHeal.events.CHExplosionRecordEvent;

public class CreeperHealListener extends BlockLogListener {
	
	@EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
	public void CrepperHealEvent(CHBlockHealEvent event)
	{
		final Location loc = event.getBlock().getLocation();
		new BukkitRunnable() {
			 
            public void run() {
            	getQueueManager().queueBlockEditWE(
            			"#creeperHeal",
        				GameMode.SURVIVAL,
        				loc.getBlock().getTypeId(),
        				loc.getBlock().getData(),
        				loc,
        				loc.getWorld(),
        				LogType.PLACE
            	);
            }
		}.runTaskLater(this.plugin, 1);
	}
	
	@EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
	public void explosionEvent(CHExplosionRecordEvent event)
	{
		Bukkit.broadcastMessage("CH!");
		List<BlockState> bs = new ArrayList<BlockState>();
		for (Block bl : event.getBlocks()) bs.add(bl.getState());
		EntityListener.last_event_blocks = bs;
	}
}
