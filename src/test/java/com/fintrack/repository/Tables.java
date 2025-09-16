package com.fintrack.repository;

import java.sql.*;
import java.util.*;

import com.fintrack.entity.*;

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

	public boolean insertInto(Object obj) {
        if (obj instanceof Expense expense) {
            String sql = """
                    INSERT INTO expenses (user_id, amount, category, description)
                        VALUES ('%d', '%.2f%n', '%s', '%s');
                    """.formatted(
                    	expense.getUserId(), 
                    	expense.getAmount(), 
                    	expense.getCategory(), 
                    	expense.getDescription()
                    );
            return executeSql(sql);
        } 
        else if (obj instanceof Income income) {
            String sql = """
                    INSERT INTO income (user_id, amount, source)
                        VALUES ('%d', '%.2f%n', '%s');
                    """.formatted(
                    	income.getUserId(), 
                    	income.getAmount(), 
                    	income.getSource()
                    );

            return executeSql(sql);
        }
        else if (obj instanceof User user) {
        	String sql = """
		        	INSERT INTO users (name, email) 
		        	VALUES ('%s', '%s');
		        	""".formatted(
		        		user.getName(), 
		        		user.getEmail()
		        	);
		    return executeSql(sql);
        }

        return false;
    }

//    public ResultSet getMonthly() {
//    	String sql = """
//    		SELECT
//			  (SELECT IFNULL(
//			  	SUM(amount), 0) FROM income WHERE user_id = ?
//			  	AND MONTH(date) = MONTH(CURRENT_DATE) AND
//			  	YEAR(date) = YEAR(CURRENT_DATE)) AS total_income,
//
//			  (SELECT IFNULL(SUM(amount), 0) FROM expenses WHERE user_id = ?
//			   AND MONTH(date) = MONTH(CURRENT_DATE) AND
//			   YEAR(date) = YEAR(CURRENT_DATE)) AS total_expenses;
//    	""";
//
//		try (ResultSet s = executeFinder(sql)) {
//			while (s.next()) {
//
//			}
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		}
//    }

    private ResultSet executeFinder(String sql) {
    	try (PreparedStatement s = connection.prepareStatement(sql)) {
    		return s.executeQuery();
    	} catch (SQLException e) {
    		e.printStackTrace();
            return null;
    	}
        
    }

    public boolean deleteTable(String tableName) {
		String sql = "DROP TABLE %s;".formatted(tableName);
		return executeSql(sql);
    }

    private boolean executeSql(String sql) {
        try (final PreparedStatement s = connection.prepareStatement(sql)) {
            int result = s.executeUpdate();
            return result == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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

	public boolean createExpenses() {
		String sql = """
			CREATE TABLE expenses (
		    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
		    user_id INT,
		    amount DECIMAL(10,2),
		    category VARCHAR(50),
		    description VARCHAR(255),
		    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		    FOREIGN KEY (user_id) REFERENCES users(id)
		);
		""";

		return createTable(sql);
	}

	public boolean createIncome() {
		String sql = """
			CREATE TABLE income (
		    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
		    user_id INTEGER,
		    amount DECIMAL(10,2),
		    source VARCHAR(100),
		    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		    FOREIGN KEY (user_id) REFERENCES users(id)
		);
		""";

		return createTable(sql);
	}
}