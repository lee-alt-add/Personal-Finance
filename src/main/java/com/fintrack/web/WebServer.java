package com.fintrack.web;

import io.javalin.Javalin;
import java.util.Map;
import java.sql.Connection;

import com.fintrack.repository.*;
import com.fintrack.db.DatabaseManager;

public class WebServer {
	private Javalin javalin;
	private DatabaseManager manager;


	public WebServer() {

		this.javalin = Javalin.create(config -> {
			config.defaultContentType = "application/json";
		});


		this.javalin.get("/health", ctx -> APIHandler.getHealth(ctx));

        this.javalin.post("/user", ctx -> APIHandler.saveUser(ctx));

        this.javalin.delete("/user/{id}", ctx -> APIHandler.removeUser(ctx));

        this.javalin.get("/all", ctx -> APIHandler.findAllUsers(ctx));
	}

	public void setDatabaseManager(String databaseUrl) {
		manager = new DatabaseManager(databaseUrl);
		APIHandler.initializeDB(manager.getConnection());
	}

	public Connection getDBConnection() {
		return this.manager.getConnection();
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