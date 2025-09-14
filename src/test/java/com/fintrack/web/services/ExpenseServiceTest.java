package com.fintrack.web.services;

import kong.unirest.HttpResponse;
import kong.unirest.HttpStatus;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fintrack.repository.Tables;
import com.fintrack.web.WebServer;

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
        tables.createUsers();
        tables.createExpenses();
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
    	HttpResponse<JsonNode> userResponse = TestUtilities.addUserRequest(server.getPort(), "John", "john@doe.com");

        int userId = TestUtilities.getDatabaseId(userResponse);

        HttpResponse<JsonNode> expenseResponse = TestUtilities.testAddExpense(server.getPort(), userId, 70.00, "Food", "Lunch");

        // Remove user
        TestUtilities.testRemoveUser(server.getPort(), userId);

        // Remove expense
        int expenseId = TestUtilities.getDatabaseId(expenseResponse);
        TestUtilities.testRemoveExpense(server.getPort(), expenseId);
    }
}