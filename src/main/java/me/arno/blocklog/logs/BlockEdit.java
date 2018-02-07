package me.arno.blocklog.logs;

import java.sql.SQLException;
import java.sql.Statement;

import me.arno.blocklog.BlockLog;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;


public class BlockEdit {
	private final BlockLog plugin;
	private final LogType type;
	
	private final Player player;
	private final BlockState block;
	private final EntityType entity;
	private final GameMode gamemode;
	private final int blockid;
	private final byte blockdata;
	private final Location loc;
	private final World world;
	private final String pname;
	
	private final long date;
	
	public BlockEdit(Player player, BlockState block, EntityType entity, LogType type) {
		this.plugin = BlockLog.plugin;
		this.player = player;
		this.block = block;
		this.entity = (entity == null) ? EntityType.UNKNOWN : entity;
		this.type = type;
		this.date = (System.currentTimeMillis()/1000);
		this.gamemode  = (player == null) ? GameMode.SURVIVAL : player.getGameMode();
		this.loc = null;
		this.blockid = 0;
		this.blockdata = 0;
		this.world = null;
		this.pname = (player == null) ? "#environment" : player.getName().toLowerCase();
	}
	
	/* ugly hack */
	public BlockEdit(String name, GameMode gm, int blockid, byte blockdata, Location loc, LogType type, World world)
	{
		this.plugin = BlockLog.plugin;
		this.player = null;
		this.blockid = blockid;
		this.blockdata = blockdata;
		this.loc = loc;
		this.type = type;
		this.date = (System.currentTimeMillis()/1000);
		this.gamemode = gm;
		this.entity = EntityType.PLAYER;
		this.block = null;
		this.world = world;
		this.pname = name.toLowerCase();
	}

	public void save() {
		try {
			Statement stmt = plugin.conn.createStatement();
			stmt.executeUpdate("INSERT INTO blocklog_blocks (entity, triggered, block_id, datavalue, gamemode, world, date, x, y, z, type) VALUES ('" + getEntityName() + "', '" + getPlayerName() + "', " + getBlockId() + ", " + getDataValue() + ", " + getPlayerGameMode() + ", '" + getWorld().getName() + "', " + getDate() + ", " + getX() + ", " + getY() + ", " + getZ() + ", " + getTypeId() + ")");
		} catch (SQLException e) {
    		e.printStackTrace();
    	}
	}
	
	public String getEntityName() {
		return entity.toString().toLowerCase();
	}
	
	public EntityType getEntity() {
		return entity;
	}
	
	public int getPlayerGameMode() {
		return gamemode.getValue();
	}
	
	public int getBlockId() {
		if (block != null) return block.getTypeId();
		else return blockid;
	}
	
	public byte getDataValue() {
		if (block != null) return block.getData().getData();
		else return blockdata;
	}
	
	public BlockState getBlock() {
		return block;
	}
	
	public World getWorld() {
		if (block != null) return block.getWorld();
		else return this.world;
	}
	
	public Location getLocation() {
		if (block != null) return block.getLocation();
		else return loc;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public String getPlayerName() {
		return this.pname.replaceAll("'", "''");
	}
	
	public long getDate() {
		return date;
	}
	
	public LogType getType() {
		return type;
	}
	
	public int getTypeId() {
		return getType().getId();
	}
	
	public int getX()
	{
		if (block != null) return block.getLocation().getBlockX();
		else return loc.getBlockX();
	}
	
	public int getY()
	{
		if (block != null) return block.getLocation().getBlockY();
		else return loc.getBlockY();
	}
	
	public int getZ()
	{
		if (block != null) return block.getLocation().getBlockZ();
		else return loc.getBlockZ();
	}
}
