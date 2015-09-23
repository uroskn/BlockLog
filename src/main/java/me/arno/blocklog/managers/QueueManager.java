package me.arno.blocklog.managers;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import me.arno.blocklog.logs.LogType;
import me.arno.blocklog.logs.BlockEdit;

import net.knuples.misc.StatCounter;

public class QueueManager extends BlockLogManager {
	private final ConcurrentLinkedQueue<BlockEdit> blockEdits = new ConcurrentLinkedQueue<BlockEdit>();
	
	private StatCounter inrate = new StatCounter();
	private StatCounter outrate = new StatCounter();
	
	/**
	 * Logs a block edit by the environment.
	 * This can be either a block that has been created or a block that has been destroyed
	 * 
	 * @param block {@link BlockState} of the block that got destroyed
	 * @param type {@link LogType} of the log
	 */
	public void queueBlockEdit(BlockState block, LogType type) {
		queueBlockEdit(null, block, EntityType.UNKNOWN, type);
	}
	
	/**
	 * Logs a block edit by a player.
	 * This can be either a block that has been created or a block that has been destroyed
	 * 
	 * @param player The player that triggered the event
	 * @param block {@link BlockState} of the block that got destroyed
	 * @param type {@link LogType} of the log
	 */
	public void queueBlockEdit(Player player, BlockState block, LogType type) {
		queueBlockEdit(player, block, EntityType.PLAYER, type);
	}
	
	/**
	 * Logs a block edit by an entity other than a player.
	 * This can be either a block that has been created or a block that has been destroyed
	 * 
	 * @param block {@link BlockState} of the block that got destroyed
	 * @param entity {@link EntityType} of the entity that triggered this event
	 * @param type {@link LogType} of the log
	 */
	public void queueBlockEdit(BlockState block, EntityType entity, LogType type) {
		queueBlockEdit(null, block, entity, type);
	}
	
	/**
	 * Logs a block edit by an entity that got triggered by a player.
	 * This can be either a block that has been created or a block that has been destroyed
	 * 
	 * @param player The {@link Player} that triggered the event
	 * @param block {@link BlockState} of the block that got destroyed
	 * @param entity {@link EntityType} of the entity that triggered this event
	 * @param type {@link LogType} of the log
	 */
	public void queueBlockEdit(Player player, BlockState block, EntityType entity, LogType type) {
		inrate.Count();
		blockEdits.add(new BlockEdit(player, block, entity, type));
	}
	
	public void queueBlockEditWE(String name, GameMode gm, int blockid, byte blockdata, Location loc, org.bukkit.World world, LogType type)
	{
		inrate.Count();
		blockEdits.add(new BlockEdit(name, gm, blockid, blockdata, loc, type, world));
	}
	
	/**
	 * Gets a list of unsaved block edits
	 * 
	 * @return An {@link ArrayList} containing all the unsaved block edits
	 */
	public ConcurrentLinkedQueue<BlockEdit> getEditQueue() {
		return blockEdits;
	}
	
	/**
	 * Gets the amount of unsaved block edits
	 * 
	 * @return An integer value that represents the amount of unsaved block edits
	 */
	public int getEditQueueSize() {
		return blockEdits.size();
	}
	
	public void TickSave()
	{
		outrate.Count();
	}
	
	public int GetInRate()
	{
		return inrate.GetRate();
	}
	
	public int GetOutRate()
	{
		return outrate.GetRate();
	}

	
}
