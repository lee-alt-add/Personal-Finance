package com.fintrack.web;

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

public class ExpenseServiceTest {
	private static WebServer server;
    private static Tables tables;
    private String userInfo = "{\"name\":\"John\", \"email\":\"john@doe.com\"}";
    private String expenseInfo = "{\"amount\":50,\"category\":\"Food\",\"description\":\"Lunch\"}";

	/**
     * Start the app server before all tests run
     */
    @BeforeAll
    public static void startServer() {
        server = new WebServer();
        server.setDatabaseManager("memory");
        server.start(5000);
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
    	HttpResponse<JsonNode> userResponse = addUser(userInfo);
        assertEquals(200, userResponse.getStatus());
        int userId = getDatabaseId(userResponse);
        HttpResponse<JsonNode> response = addExpense(expenseInfo, userId);
        assertEquals(200, response.getStatus());

        JSONObject jsonObject = response.getBody().getObject();
        assertEquals("Food", jsonObject.get("category"));
        assertEquals("Lunch", jsonObject.get("description"));

        // Remove user
        HttpResponse<JsonNode> removalResponse = removeUser(userId);
        assertEquals(200, removalResponse.getStatus());
    }

    private HttpResponse<JsonNode> addUser(String userInfo) {
        String url = "http://localhost:5000/users";
        return Unirest.post(url).body(userInfo).asJson();
    }

    private HttpResponse<JsonNode> addExpense(String expenseInfo, int userId) {
        String url = "http://localhost:5000/users/%d/expenses".formatted(userId);
        return Unirest.post(url).body(expenseInfo).asJson();
    }

    private HttpResponse<JsonNode> removeUser(int id) {
        String url = "http://localhost:5000/users/" + id;
        return Unirest.delete(url).asJson();
    }

    private int getDatabaseId(HttpResponse<JsonNode> response) {
        JSONObject jsonObject = response.getBody().getObject();

        if (jsonObject == null) {
            jsonObject =  response.getBody().getArray().getJSONObject(0);
        }
        
        return jsonObject.getInt("id");
    }
}