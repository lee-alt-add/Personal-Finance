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

public class WebServerTests {
	private static WebServer server;
    private String userInfo = "{\"name\":\"John\", \"email\":\"john@doe.com\"}";

	/**
     * Start the app server before all tests run
     */
    @BeforeAll
    public static void startServer() {
        server = new WebServer();
        server.start(8080);
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

        assertEquals(201, response.getStatus());
        JSONObject jsonObject = response.getBody().getObject();
        assertEquals("John", jsonObject.get("name"));
        assertEquals("john@doe.com", jsonObject.get("email"));
    }

    @Test
    public void findAllUsersEmptyTest() {
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:8080/all").asJson();
        assertEquals(201, response.getStatus());
        JSONArray jsonArray = response.getBody().getArray();
        assertEquals(0, jsonArray.length());
    }

    @Test
    public void findAllUsersTest() {
        // Adding user
        addUser(userInfo);

        // Requesting all user added
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:8080/all").asJson();
        assertEquals(201, response.getStatus());
        JSONArray jsonArray = response.getBody().getArray();
        assertEquals(1, jsonArray.length());
        assertTrue(jsonArray.get(0).toString().contains("John"));
    }

    private HttpResponse<JsonNode> addUser(String userInfo) {
        String url = "http://localhost:8080/user";
        return Unirest.post(url).body(userInfo).asJson();
    }
}