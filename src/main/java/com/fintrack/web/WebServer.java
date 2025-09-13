package com.fintrack.web;

import io.javalin.Javalin;
import java.util.Map;
import java.sql.Connection;

import com.fintrack.repository.*;
import com.fintrack.db.DatabaseManager;
import com.fintrack.web.service.*;

public class WebServer {
	private Javalin javalin;
	private DatabaseManager manager = new DatabaseManager();


	public WebServer() {

		this.setServiceConnections();

		this.javalin = Javalin.create(config -> {
			config.defaultContentType = "application/json";
		});


		this.javalin.get("/health", ctx -> APIHandler.getHealth(ctx));

        this.javalin.post("/users", ctx -> UserService.saveUser(ctx));

        this.javalin.delete("/users/{id}", ctx -> UserService.removeUser(ctx));

        this.javalin.get("/all", ctx -> UserService.findAllUsers(ctx));

        this.javalin.post("/users/{id}/expenses", ctx -> ExpenseService.addUserExpense(ctx));
	}

	public void setDatabaseManager(String databaseUrl) {
		manager = new DatabaseManager(databaseUrl);
		this.setServiceConnections();
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

	private void setServiceConnections() {
		UserService.setDatabaseConnection(manager.getConnection());
		ExpenseService.setDatabaseConnection(manager.getConnection());
	}

	public static void main(String[] args) {
		WebServer server = new WebServer();
		server.setDatabaseManager("jdbc:sqlite:Database.db");
		server.start(8080);
	}
}