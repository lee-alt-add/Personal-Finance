package com.fintrack.repository;


import java.sql.*;
import java.util.*;
import java.time.LocalDateTime;

import com.fintrack.dao.Income;

public class IncomeDao {
	private Connection connection;

	public IncomeDao(Connection connection) {
		this.connection = connection;
	}


	public Income addIncome(Income income) throws SQLException {
	    String sql = "INSERT INTO income (user_id, amount, source) VALUES (?, ?, ?);";
	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	        stmt.setInt(1, income.getUserId());
	        stmt.setDouble(2, income.getAmount());
	        stmt.setString(3, income.getSource());
	        stmt.executeUpdate();
	        Income incomeInDb = findAllIncome().getLast();

	        return incomeInDb;
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
					results.getTimestamp("date").toLocalDateTime()
				));
			}
			return allIncome;

		} catch (SQLException e) {
			e.printStackTrace();
			return allIncome;
		}
	}

	public Income removeIncomeById(int id) {
		String sql = "DELETE FROM income WHERE income.id = ?;";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, id);
			Income income = findAllIncome().stream().filter(e -> e.getId() == id).toList().getFirst();
			stmt.executeUpdate();
			return income;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}