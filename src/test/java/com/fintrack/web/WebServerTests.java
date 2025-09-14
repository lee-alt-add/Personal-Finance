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
    public void healthTest() {
    	HttpResponse<JsonNode> response = Unirest.get("http://localhost:%d/health".formatted(server.getPort())).asJson();
    	assertEquals(HttpStatus.OK, response.getStatus());
        JSONObject jsonObject = response.getBody().getObject();
        assertEquals("UP", jsonObject.get("status"));
    }
}