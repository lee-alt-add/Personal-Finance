package com.fintrack.repository;

import java.sql.*;
import java.util.*;

public class Tables {
	private Connection connection;

	public Tables(Connection connection) {
		this.connection = connection;
	}

	public boolean createTable(String sql) {
		if (sql.toLowerCase().trim().contains("create table")) {
            try (final PreparedStatement s = connection.prepareStatement(sql)) {
                int result = s.executeUpdate();
                return result == 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
	}

	public boolean createUsers() {
		String sql = """
			CREATE TABLE users (
				id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
				name VARCHAR(100) NOT NULL,
				email VARCHAR(100) NOT NULL
			);
		""";

		return createTable(sql);
	}

	public boolean createExpenses() {
		String sql = """
			CREATE TABLE expenses (
		    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
		    user_id INT,
		    amount DECIMAL(10,2),
		    category VARCHAR(50),
		    description VARCHAR(255),
		    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		    FOREIGN KEY (user_id) REFERENCES users(id)
		);
		""";

		return createTable(sql);
	}

	public boolean createIncome() {
		String sql = """
			CREATE TABLE income (
		    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
		    user_id INTEGER,
		    amount DECIMAL(10,2),
		    source VARCHAR(100),
		    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		    FOREIGN KEY (user_id) REFERENCES users(id)
		);
		""";

		return createTable(sql);
	}
}