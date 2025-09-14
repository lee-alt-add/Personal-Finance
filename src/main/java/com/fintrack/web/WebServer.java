package com.fintrack.web;

import io.javalin.Javalin;
import java.util.Map;
import java.sql.Connection;

import com.fintrack.repository.*;
import com.fintrack.db.DatabaseManager;
import com.fintrack.web.service.*;

public class WebServer {
	private Javalin javalin;
	private DatabaseManager manager;
	private ExpenseService expenseService;
	private UserService userService;


	public WebServer() {

		this.javalin = Javalin.create(config -> {
			config.defaultContentType = "application/json";
		});


		this.javalin.get("/health", ctx -> APIHandler.getHealth(ctx));

        this.javalin.post("/users", ctx -> userService.saveUser(ctx));

        this.javalin.delete("/users/{id}", ctx -> userService.removeUser(ctx));

        this.javalin.get("/all", ctx -> userService.findAllUsers(ctx));

        this.javalin.post("/users/{id}/expenses", ctx -> expenseService.addUserExpense(ctx));
	}

	public void setDatabaseManager(String databaseUrl) {
		manager = databaseUrl.toLowerCase().trim().equals("memory") ?
				  new DatabaseManager() :
				  new DatabaseManager(databaseUrl);

		expenseService = new ExpenseService(manager.getConnection());
		userService = new UserService(manager.getConnection());
	}

	public Connection getDBConnection() {
		return this.manager.getConnection();
	}

	public int getPort() {
		return this.javalin.port();
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
		server.setDatabaseManager("jdbc:sqlite:Database.db");
		server.start(8080);
	}
}