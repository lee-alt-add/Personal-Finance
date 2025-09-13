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

public class WebServerTests {
	private static WebServer server;
    private static Tables tables;
    private String userInfo = "{\"name\":\"John\", \"email\":\"john@doe.com\"}";

	/**
     * Start the app server before all tests run
     */
    @BeforeAll
    public static void startServer() {
        server = new WebServer();
        server.start(8080);
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
    public void healthTest() {
    	HttpResponse<JsonNode> response = Unirest.get("http://localhost:8080/health").asJson();
    	assertEquals(HttpStatus.OK, response.getStatus());
        JSONObject jsonObject = response.getBody().getObject();
        assertEquals("UP", jsonObject.get("status"));
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
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:8080/all").asJson();
        assertEquals(200, response.getStatus());
        JSONArray jsonArray = response.getBody().getArray();
        assertEquals(0, jsonArray.length());
    }

    @Test
    public void findAllUsersTest() {
        // Adding user
        addUser(userInfo);

        // Requesting all users added
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:8080/all").asJson();
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
        String url = "http://localhost:8080/users";
        return Unirest.post(url).body(userInfo).asJson();
    }

    private HttpResponse<JsonNode> removeUser(int id) {
        String url = "http://localhost:8080/users/" + id;
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