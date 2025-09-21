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
		String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?);";
		try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, user.getName());
			stmt.setString(2, user.getEmail());
			stmt.setString(3, user.getPassword());
			stmt.executeUpdate();

			try (ResultSet rs = stmt.getGeneratedKeys()) {
				if (rs.next()) {
					int generatedId = rs.getInt(1);
					return getUserById(generatedId);
				}
			}
			return null; // no key returned
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
		String sql = "DELETE FROM users WHERE id = ?;";
		try {
			User user = getUserById(id); // ✅ get the user before deleting
			if (user == null) {
				return null; // nothing to delete
			}

			try (PreparedStatement stmt = connection.prepareStatement(sql)) {
				stmt.setInt(1, id);
				stmt.executeUpdate();
			}
			return user;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}


	public User getUserById(int id) {
		String sql = "SELECT * FROM users WHERE users.id = ?;";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, id);
			try (ResultSet s = stmt.executeQuery()) {
				if (s.next()) {
					return new User (
						s.getInt("id"),
						s.getString("name"),
						s.getString("email")
					);
				} else {
					return null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}


	public User getUser(User user) {
		String sql = "SELECT * FROM users u WHERE u.email = ? AND u.password = ?;";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, user.getEmail());
			stmt.setString(2, user.getPassword());
			
			try (ResultSet s = stmt.executeQuery()) {
				if (s.next()) {
					return new User (
						s.getInt("id"),
						s.getString("name"),
						s.getString("email")
					);
				} else {
					return null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}