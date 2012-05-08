package me.arno.blocklog.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import me.arno.blocklog.database.Query;
import me.arno.blocklog.logs.LogType;
import me.arno.blocklog.logs.LoggedBlock;
import me.arno.blocklog.util.Text;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandWand extends BlockLogCommand {
	public CommandWand() {
		super("blocklog.wand");
		setCommandUsage("/bl wand [target]");
	}
	
	@Override
	public boolean execute(Player player, Command cmd, String[] args) {
		if(args.length > 1)
			return false;
		
		if(!hasPermission(player)) {
			player.sendMessage("You don't have permission");
			return true;
		}
		
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("target")) {
				getBlockEdits(player, player.getTargetBlock(null, 0).getLocation());
				return true;
			}
		}
		
		HashMap<String, ItemStack> playerItemStack = plugin.playerItemStack;
		HashMap<String, Integer> playerItemSlot = plugin.playerItemSlot;
		
		Material wand = Material.getMaterial(getConfig().getInt("blocklog.wand"));
		
		if(player.getInventory().contains(wand) && !playerItemStack.containsKey(player.getName())) {
			if(plugin.users.isEmpty()) {
				plugin.users.add(player.getName());
				player.sendMessage(ChatColor.DARK_RED +"[BlockLog] " + ChatColor.GOLD + "Wand enabled!");
			} else if(plugin.users.contains(player.getName())) {
				plugin.users.remove(player.getName());
				player.sendMessage(ChatColor.DARK_RED +"[BlockLog] " + ChatColor.GOLD + "Wand disabled!");
			} else {
				plugin.users.add(player.getName());
				player.sendMessage(ChatColor.DARK_RED +"[BlockLog] " + ChatColor.GOLD + "Wand enabled!");
			}
		} else if(!player.getInventory().contains(wand) && plugin.users.contains(player.getName())) {
			plugin.users.remove(player.getName());
			player.sendMessage(ChatColor.DARK_RED +"[BlockLog] " + ChatColor.GOLD + "Wand disabled!");
		} else {
			if(plugin.users.isEmpty()) {
				playerItemStack.put(player.getName(), player.getItemInHand());
				playerItemSlot.put(player.getName(), player.getInventory().getHeldItemSlot());
				
				player.setItemInHand(new ItemStack(wand, 1));
				
				plugin.users.add(player.getName());
				player.sendMessage(ChatColor.DARK_RED +"[BlockLog] " + ChatColor.GOLD + "Wand enabled!");
			} else if(plugin.users.contains(player.getName())) {
				ItemStack itemStack = playerItemStack.get(player.getName());
				Material itemInHand = player.getItemInHand().getType();
				int invSlot = playerItemSlot.get(player.getName());
				int itemInHandSlot = player.getInventory().getHeldItemSlot();
				
				if(itemInHandSlot == invSlot || (itemInHand == wand && itemInHandSlot != invSlot))
					player.setItemInHand(itemStack);
				else
					player.getInventory().setItem(invSlot, itemStack);
				
				plugin.users.remove(player.getName());
				player.sendMessage(ChatColor.DARK_RED +"[BlockLog] " + ChatColor.GOLD + "Wand disabled!");
			} else {
				playerItemStack.put(player.getName(), player.getItemInHand());
				playerItemSlot.put(player.getName(), player.getInventory().getHeldItemSlot());
				
				player.setItemInHand(new ItemStack(wand, 1));
				
				plugin.users.add(player.getName());
				player.sendMessage(ChatColor.DARK_RED +"[BlockLog] " + ChatColor.GOLD + "Wand enabled!");
			}
		}
		return true;
	}
	
	public void getBlockEdits(Player player, Location location) {
		try {
			player.sendMessage(ChatColor.YELLOW + "Block History" + ChatColor.BLUE + " (" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ")" + ChatColor.DARK_GRAY + " ------------------------");
            player.sendMessage(ChatColor.GRAY + Text.addSpaces("Name", 90) + Text.addSpaces("Reason", 75) + "Details");
            
            Integer BlockNumber = 0;
			Integer BlockCount = 0;
			Integer BlockSize = plugin.getBlocks().size();
			
			while(BlockSize > BlockNumber)
			{
				LoggedBlock LBlock = plugin.getBlocks().get(BlockNumber); 
				if(LBlock.getX() == location.getX() && LBlock.getY() == location.getY() && LBlock.getZ() == location.getZ() && LBlock.getWorld() == location.getWorld()) {
					if(BlockCount == getConfig().getInt("blocklog.results"))
						break;
					
					Calendar calendar = GregorianCalendar.getInstance();
					calendar.setTimeInMillis(LBlock.getDate() * 1000);
					
					String date =  calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
					
					String name = Material.getMaterial(LBlock.getBlockId()).toString();
					LogType type = LBlock.getType();
					
					if(type.getId() <= 12 && type.getId() >= 10)
						type = LogType.EXPLOSION;
					
					player.sendMessage(Text.addSpaces(ChatColor.GOLD + LBlock.getPlayerName(), 99) + Text.addSpaces(ChatColor.DARK_RED + type.name(), 80) + ChatColor.GREEN + name + ChatColor.AQUA + " [" + date + "]");
					BlockCount++;
				}
				BlockNumber++;
			}
			
			
			// Database Results
			Query query = new Query("blocklog_blocks");
			query.addSelect("entity", "trigered", "block_id", "type");
			query.addSelectDateAs("date", "date");
			query.addWhere("x", location.getBlockX());
			query.addWhere("y", location.getBlockY());
			query.addWhere("z", location.getBlockZ());
			query.addWhere("world", location.getWorld().getName());
			query.addOrderBy("date", "DESC");
			
			ResultSet rs = query.getResult();
			
			while(rs.next()) {
				String name = Material.getMaterial(rs.getInt("block_id")).toString();
				LogType type = LogType.values()[rs.getInt("type")];
				
				if(type.getId() <= 12 && type.getId() >= 10)
					type = LogType.EXPLOSION;
				
				player.sendMessage(Text.addSpaces(ChatColor.GOLD + rs.getString("trigered"), 99) + Text.addSpaces(ChatColor.DARK_RED + type.name(), 81) + ChatColor.GREEN + name + ChatColor.AQUA + " [" + rs.getString("date") + "]");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
