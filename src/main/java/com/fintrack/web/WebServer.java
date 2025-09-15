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
	private UserService userService;
	private ExpenseService expenseService;
	private IncomeService incomeService;


	public WebServer() {

		this.javalin = Javalin.create(config -> {
			config.defaultContentType = "application/json";
		});


		this.javalin.get("/health", ctx -> APIHandler.getHealth(ctx));

		/* ------- */
        // User
        /* ------- */
        this.javalin.post("/users", ctx -> userService.saveUser(ctx));

        this.javalin.get("/users/{id}", ctx -> userService.getUser(ctx));

        this.javalin.delete("/users/{id}", ctx -> userService.removeUser(ctx));

        this.javalin.get("/all", ctx -> userService.findAllUsers(ctx));

        /* ------- */
        // Expenses
        /* ------- */
        this.javalin.post("/users/{id}/expenses", ctx -> expenseService.addUserExpense(ctx));

        this.javalin.delete("users/{id}/expenses", ctx -> expenseService.removeExpenseById(ctx));

        this.javalin.get("users/{id}/expenses", ctx -> expenseService.getUserExpenses(ctx));

        /* ------- */
        // Income
        /* ------- */
        this.javalin.post("users/{id}/income", ctx -> incomeService.addUserIncome(ctx));

        this.javalin.delete("users/{id}/income", ctx -> incomeService.removeIncomeById(ctx));

        this.javalin.get("users/{id}/income", ctx -> incomeService.getUserIncome(ctx));

        /* ------------ */
        // Transactions
        /* ------------ */
        this.javalin.get("/users/{id}/transactions", ctx -> userService.getUserTransactions(ctx));

        this.javalin.get("/users/{id}/balance", ctx -> userService.getUserBalance(ctx));
	}

	public void setDatabaseManager(String databaseUrl) {
		manager = databaseUrl.toLowerCase().trim().equals("memory") ?
				  new DatabaseManager() :
				  new DatabaseManager(databaseUrl);

		expenseService = new ExpenseService(manager.getConnection());
		userService = new UserService(manager.getConnection());
		incomeService = new IncomeService(manager.getConnection());
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