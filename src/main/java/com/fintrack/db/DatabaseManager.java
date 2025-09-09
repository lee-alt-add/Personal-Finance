package com.fintrack.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
	private final String URL = "jdbc:sqlite::memory:";
    private Connection connection;

	/**
     * Connect to an in-memory SQLite database
     *
     * @throws SQLException if the connection failed
     */
    public DatabaseManager() {
        try {
            this.connection = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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