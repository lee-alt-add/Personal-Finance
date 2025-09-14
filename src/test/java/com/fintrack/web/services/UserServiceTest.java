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
        tables.createUsers();
    }

    /**
     * Stop the app server once all tests are run
     */
    @AfterAll
    public static void stopServer() {
    	server.stop();
    }

    @Test
    public void addUserTest() {
        HttpResponse<JsonNode> response = testAddUser("John", "john@doe.com");
        testRemoveUser(response);
    }

    @Test
    public void getUserTest() {
        HttpResponse<JsonNode> response = testAddUser("John", "john@doe.com");
        testGetUser(response);
        testRemoveUser(response);
    }

    @Test
    public void findAllUsersEmptyTest() {
        // Requesting all users added
        HttpResponse<JsonNode> response = Requesters.findAllRequest(server.getPort());

        assertEquals(200, response.getStatus());
        JSONArray jsonArray = response.getBody().getArray();
        assertEquals(0, jsonArray.length());
    }

    @Test
    public void findAllUsersTest() {
        // Adding user
        Requesters.addUserRequest(server.getPort(), "John", "john@doe.com");
        Requesters.addUserRequest(server.getPort(), "Jane", "jane@doe.com");

        // Requesting all users added
        HttpResponse<JsonNode> response = Requesters.findAllRequest(server.getPort());
        
        assertEquals(200, response.getStatus());
        JSONArray jsonArray = response.getBody().getArray();
        assertEquals(2, jsonArray.length());
        assertTrue(jsonArray.get(0).toString().contains("John"));

        // Removing users
        for (Object object : jsonArray) {
            JSONObject jsonObject = (JSONObject) object;
            int id = jsonObject.getInt("id");
            HttpResponse<JsonNode> removalResponse = Requesters.removeUserRequest(server.getPort(), id);
            assertEquals(200, removalResponse.getStatus());
        }
    }

    /* 
    * --------
    * Helper
    * --------
    */
    private int getDatabaseId(HttpResponse<JsonNode> response) {
        JSONObject jsonObject = response.getBody().getObject();

        if (jsonObject == null) {
            jsonObject =  response.getBody().getArray().getJSONObject(0);
        }
        
        return jsonObject.getInt("id");
    }

    /* 
    * --------
    * Testers
    * --------
    */
    private HttpResponse<JsonNode> testAddUser(String name, String email) {
        HttpResponse<JsonNode> response = Requesters.addUserRequest(server.getPort(), name, email);
        assertEquals(200, response.getStatus());

        JSONObject jsonObject = response.getBody().getObject();

        assertEquals(name, jsonObject.get("name"));
        assertEquals(email, jsonObject.get("email"));
        return response;
    }

    private void testRemoveUser(HttpResponse<JsonNode> response) {
        // Get id (as per the database) of user that was saved
        int id = getDatabaseId(response);
        HttpResponse<JsonNode> removalResponse = Requesters.removeUserRequest(server.getPort(), id);
        assertEquals(200, removalResponse.getStatus());
    }

    private void testGetUser(HttpResponse<JsonNode> json) {
        int id = getDatabaseId(json);
        HttpResponse<JsonNode> response = Requesters.getUserRequest(server.getPort(), id);
        assertEquals(200, response.getStatus());

        JSONObject original = json.getBody().getObject();
        JSONObject expected = response.getBody().getObject();

        assertEquals(original.get("name"), expected.get("name"));
        assertEquals(original.get("email"), expected.get("email"));
    }
}