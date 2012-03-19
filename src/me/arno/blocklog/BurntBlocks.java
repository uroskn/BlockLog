package me.arno.blocklog;


import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class BurntBlocks {
	public BlockLog plugin;
	
	private Block block;
	
	private long date;
	
	public BurntBlocks(BlockLog plugin, Block block) {
		this.plugin = plugin;
		this.block = block;
		this.date = System.currentTimeMillis()/1000;
	}
	
	public void push() {
		//plugin.blocks.add(new LoggedBlock(this));
	}
	
	public int getId() {
		return block.getTypeId();
	}
	
	public Block getBlock() {
		return block;
	}
	
	public World getWorld() {
		return block.getWorld();
	}
	
	public Location getLocation() {
		return block.getLocation();
	}
	
	public long getDate() {
		return date;
	}
}
