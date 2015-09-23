package me.arno.blocklog.listeners;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;

import me.arno.blocklog.logs.LogType;
import me.arno.blocklog.util.Query;
import me.arno.blocklog.util.Text;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class WandListener extends BlockLogListener {
	
	public String convertTime(long time){
	    Date date = new Date(time);
	    Format format = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
	    return format.format(date).toString();
	}
	
	public void getBlockEdits(Player player, Location location) {
		try {
			player.sendMessage(ChatColor.YELLOW + "Block History" + ChatColor.BLUE + " (" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ")" + ChatColor.DARK_GRAY + " ------------------------");
            //player.sendMessage(ChatColor.GRAY + Text.addSpaces("Name", 90) + Text.addSpaces("Action", 75) + "Details");
            
			int maxResults = getSettingsManager().getMaxResults();			
			
				Query query = new Query("blocklog_blocks");
				query.select("entity", "triggered", "block_id", "datavalue", "gamemode", "type", "date");
				query.where("x", location.getBlockX());
				query.where("y", location.getBlockY());
				query.where("z", location.getBlockZ());
				query.where("world", location.getWorld().getName());
				query.where("rollback_id", 0);
				query.orderBy("date", "DESC");
				query.limit(maxResults);
				
				ResultSet rs = query.getResult();
				
				boolean found = false;
				while(rs.next()) {
					String name = Material.getMaterial(rs.getInt("block_id")).toString();
					LogType type = LogType.values()[rs.getInt("type")];
					
					found = true;
					
					ChatColor color = ChatColor.DARK_RED;
					if (rs.getInt("gamemode") == 1) color = ChatColor.RED;
					
					ChatColor triggred = ChatColor.GOLD;
					if (rs.getString("triggered").substring(0, 1).compareTo("#") == 0) triggred = ChatColor.YELLOW;
					
					player.sendMessage(ChatColor.AQUA + convertTime(rs.getLong("date")*1000) + " " + triggred + 
							           rs.getString("triggered") + " " + color + type.name() + " " + 
							           ChatColor.GREEN + name + ChatColor.DARK_GREEN + " [#" + rs.getString("block_id") +
							           ":" + rs.getString("datavalue") + "]");
				}
			    if (!found) player.sendMessage("No records found");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		boolean WandEnabled = plugin.users.contains(event.getPlayer().getName());
		if(!event.isCancelled()) {
			if(event.getPlayer().getItemInHand().getType() == getSettingsManager().getWand()  && WandEnabled) {
				if((event.getAction() == Action.RIGHT_CLICK_BLOCK && (!event.getPlayer().getItemInHand().getType().isBlock()) || event.getAction() == Action.LEFT_CLICK_BLOCK)) {
					
					Material type = event.getClickedBlock().getType();
					// LOL, ugly hack!
					if(type == Material.WOODEN_DOOR || type == Material.TRAP_DOOR || type == Material.CHEST || type == Material.DISPENSER || type == Material.STONE_BUTTON || type == Material.LEVER)  { }
					else
						getBlockEdits(event.getPlayer(), event.getClickedBlock().getLocation());
					
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		boolean WandEnabled = plugin.users.contains(event.getPlayer().getName());
		if(!event.isCancelled()) {
			if(event.getPlayer().getItemInHand().getType() == getSettingsManager().getWand() && WandEnabled) {
				if(event.getPlayer().getItemInHand().getType().isBlock()) {
					getBlockEdits(event.getPlayer(), event.getBlockPlaced().getLocation());
					event.setCancelled(true);
				}
			}
		}
	}
}
