package me.arno.blocklog.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CommandQueue extends BlockLogCommand {
	public CommandQueue() {
		super("blocklog.queue", true);
		setCommandUsage("/bl queue");
	}

	@Override
	public boolean execute(Player player, Command cmd, String[] args) {
		
		if(!hasPermission(player)) {
			player.sendMessage("You don't have permission");
			return true;
		}
		
		if (args.length == 0)
		{
			if(player == null) {
				log.info(String.format("The queue contains %s block edits", getQueueManager().getEditQueueSize()));
				log.info(String.format("Inrate @ %s b/s, Outrate %s b/s", getQueueManager().GetInRate(), getQueueManager().GetOutRate()));
				log.info(String.format("Written total %s blocks", getQueueManager().GetWritten()));
			} else {
				player.sendMessage(String.format(ChatColor.DARK_RED +"[BlockLog] " + ChatColor.GOLD + "The queue contains %s block edits", getQueueManager().getEditQueueSize()));
				player.sendMessage(String.format(ChatColor.DARK_RED +"[BlockLog] " + ChatColor.GOLD + "Inrate @ %s b/s, Outrate %s b/s", getQueueManager().GetInRate(), getQueueManager().GetOutRate()));
				player.sendMessage(String.format(ChatColor.DARK_RED +"[BlockLog] " + ChatColor.GOLD + "Written total %s blocks", getQueueManager().GetWritten()));
			}
		}
		else
		{
			if (args[0].equalsIgnoreCase("write"))
			{
				if (player == null) log.info(String.format("Queue flushing enabled status: %s", (new Boolean(this.plugin.SetSaving()).toString())));
				else player.sendMessage(String.format(ChatColor.DARK_RED +"[BlockLog] " + ChatColor.GOLD + "Queue flushing enabled status: %s", (new Boolean(this.plugin.SetSaving()).toString())));
			}
		}
		return true;
	}
}
