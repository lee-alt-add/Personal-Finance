package com.fintrack.repository;


import java.sql.*;
import java.util.*;
import java.time.LocalDateTime;
import com.fintrack.entity.Expense;


public class ExpenseDao {
	private final Connection connection;

	public ExpenseDao(Connection connection) {
		this.connection = connection;
	}

	public Expense addExpense(Expense expense) throws SQLException {
	    String sql = "INSERT INTO expenses (user_id, amount, category, description) VALUES (?, ?, ?, ?);";
	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	        stmt.setInt(1, expense.getUserId());
	        stmt.setDouble(2, expense.getAmount());
	        stmt.setString(3, expense.getCategory());
	        stmt.setString(4, expense.getDescription());
	        stmt.executeUpdate();
	        Expense expenseInDb = findAllExpenses().getLast();

	        return expenseInDb;
	    } catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Expense> findAllExpenses() {
		List<Expense> allExpenses = new ArrayList<>();
		String sql = "SELECT * FROM expenses;";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			ResultSet results = stmt.executeQuery();

			while (results.next()) {
				allExpenses.add(new Expense (
					results.getInt("id"),
					results.getInt("user_id"),
					results.getDouble("amount"),
					results.getString("category"),
					results.getString("description"),
					results.getTimestamp("date")
				));
			}
			return allExpenses;

		} catch (SQLException e) {
			e.printStackTrace();
			return allExpenses;
		}
	}

	public List<Expense> findAllExpensesById(int id) {
		return findAllExpenses().stream().filter(e -> e.getId() == id).toList();
	}

	public Expense removeExpenseById(int id) {
		String sql = "DELETE FROM expenses WHERE expenses.id = ?;";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, id);
			Expense expense = findAllExpenses().stream().filter(e -> e.getId() == id).toList().getFirst();
			stmt.executeUpdate();
			return expense;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Expense findExpenseById(int id) {
	    String sql = "SELECT * FROM expenses WHERE id = ?";
	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	        stmt.setInt(1, id);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            return new Expense(
	                rs.getInt("id"),
	                rs.getInt("user_id"),
	                rs.getDouble("amount"),
	                rs.getString("category"),
	                rs.getString("description"),
	                rs.getTimestamp("date")
	            );
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

}