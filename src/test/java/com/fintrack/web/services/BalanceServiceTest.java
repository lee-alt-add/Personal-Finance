package com.fintrack.web.services;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fintrack.repository.Tables;
import com.fintrack.web.WebServer;
import com.fintrack.entity.*;

public class BalanceServiceTest {
    private static WebServer server;
    private static Tables tables;

    /**
     * Start the app server before all tests run
     */
    @BeforeAll
    public static void startServer() {
        server = new WebServer();
        server.setDatabaseManager("memory");
        server.start(0);
        tables = new Tables(server.getDBConnection());

    }

    /**
     * Stop the app server once all tests are run
     */
    @AfterAll
    public static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void makeTable() {
        tables.createExpenses();
        tables.createIncome();
        tables.createUsers();
    }

    @AfterEach
    public void discardTables() {
        tables.deleteTable("expenses");
        tables.deleteTable("income");
        tables.deleteTable("users");
    }

    @Test
    public void getUserBalanceTest() {
        User user = new User(1, "John", "john@doe.com");
        Income income = new Income(1, 10000.00, "salary");
        Expense expense = new Expense(1, 1000.00, "Food", "Lunch");

        // Add to tables
        tables.insertInto(income);
        tables.insertInto(expense);
        tables.insertInto(user);

        TestUtilities.testGetUserBalance(server.getPort(), 1, 9000.00);
    }

    @Test
    public void getUserMonthlySummaryTest() {
        User user = new User(1, "John", "john@doe.com");
        Income income = new Income(1, 500.00, "salary");
        Expense expense = new Expense(1, 300.00, "Food", "Lunch");
        MonthlySummary monthlySummary = new MonthlySummary(500.00, 300.00, 200.00);

        // Add to tables
        tables.insertInto(user);
        tables.insertInto(income);
        tables.insertInto(expense);

        TestUtilities.testGetUserMonthlySummary(server.getPort(), 1, monthlySummary);
    }
}
