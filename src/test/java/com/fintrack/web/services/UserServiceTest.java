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
    private String userInfo = "{\"name\":\"John\", \"email\":\"john@doe.com\"}";

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
    public void userTest() {
        HttpResponse<JsonNode> response = addUser(userInfo);
        assertEquals(200, response.getStatus());

        // Get id (as per the database) of user that was saved
        int id = getDatabaseId(response);
        HttpResponse<JsonNode> removalResponse = removeUser(id);

        assertEquals(200, removalResponse.getStatus());
        JSONObject jsonObject = response.getBody().getObject();
        assertEquals("John", jsonObject.get("name"));
        assertEquals("john@doe.com", jsonObject.get("email"));
    }

    @Test
    public void removeUserTest() {
        HttpResponse<JsonNode> response = addUser(userInfo);
        assertEquals(200, response.getStatus());

        int id = getDatabaseId(response);
        HttpResponse<JsonNode> removalResponse = removeUser(id);

        assertEquals(200, removalResponse.getStatus());
        JSONObject jsonObject = removalResponse.getBody().getObject();
        assertEquals("John", jsonObject.get("name"));
        assertEquals("john@doe.com", jsonObject.get("email"));
    }

    @Test
    public void findAllUsersEmptyTest() {
        String url = "http://localhost:%d/all".formatted(server.getPort());
        HttpResponse<JsonNode> response = Unirest.get(url).asJson();
        assertEquals(200, response.getStatus());
        JSONArray jsonArray = response.getBody().getArray();
        assertEquals(0, jsonArray.length());
    }

    @Test
    public void findAllUsersTest() {
        // Adding user
        addUser(userInfo);

        // Requesting all users added
        String url = "http://localhost:%d/all".formatted(server.getPort());
        HttpResponse<JsonNode> response = Unirest.get(url).asJson();
        assertEquals(200, response.getStatus());
        JSONArray jsonArray = response.getBody().getArray();
        assertEquals(1, jsonArray.length());
        assertTrue(jsonArray.get(0).toString().contains("John"));

        // Removing user
        int id = getDatabaseId(response);
        HttpResponse<JsonNode> removalResponse = removeUser(id);
        assertEquals(200, removalResponse.getStatus());
    }

    private HttpResponse<JsonNode> addUser(String userInfo) {
        int port = server.getPort();
        String url = "http://localhost:%d/users".formatted(port);
        return Unirest.post(url).body(userInfo).asJson();
    }

    private HttpResponse<JsonNode> removeUser(int id) {
        int port = server.getPort();
        String url = "http://localhost:%d/users/%d".formatted(port, id);
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