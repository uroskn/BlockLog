package me.arno.blocklog.managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import me.arno.blocklog.BlockLog;

public class DatabaseManager extends BlockLogManager {
	public static final String databasePrefix = "blocklog_";
	public static final String[] databaseTables = {"blocks", "kills", "deaths"};
	
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
