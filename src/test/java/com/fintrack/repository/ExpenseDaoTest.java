package com.fintrack.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.*;
import java.util.*;
import java.time.LocalDateTime;

import com.fintrack.db.DatabaseManager;
import com.fintrack.entity.Expense;

public class ExpenseDaoTest {
	private static DatabaseManager manager;

    /**
     * Connect to the in-memory SQLite database before each test runs
     * @throws SQLException failed to connect
     */
    @BeforeEach
    public void setup() throws SQLException {
        manager = new DatabaseManager();
    }

    /**
     * Close the connection to the database after each tests runs
     * @throws SQLException failed to close the connection
     */
    @AfterEach
    public void cleanup() throws SQLException {
        manager.shutdown();
    }

    @Test
    public void saveExpenseTest() throws SQLException {
        Tables tables = new Tables(manager.getConnection());
        ExpenseDao expenseDao = new ExpenseDao(manager.getConnection());
        Expense expense = new Expense(1,1, 200.00, "food", "none", Timestamp.valueOf(LocalDateTime.now()));

        assertTrue(tables.createExpenses());
        Expense expenseAdded = expenseDao.addExpense(expense);
        assertNotNull(expenseAdded);
        assertEquals(1,expenseAdded.getId());
        assertNotNull(expenseDao.removeExpenseById(1));
    }

    @Test
    public void findAllExpensesTest() throws SQLException {
        Tables tables = new Tables(manager.getConnection());
        ExpenseDao expenseDao = new ExpenseDao(manager.getConnection());
        Expense expense = new Expense(1,1, 200.00, "food", "none", Timestamp.valueOf(LocalDateTime.now()));
        Expense expensetwo = new Expense(2,1, 350.00, "gym", "none", Timestamp.valueOf(LocalDateTime.now()));

        assertTrue(tables.createExpenses());
        assertNotNull(expenseDao.addExpense(expense));
        assertNotNull(expenseDao.addExpense(expensetwo));

        List<Expense> expensesFound = expenseDao.findAllExpenses();
        assertEquals(2, expensesFound.size());
        assertEquals("food", expensesFound.get(0).getCategory());
        assertEquals("gym", expensesFound.get(expensesFound.size()-1).getCategory());
        assertNotNull(expenseDao.removeExpenseById(1));
        assertNotNull(expenseDao.removeExpenseById(2));
    }
}