package me.arno.blocklog;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import me.arno.blocklog.Config;
import me.arno.blocklog.commands.*;
import me.arno.blocklog.listeners.*;
import me.arno.blocklog.logs.LogType;
import me.arno.blocklog.managers.*;
import me.arno.blocklog.schedules.Save;
import me.arno.blocklog.util.Text;
import me.arno.blocklog.worldedit.BlockLogEditSessionFactory;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Bukkit.getPluginManager;

public class BlockLog extends JavaPlugin {
	public static BlockLog plugin;
	public Logger log;
	public Connection conn;
	
	private SettingsManager settingsManager;
	private DatabaseManager databaseManager;
	private QueueManager queueManager;
	private Save saveThread;
	
	public ArrayList<String> users = new ArrayList<String>();
	public HashMap<String, ItemStack> playerItemStack = new HashMap<String, ItemStack>();
	public HashMap<String, Integer> playerItemSlot = new HashMap<String, Integer>();
	
	public SettingsManager getSettingsManager() {
		return settingsManager;
	}
	
	public DatabaseManager getDatabaseManager() {
		return databaseManager;
	}
	
	public QueueManager getQueueManager() {
		return queueManager;
	}
	
	private void loadConfiguration() {
		Config worldConfig;
		for(World world : getServer().getWorlds()) {
			worldConfig = new Config("worlds" + File.separator + world.getName() + ".yml");
			
			for(LogType type : LogType.values()) {
				if(type != LogType.CREEPER && type != LogType.FIREBALL && type != LogType.TNT) {
					worldConfig.getConfig().addDefault(type.name(), true);
				}
			}
			worldConfig.getConfig().options().copyDefaults(true);
			worldConfig.saveConfig();
		}
		
		getConfig().addDefault("mysql.host", "localhost");
	    getConfig().addDefault("mysql.username", "root");
	    getConfig().addDefault("mysql.password", "");
	    getConfig().addDefault("mysql.database", "bukkit");
	    getConfig().addDefault("mysql.port", 3306);
	   	getConfig().addDefault("blocklog.wand", 19);
	   	getConfig().addDefault("blocklog.results", 15);
	    getConfig().addDefault("blocklog.dateformat", "%d-%m %H:%i");
	    getConfig().addDefault("welog", true);
	    getConfig().options().copyDefaults(true);
	    saveConfig();
	}
	
	private void loadDatabase() {
		try {
	    	conn = getDatabaseManager().getConnection();
	    	Statement stmt = conn.createStatement();
	    	
	    	for(String table : DatabaseManager.databaseTables) {
	    		stmt.executeUpdate(Text.getResourceContent("database/" + DatabaseManager.databasePrefix + table + ".sql"));
	    	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void loadPlugin() {
		BlockLog.plugin = this;
		log = getLogger();
		
		log.info("Loading the configurations");
	    loadConfiguration();
		
		log.info("Loading the managers");
		settingsManager = new SettingsManager();
		databaseManager = new DatabaseManager();
		queueManager = new QueueManager();
	    
	    log.info("Loading the database");
	    loadDatabase();
			    
		log.info("Starting BlockLog");
    	
    	getServer().getPluginManager().registerEvents(new WandListener(), this);
    	getServer().getPluginManager().registerEvents(new BlockListener(), this);
    	getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    	getServer().getPluginManager().registerEvents(new EntityListener(), this);
    	getServer().getPluginManager().registerEvents(new WorldListener(), this);
    	
    	if (getPluginManager().getPlugin("WorldEdit") != null) {
    		log.info("Found worldedit!");
			BlockLogEditSessionFactory.initialize(this);
		}
    	
    	if (getPluginManager().getPlugin("TreeAssist") != null)
    	{
    		log.info("Found TreeAssist!");
    		getServer().getPluginManager().registerEvents(new TreeAssistListener(), this);
    	}
    	
    	saveThread = new Save();
    	saveThread.start();
    }
	
	public boolean SetSaving()
	{
		saveThread.asave = !saveThread.asave;
		return saveThread.asave;
	}
	
	public void WaitToSaveQueue()
	{
		saveThread.halt = true;
		try
		{
			saveThread.join();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void stopPlugin() {
		log.info("Waiting to save all block edits...");
		WaitToSaveQueue();
		try {			
			if(conn != null)
				conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		BlockLog.plugin = null;
	}
	
	@Override
	public void onEnable() {
		loadPlugin();
		PluginDescriptionFile PluginDesc = getDescription();
		log.info("v" + PluginDesc.getVersion() + " is enabled!");
	}
	
	@Override
	public void onDisable() {
		stopPlugin();
		PluginDescriptionFile PluginDesc = getDescription();
		log.info("v" + PluginDesc.getVersion() + " is disabled!");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = null;
		
		if (sender instanceof Player)
			player = (Player) sender;
		
		if(!cmd.getName().equalsIgnoreCase("blocklog"))
			return false;
		
		if(args.length < 1) {
			player.sendMessage(ChatColor.GOLD + "Say " + ChatColor.BLUE + "/bl help " + ChatColor.GOLD + "for a list of available commands");
			player.sendMessage(ChatColor.GOLD + "This server is using BlockLog v" + getDescription().getVersion() + " by Anerach");
			return true;
		}
		
		ArrayList<String> argList = new ArrayList<String>();
		
		if(args.length > 1) {
			for(int i=1;i<args.length;i++) {
				argList.add(args[i]);
			}
		}
		
		String[] newArgs = argList.toArray(new String[]{});
		
		BlockLogCommand command = new BlockLogCommand();
		
		if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h"))
			command = new CommandHelp();
		else if(args[0].equalsIgnoreCase("config") || args[0].equalsIgnoreCase("cfg"))
			command = new CommandConfig();
		else if(args[0].equalsIgnoreCase("queue"))
			command = new CommandQueue();
		else if(args[0].equalsIgnoreCase("wand"))
			command = new CommandWand();
		
		cmd.setUsage(command.getCommandUsage());
		return command.execute(player, cmd, newArgs);
	}
}
