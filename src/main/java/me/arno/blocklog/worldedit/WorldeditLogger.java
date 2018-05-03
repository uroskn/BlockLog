package me.arno.blocklog.worldedit;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.Location;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.logging.AbstractLoggingExtent;

import me.arno.blocklog.BlockLog;
import me.arno.blocklog.logs.LogType;

public class WorldeditLogger extends AbstractLoggingExtent {
    private final Actor actor;
    private final BlockLog plugin;
 
    public WorldeditLogger(Actor actor, Extent extent, BlockLog plugin) {
        super(extent);
        this.actor = actor;
        this.plugin = plugin;
    }
 
    @Override
    protected void onBlockChange(Vector position, BaseBlock newBlock) {
    	if (!this.plugin.getConfig().getBoolean("welog"))
    		return;
    	
        BaseBlock oldBlock = getBlock(position);      
        Player player = Bukkit.getPlayer(this.actor.getUniqueId());
        
        GameMode gm = GameMode.SURVIVAL;
		if (player.getGameMode() == GameMode.CREATIVE) 
			gm = GameMode.CREATIVE;
		
		Location loc = new Location(
				player.getWorld(),
				position.getBlockX(), position.getBlockY(), position.getBlockZ()
		);
		
		// No change.
		if ((oldBlock.getType() == newBlock.getType()) && (oldBlock.getData() == newBlock.getData()))
			return;
		
		if (oldBlock.getType() != Material.AIR.getId())
		{
			plugin.getQueueManager().queueBlockEditWE(
					player.getName(), gm, oldBlock.getId(), (byte) oldBlock.getData(), 
					loc, loc.getWorld(), LogType.WE_BREAK
			);
		}
		
		if (newBlock.getType() != Material.AIR.getId())
		{
			plugin.getQueueManager().queueBlockEditWE(
					player.getName(), gm, newBlock.getId(), (byte) newBlock.getData(), 
					loc, loc.getWorld(), LogType.WE_PLACE
			);
		}
    }
}