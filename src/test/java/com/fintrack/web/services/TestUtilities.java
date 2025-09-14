package com.fintrack.web.services;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;
import com.fintrack.entity.*;

public class TestUtilities {

    /*
    * ---------
    * Requests
    * ---------
    */
	public static HttpResponse<JsonNode> addUserRequest(int port, String name, String email) {
        String userInfo = "{\"name\":\"%s\", \"email\":\"%s\"}".formatted(name, email);
        String url = "http://localhost:%d/users".formatted(port);
        return Unirest.post(url).body(userInfo).asJson();
    }

    public static HttpResponse<JsonNode> getUserRequest(int port, int id) {
        String url = "http://localhost:%d/users/%d".formatted(port, id);
        return Unirest.get(url).asJson();
    }

    public static HttpResponse<JsonNode> findAllUsersRequest(int port) {
        String url = "http://localhost:%d/all".formatted(port);
        return Unirest.get(url).asJson();
    }

    public static HttpResponse<JsonNode> removeUserRequest(int port, int id) {
        String url = "http://localhost:%d/users/%d".formatted(port, id);
        return Unirest.delete(url).asJson();
    }

    public static HttpResponse<JsonNode> addExpenseRequest(
    	int port, 
    	int userId, 
    	double amount, 
    	String category, 
    	String description
    ) {
    	String expenseInfo = "{\"amount\":%.2f%n,\"category\":\"%s\",\"description\":\"%s\"}"
    						 .formatted(amount, category, description);

        String url = "http://localhost:%d/users/%d/expenses".formatted(port, userId);
        return Unirest.post(url).body(expenseInfo).asJson();
    }

    public static HttpResponse<JsonNode> removeExpenseRequest(int port, int expenseId) {
        String url = "http://localhost:%d/users/%d/expenses".formatted(port, expenseId);
        return Unirest.delete(url).asJson();
    }

    public static HttpResponse<JsonNode> getUserExpensesRequest(int port, int userId) {
        String url = "http://localhost:%d/users/%d/expenses".formatted(port, userId);
        return Unirest.get(url).asJson();
    }


    /*
    * ---------
    * Testers
    * ---------
    */

    public static void testGetUserExpenses(int port, int userId, List<Expense> itemsAdded) {
        HttpResponse<JsonNode> response = getUserExpensesRequest(port, userId);
        assertEquals(200, response.getStatus());

        JSONArray jsonArray = response.getBody().getArray();
        assertEquals(itemsAdded.size(), jsonArray.length());
    }

    public static HttpResponse<JsonNode> testAddUser(int port, String name, String email) {
        HttpResponse<JsonNode> response = addUserRequest(port, name, email);
        assertEquals(200, response.getStatus());

        JSONObject jsonObject = response.getBody().getObject();

        assertEquals(name, jsonObject.get("name"));
        assertEquals(email, jsonObject.get("email"));
        return response;
    }

    public static void testGetUser(int port, int userId, String name, String email) {
        HttpResponse<JsonNode> response = getUserRequest(port, userId);
        assertEquals(200, response.getStatus());

        JSONObject userData = response.getBody().getObject();

        assertEquals(name, userData.get("name"));
        assertEquals(email, userData.get("email"));
        assertEquals(userId, userData.getInt("id"));
    }

    public static void testRemoveUser(int port, int id) {
        // Get id (as per the database) of user that was saved
        HttpResponse<JsonNode> removalResponse = removeUserRequest(port, id);
        assertEquals(200, removalResponse.getStatus());
    }

    public static HttpResponse<JsonNode> testAddExpense(
    	int port, 
    	int userId, 
    	double amount, 
    	String category, 
    	String description
    ) {
        HttpResponse<JsonNode> response = addExpenseRequest(port, userId, amount, category, description);
        assertEquals(200, response.getStatus());

        JSONObject jsonObject = response.getBody().getObject();
        assertEquals(category, jsonObject.get("category"));
        assertEquals(description, jsonObject.get("description"));

        return response;
    }

    public static int getDatabaseId(HttpResponse<JsonNode> response) {
        JSONObject jsonObject = response.getBody().getObject();

        if (jsonObject == null) {
            jsonObject =  response.getBody().getArray().getJSONObject(0);
        }
        
        return jsonObject.getInt("id");
    }

    public static void testRemoveExpense(int port, int expenseId) {
        HttpResponse<JsonNode> removalResponse = removeExpenseRequest(port, expenseId);
        assertEquals(200, removalResponse.getStatus());
    }
}