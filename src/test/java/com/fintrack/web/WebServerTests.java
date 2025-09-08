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
}