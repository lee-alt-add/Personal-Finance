package com.fintrack.web;

import io.javalin.Javalin;

import java.util.Map;

public class WebServer {
	private Javalin javalin;


	public WebServer() {
		this.javalin = Javalin.create(config -> {
			config.defaultContentType = "application/json";
		});

		APIHandler.initializeDB();

		this.javalin.get("/health", ctx -> APIHandler.getHealth(ctx));

        this.javalin.post("/user", ctx -> APIHandler.saveUser(ctx));

        this.javalin.delete("/user/{id}", ctx -> APIHandler.removeUser(ctx));

        this.javalin.get("/all", ctx -> APIHandler.findAllUsers(ctx));
	}

	public void start(int port) {
		this.javalin.start(port);
	}

	public void stop() {
		this.javalin.close();
		this.javalin.stop();
	}

	public static void main(String[] args) {
		WebServer server = new WebServer();
		server.start(8080);
	}
}