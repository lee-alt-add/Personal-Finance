package com.fintrack.web.services;

import kong.unirest.HttpResponse;
import kong.unirest.HttpStatus;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
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

public class UserServiceTest {
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
    public void addUserTest() {
        User user = new User(1, "John", "john@doe.com");
        HttpResponse<JsonNode> response = TestUtilities.testAddUser(server.getPort(), user);
    }

    @Test
    public void getUserTest() {
        User user = new User(1, "John", "john@doe.com");
        tables.insertInto(user);
        TestUtilities.testGetUser(server.getPort(), 1, user);
    }

    @Test
    public void findAllUsersEmptyTest() {
        // Requesting all users added
        HttpResponse<JsonNode> response = TestUtilities.findAllUsersRequest(server.getPort());

        assertEquals(200, response.getStatus());
        JSONArray jsonArray = response.getBody().getArray();
        assertEquals(0, jsonArray.length());
    }

    @Test
    public void findAllUsersTest() {
        User user = new User(1, "John", "john@doe.com");
        User user2 = new User(1, "Jane", "jane@doe.com");
        
        // Adding user
        tables.insertInto(user);
        tables.insertInto(user2);

        // Requesting all users added
        HttpResponse<JsonNode> response = TestUtilities.findAllUsersRequest(server.getPort());
        
        assertEquals(200, response.getStatus());
        JSONArray jsonArray = response.getBody().getArray();
        assertEquals(2, jsonArray.length());
        assertTrue(jsonArray.get(0).toString().contains("John"));
    }

    @Test
    public void getUserTransactionsTest() {
        User user = new User(1, "John", "john@doe.com");
        Income income = new Income(1, 10000.00, "salary");
        Expense expense = new Expense(1, 70.00, "Food", "Lunch");

        // Add to tables
        tables.insertInto(income);
        tables.insertInto(expense);
        tables.insertInto(user);

        TestUtilities.testGetUserTransations(server.getPort(), 1);
    }
}