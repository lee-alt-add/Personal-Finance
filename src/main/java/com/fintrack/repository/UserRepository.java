package com.fintrack.repository;


import java.sql.*;
import java.util.*;
import com.fintrack.dao.User;


public class UserRepository {

	private final Connection connection;

	public UserRepository(Connection connection) {
		this.connection = connection;
	}

	public boolean save(User user) {
		String sql = "INSERT INTO users (name, email) VALUES (?, ?);";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, user.getName());
			stmt.setString(2, user.getEmail());
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<User> findAll() {
		List<User> allUsers = new ArrayList<>();
		String sql = "SELECT * FROM users;";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			ResultSet results = stmt.executeQuery();

			while (results.next()) {
				allUsers.add(new User (
					results.getInt("id"),
					results.getString("name"),
					results.getString("email")
				));
			}
			return allUsers;

		} catch (SQLException e) {
			e.printStackTrace();
			return allUsers;
		}
		
	}
}