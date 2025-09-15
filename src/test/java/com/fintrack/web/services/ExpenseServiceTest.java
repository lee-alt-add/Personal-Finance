package com.fintrack.web.services;

import kong.unirest.HttpResponse;
import kong.unirest.HttpStatus;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fintrack.repository.Tables;
import com.fintrack.web.WebServer;
import com.fintrack.entity.*;

public class ExpenseServiceTest {
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

    @BeforeEach
    public void makeTable() {
        tables.createExpenses();
    }

    @AfterEach
    public void discardTables() {
        tables.deleteTable("expenses");
    }

    /**
     * Stop the app server once all tests are run
     */
    @AfterAll
    public static void stopServer() {
    	server.stop();
    }

    @Test
    public void addExpenseTest() {
        Expense expense = new Expense(1, 70.00, "Food", "Lunch");
        TestUtilities.testAddExpense(server.getPort(), 1, expense);
    }

    @Test
    public void removeExpenseByIdTest() {
        Expense expense = new Expense(1, 375.00, "Transport", "To work");
        tables.insertInto(expense);
        TestUtilities.testRemoveExpense(server.getPort(), 1);
    }

    @Test
    public void getUserExpensesTest() {
        Expense expense = new Expense(1, 375.00, "Transport", "To work");
        Expense expense2 = new Expense(1, 375.00, "Transport", "To work");
        tables.insertInto(expense);
        tables.insertInto(expense2);

        TestUtilities.testGetUserExpenses(server.getPort(), 1, List.of(expense, expense2));
    }
}