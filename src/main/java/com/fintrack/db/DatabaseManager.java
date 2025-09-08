package com.fintrack.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

	private final Connection connection;
	private final String URL = "jdbc:sqlite::memory:";

	public DatabaseManager() throws SQLException {
		this.connection = DriverManager.getConnection(URL);
	}

	public Connection getConnection() {
		return this.connection;
	}

	public void shutdown() {
		this.connection.close();
	}
}