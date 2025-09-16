package com.fintrack.repository;

import java.sql.*;
import java.util.*;
import java.time.LocalDateTime;
import com.fintrack.entity.Balance;
import com.fintrack.entity.MonthlySummary;

public class BalanceDao {
    private final Connection connection;

    public BalanceDao(Connection connection) {
        this.connection = connection;
    }

    public Balance getUserBalance(int userId) {
        String sql = """
            SELECT
                IFNULL((SELECT SUM(amount) FROM income WHERE user_id = ?), 0) AS total_income,
                IFNULL((SELECT SUM(amount) FROM expenses WHERE user_id = ?), 0) AS total_expenses;
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double totalIncome = rs.getDouble("total_income");
                double totalExpenses = rs.getDouble("total_expenses");
                return new Balance(totalIncome - totalExpenses);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Balance();
    }

    public MonthlySummary getMonthlySummary(int userId) {
        String sql = """
            SELECT
                IFNULL((SELECT SUM(amount) FROM income WHERE user_id = ?), 0) AS total_income,
                IFNULL((SELECT SUM(amount) FROM expenses WHERE user_id = ?), 0) AS total_expenses;
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double totalIncome = rs.getDouble("total_income");
                double totalExpenses = rs.getDouble("total_expenses");

                return new MonthlySummary(totalIncome, totalExpenses, totalIncome - totalExpenses);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new MonthlySummary();
    }
}

