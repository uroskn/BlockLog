package me.arno.blocklog.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

import me.arno.blocklog.logs.LogType;
import me.arno.blocklog.util.Query;
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
				getBlockEdits(player, player.getTargetBlock((HashSet<Byte>)null, 0).getLocation());
				return true;
			}
		}
		
		HashMap<String, ItemStack> playerItemStack = plugin.playerItemStack;
		HashMap<String, Integer> playerItemSlot = plugin.playerItemSlot;
		
		Material wand = getSettingsManager().getWand();
		
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
            player.sendMessage(ChatColor.GRAY + Text.addSpaces("Name", 90) + Text.addSpaces("Action", 75) + "Details");
            
			int maxResults = getSettingsManager().getMaxResults();
			
				Query query = new Query("blocklog_blocks");
				query.select("entity", "triggered", "block_id", "type");
				query.selectDate("date");
				query.where("x", location.getBlockX());
				query.where("y", location.getBlockY());
				query.where("z", location.getBlockZ());
				query.where("world", location.getWorld().getName());
				query.orderBy("date", "DESC");
				query.limit(maxResults);
				
				ResultSet rs = query.getResult();
				
				while(rs.next()) {
					String name = Material.getMaterial(rs.getInt("block_id")).toString();
					LogType type = LogType.values()[rs.getInt("type")];
					
					player.sendMessage(Text.addSpaces(ChatColor.GOLD + rs.getString("triggered"), 99) + Text.addSpaces(ChatColor.DARK_RED + type.name(), 81) + ChatColor.GREEN + name + ChatColor.AQUA + " [" + rs.getString("date") + "]");
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
