package me.arno.blocklog.managers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import me.arno.blocklog.BlockLog;
import me.arno.util.Query;

public class DatabaseManager extends BlockLogManager {
	public static final String databasePrefix = "blocklog_";
	public static final String[] databaseTables = {"blocks", "rollbacks", "undos", "interactions", "reports", "chat", "deaths", "kills", "commands"};
	public static final String[] purgeableTables = {"blocks", "interactions", "chat", "deaths", "kills", "commands"};
	
	public Connection getConnection() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			return DriverManager.getConnection("jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDatabase(), getUsername(), getPassword());
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (SQLException e) {
			BlockLog.plugin.log.severe("Unable to connect to the MySQL Server");
			BlockLog.plugin.log.severe("Please check your MySQL settings in your config.yml");
		} catch (ClassNotFoundException e) {
			throw new SQLException("Unable to find the MySQL JDBC Driver");
		}
		return null;
	}
	
	public void purge(String[] tables) throws SQLException, IOException {
		purge(tables, 0);
	}
	
	public void purge(String[] tables, long timestamp) throws SQLException, IOException {
		FileWriter fileWriter = new FileWriter("BlockLog Database Cleanup.log");
		BufferedWriter writer = new BufferedWriter(fileWriter);
		
		Query query;
		for(String table : tables) {
			if(getSettingsManager().isPurgeEnabled(table)) {
				if(timestamp == 0)
					timestamp = (System.currentTimeMillis()/1000) - (getSettingsManager().getPurgeDate(table) * 60 * 60 * 24);
				query = new Query(databasePrefix + table);
				query.where("date", timestamp, "<");
				int count = query.deleteRows();
				
				if(getSettingsManager().isPurgeLoggingEnabled())
					writer.write("[BlockLog] Deleted " + count + " results from " + DatabaseManager.databasePrefix + table + System.getProperty("line.separator"));
			}
		}
		writer.close();
	}
	
	public String getHost() {
		return BlockLog.plugin.getConfig().getString("mysql.host");
	}
	
	public String getUsername() {
		return BlockLog.plugin.getConfig().getString("mysql.username");
	}
	
	public String getPassword() {
		return BlockLog.plugin.getConfig().getString("mysql.password");
	}
	
	public String getDatabase() {
		return BlockLog.plugin.getConfig().getString("mysql.database");
	}
	
	public int getPort() {
		return BlockLog.plugin.getConfig().getInt("mysql.port");
	}
}
