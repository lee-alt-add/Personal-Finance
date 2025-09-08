package com.fintrack.repository;


import java.sql.*;
import java.util.*;
import com.fintrack.dao.User;
import com.fintrack.db.DatabaseManager;


public class UserRepository {

	public void save(User user) throws SQLException {
		try (final Connection conn = DatabaseManager.getConnection()) {
			String sql = "INSERT INTO users (name, email) VALUES (?, ?);";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, user.getName());
			stmt.setString(2, user.getEmail());
			stmt.executeUpdate();
		}
	}

	public List<User> findAll() throws SQLException {
		List<User> allUsers = new ArrayList<>();

		try (final Connection conn = DatabaseManager.getConnection()) {
			String sql = "SELECT * FROM users;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet results = stmt.executeQuery();

			while (results.next()) {
				allUsers.add(new User (
					results.getInt("id"),
					results.getString("name"),
					results.getString("email")
				));
			}
		}
		return allUsers;
	}
}