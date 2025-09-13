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
import com.fintrack.dao.Income;

public class IncomeDaoTest {
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
    public void saveIncomeTest() throws SQLException {
        Tables tables = new Tables(manager.getConnection());
        IncomeDao incomeDao = new IncomeDao(manager.getConnection());
        Income income = new Income(1,1, 20000.00, "salary", LocalDateTime.now());

        assertTrue(tables.createIncome());
        Income incomeAdded = incomeDao.addIncome(income);
        assertNotNull(incomeAdded);
        assertEquals(1,incomeAdded.getId());
        assertEquals(LocalDateTime.now().toLocalDate(), incomeAdded.getDate().toLocalDate());
        assertNotNull(incomeDao.removeIncomeById(1));
    }

    @Test
    public void findAllIncomeTest() throws SQLException {
        Tables tables = new Tables(manager.getConnection());
        IncomeDao incomeDao = new IncomeDao(manager.getConnection());
        Income income = new Income(1,1, 20000.00, "salary", LocalDateTime.now());
        Income incomeTwo = new Income(2,1, 5000.00, "gift", LocalDateTime.now());

        assertTrue(tables.createIncome());
        assertNotNull(incomeDao.addIncome(income));
        assertNotNull(incomeDao.addIncome(incomeTwo));

        List<Income> incomeFound = incomeDao.findAllIncome();
        assertEquals(2, incomeFound.size());
        System.out.println(incomeFound.getFirst().getId() + " : " + incomeFound.getLast().getId());
        assertEquals("salary", incomeFound.getFirst().getSource());
        assertEquals("gift", incomeFound.getLast().getSource());
        assertNotNull(incomeDao.removeIncomeById(1));
        assertNotNull(incomeDao.removeIncomeById(2));
    }
}