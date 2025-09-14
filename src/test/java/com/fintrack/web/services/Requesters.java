package com.fintrack.web.services;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

public class Requesters {
	public static HttpResponse<JsonNode> addUserRequest(int port, String name, String email) {
        String userInfo = "{\"name\":\"%s\", \"email\":\"%s\"}".formatted(name, email);
        String url = "http://localhost:%d/users".formatted(port);
        return Unirest.post(url).body(userInfo).asJson();
    }

    public static HttpResponse<JsonNode> getUserRequest(int port, int id) {
        String url = "http://localhost:%d/users/%d".formatted(port, id);
        return Unirest.get(url).asJson();
    }

    public static HttpResponse<JsonNode> findAllRequest(int port) {
        String url = "http://localhost:%d/all".formatted(port);
        return Unirest.get(url).asJson();
    }

    public static HttpResponse<JsonNode> removeUserRequest(int port, int id) {
        String url = "http://localhost:%d/users/%d".formatted(port, id);
        return Unirest.delete(url).asJson();
    }
}