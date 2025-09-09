package com.fintrack.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
	private static final String URL = "jdbc:sqlite::memory:";
    private final Connection connection;

	/**
     * Connect to an in-memory SQLite database
     *
     * @throws SQLException if the connection failed
     */
    public DatabaseManager() throws SQLException {
        this.connection = DriverManager.getConnection(URL);
    }

    /**
     * Get the database connection
     * @return the database connection
     */
    public Connection getConnection() {
        return this.connection;
    }

    /**
     * Close the database connection
     *
     * @throws SQLException the connection could not be closed
     */
    public void shutdown() throws SQLException {
        connection.close();
    }
}