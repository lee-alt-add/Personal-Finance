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
        HttpResponse<JsonNode> response = TestUtilities.testAddUser(server.getPort(), "John", "john@doe.com");
        int userId = TestUtilities.getDatabaseId(response);
        TestUtilities.testRemoveUser(server.getPort(), userId);
    }

    @Test
    public void getUserTest() {
        HttpResponse<JsonNode> response = TestUtilities.addUserRequest(server.getPort(), "John", "john@doe.com");
        int userId = TestUtilities.getDatabaseId(response);
        TestUtilities.testGetUser(server.getPort(), userId, "John", "john@doe.com");
        TestUtilities.testRemoveUser(server.getPort(), userId);
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
        // Adding user
        TestUtilities.addUserRequest(server.getPort(), "John", "john@doe.com");
        TestUtilities.addUserRequest(server.getPort(), "Jane", "jane@doe.com");

        // Requesting all users added
        HttpResponse<JsonNode> response = TestUtilities.findAllUsersRequest(server.getPort());
        
        assertEquals(200, response.getStatus());
        JSONArray jsonArray = response.getBody().getArray();
        assertEquals(2, jsonArray.length());
        assertTrue(jsonArray.get(0).toString().contains("John"));

        // Removing users
        for (Object object : jsonArray) {
            JSONObject jsonObject = (JSONObject) object;
            int id = jsonObject.getInt("id");
            HttpResponse<JsonNode> removalResponse = TestUtilities.removeUserRequest(server.getPort(), id);
            assertEquals(200, removalResponse.getStatus());
        }
    }
}