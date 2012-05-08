package me.arno.blocklog.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CommandStorage extends BlockLogCommand {
	public CommandStorage() {
		super("blocklog.storage", true);
		setCommandUsage("/bl storage");
	}

	@Override
	public boolean execute(Player player, Command cmd, String[] args) {
		if(args.length > 0)
			return false;
		
		if(!hasPermission(player)) {
			player.sendMessage("You don't have permission");
			return true;
		}
		
		if(player == null) {
			log.info(String.format("The internal storage contains %s block(s)!", plugin.getBlocks().size()));
			log.info(String.format("The internal storage contains %s interaction(s)!", plugin.getInteractions().size()));
		} else {
			player.sendMessage(String.format(ChatColor.DARK_RED +"[BlockLog] " + ChatColor.GOLD + "The internal storage contains %s block(s)!", plugin.getBlocks().size()));
			player.sendMessage(String.format(ChatColor.DARK_RED +"[BlockLog] " + ChatColor.GOLD + "The internal storage contains %s interaction(s)!", plugin.getInteractions().size()));
		}
		return true;
	}
}
