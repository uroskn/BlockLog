package me.arno.blocklog.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CommandHelp extends BlockLogCommand {
	public CommandHelp() {
		super("blocklog.help");
		setCommandUsage("/bl help");
	}

	@Override
	public boolean execute(Player player, Command cmd, String[] args) {
		if(args.length > 0) {
			return false;
		}
		
		if(!hasPermission(player)) {
			player.sendMessage("You don't have permission");
			return true;
		}
		
		player.sendMessage(ChatColor.DARK_RED +"[BlockLog] " + ChatColor.GOLD + "Commands");
		player.sendMessage(ChatColor.DARK_RED +"/bl help" + ChatColor.GOLD + " - Shows this message");
				
		if(player.hasPermission("blocklog.config"))
			player.sendMessage(ChatColor.DARK_RED +"/bl config" + ChatColor.GOLD + " - Change blocklog's command values ingame");
		
		if(player.hasPermission("blocklog.queue"))
			player.sendMessage(ChatColor.DARK_RED +"/bl queue" + ChatColor.GOLD + " - Shows the total amount of queued block edits and interactions");
		
		if(player.hasPermission("blocklog.wand"))
			player.sendMessage(ChatColor.DARK_RED +"/bl wand" + ChatColor.GOLD + " - Enables blocklog's wand");
		player.sendMessage(ChatColor.DARK_RED +"/blocklog" + ChatColor.GOLD + " - Basic Information");
		return true;
	}
}
