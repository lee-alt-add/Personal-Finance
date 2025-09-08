package com.fintrack.web;

import io.javalin.Javalin;

import java.util.Map;

public class WebServer {
	private Javalin javalin;


	public WebServer() {
		this.javalin = Javalin.create(config -> {
			config.defaultContentType = "application/json";
		});

		this.javalin.get("/health", ctx -> {
            ctx.json(Map.of("status", "UP"));
        });
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