package com.fintrack.repository;


import java.sql.*;
import java.util.*;
import java.time.LocalDateTime;

import com.fintrack.entity.Income;

public class IncomeDao {
	private Connection connection;

	public IncomeDao(Connection connection) {
		this.connection = connection;
	}


	public Income addIncome(Income income) throws SQLException {
		String sql = "INSERT INTO income (user_id, amount, source) VALUES (?, ?, ?);";
		try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, income.getUserId());
			stmt.setDouble(2, income.getAmount());
			stmt.setString(3, income.getSource());
			stmt.executeUpdate();

			try (ResultSet rs = stmt.getGeneratedKeys()) {
				if (rs.next()) {
					int generatedId = rs.getInt(1);
					return findById(generatedId);
				}
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}


	public List<Income> findAllIncome() {
		List<Income> allIncome = new ArrayList<>();
		String sql = "SELECT * FROM income;";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			ResultSet results = stmt.executeQuery();

			while (results.next()) {
				allIncome.add(new Income (
					results.getInt("id"),
					results.getInt("user_id"),
					results.getDouble("amount"),
					results.getString("source"),
					results.getTimestamp("date")
				));
			}
			return allIncome;

		} catch (SQLException e) {
			e.printStackTrace();
			return allIncome;
		}
	}

	public List<Income> findIncomeByUserId(int id) {
		List<Income> allIncome = new ArrayList<>();
		String sql = "SELECT * FROM income WHERE income.user_id = ?;";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, id);
			ResultSet results = stmt.executeQuery();

			while (results.next()) {
				allIncome.add(new Income (
					results.getInt("id"),
					results.getInt("user_id"),
					results.getDouble("amount"),
					results.getString("source"),
					results.getTimestamp("date")
				));
			}
			return allIncome;

		} catch (SQLException e) {
			e.printStackTrace();
			return allIncome;
		}
	}

	public Income removeIncomeById(int id) {
		String sql = "DELETE FROM income WHERE id = ?;";
		try {
			Income income = findById(id);
			if (income == null) {
				return null;
			}

			try (PreparedStatement stmt = connection.prepareStatement(sql)) {
				stmt.setInt(1, id);
				stmt.executeUpdate();
			}
			return income;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}


	public Income findById(int id) {
		String sql = "SELECT * FROM income WHERE id = ?;";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return new Income(
						rs.getInt("id"),
						rs.getInt("user_id"),
						rs.getDouble("amount"),
						rs.getString("source"),
						rs.getTimestamp("date")
				);
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}