package com.fintrack.repository;


import java.sql.*;
import java.util.*;
import com.fintrack.entity.User;


public class UserDao {

	private final Connection connection;

	public UserDao(Connection connection) {
		this.connection = connection;
	}

	public User save(User user) {
		String sql = "INSERT INTO users (name, email) VALUES (?, ?);";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, user.getName());
			stmt.setString(2, user.getEmail());
			stmt.executeUpdate();
			User userInDb = findAll().getLast();
			return userInDb;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
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

	public User removeUserById(int id) {
		String sql = "DELETE FROM users WHERE users.id = ?;";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, id);
			User user = findAll().stream().filter(e -> e.getId() == id).toList().getFirst();
			stmt.executeUpdate();
			return user;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}