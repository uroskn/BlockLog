package me.arno.blocklog.worldedit;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bags.BlockBag;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;

import me.arno.blocklog.BlockLog;
import me.arno.blocklog.logs.LogType;

public class BlockLogEditSession extends EditSession {
	
	private LocalPlayer player;
	private BlockLog plugin;

	/**
	 * {@inheritDoc}
	 */
	public BlockLogEditSession(LocalWorld world, int maxBlocks, LocalPlayer player, BlockLog lb) {
		super(world, maxBlocks);
		this.player = player;
		this.plugin = lb;
	}

	/**
	 * {@inheritDoc}
	 */
	public BlockLogEditSession(LocalWorld world, int maxBlocks, BlockBag blockBag, LocalPlayer player, BlockLog lb) {
		super(world, maxBlocks, blockBag);
		this.player = player;
		this.plugin = lb;
	}

	@Override
	public boolean rawSetBlock(Vector pt, BaseBlock block) {
		if (!this.plugin.getConfig().getBoolean("welog"))
		{
			return super.rawSetBlock(pt, block);
		}
		int typeBefore = ((BukkitWorld) player.getWorld()).getWorld().getBlockTypeIdAt(pt.getBlockX(), pt.getBlockY(), pt.getBlockZ());
		byte dataBefore = ((BukkitWorld) player.getWorld()).getWorld().getBlockAt(pt.getBlockX(), pt.getBlockY(), pt.getBlockZ()).getData();
		boolean success = super.rawSetBlock(pt, block);
		if (success) {
			Location location = new Location(((BukkitWorld) player.getWorld()).getWorld(), pt.getBlockX(), pt.getBlockY(), pt.getBlockZ());

			GameMode gm = GameMode.SURVIVAL;
			if (player.hasCreativeMode()) gm = GameMode.CREATIVE;
			if (typeBefore != Material.AIR.getId()) plugin.getQueueManager().queueBlockEditWE(player.getName(), gm, typeBefore, dataBefore, location, location.getWorld(), LogType.WE_BREAK);
			if (block.getType() != Material.AIR.getId()) plugin.getQueueManager().queueBlockEditWE(player.getName(), gm, block.getType(), (byte)block.getData(), location, location.getWorld(), LogType.WE_PLACE);
			
		}
		return success;
	}
}
