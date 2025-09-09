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
}