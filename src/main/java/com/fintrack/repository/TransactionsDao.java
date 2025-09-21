package com.fintrack.repository;

import com.fintrack.entity.Expense;
import com.fintrack.entity.Income;
import com.fintrack.entity.IncomeAndExpensesPerMonth;
import com.fintrack.entity.Transactions;

import java.sql.*;
import java.util.*;

public class TransactionsDao {

    private final Connection connection;

    public TransactionsDao(Connection connection) {
        this.connection = connection;
    }

    public List<Transactions> getUserTransactionsById(int userId) {
        List<Transactions> allTransactions = new ArrayList<>();
        String sql = """
        SELECT id, user_id, amount, source AS source, NULL AS category, NULL AS description, date, 'income' AS type
        FROM income WHERE user_id = ?
        UNION ALL
        SELECT id, user_id, amount, NULL AS source, category, description, date, 'expense' AS type
        FROM expenses WHERE user_id = ?
        ORDER BY date DESC;
    """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                double amount = rs.getDouble("amount");
                String source = rs.getString("source");
                String category = rs.getString("category");
                String description = rs.getString("description");
                Timestamp date = rs.getTimestamp("date");
                String type = rs.getString("type");

                Transactions transations = new Transactions(
                        id,
                        userId,
                        amount,
                        category,
                        description,
                        source,
                        date,
                        type
                );
                allTransactions.add(transations);
            }
            return allTransactions;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public IncomeAndExpensesPerMonth getUserTrends(int userId) {
        String sql = """
            SELECT
                strftime('%Y-%m', date) AS month,
                SUM(CASE WHEN type = 'income' THEN amount ELSE 0 END) AS total_income,
                SUM(CASE WHEN type = 'expense' THEN amount ELSE 0 END) AS total_expense
            FROM (
                SELECT amount, date, 'income' AS type FROM income WHERE user_id = ?
                UNION ALL
                SELECT amount, date, 'expense' AS type FROM expenses WHERE user_id = ?
            ) AS combined
            GROUP BY month
            ORDER BY month ASC;
        """;
        
        List<String> labels = new ArrayList<>();
        List<Double> income = new ArrayList<>();
        List<Double> expenses = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                labels.add(rs.getString("month"));
                income.add(rs.getDouble("total_income"));
                expenses.add(rs.getDouble("total_expense"));
            }

            return new IncomeAndExpensesPerMonth(labels, income, expenses);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new IncomeAndExpensesPerMonth();
    }
}
