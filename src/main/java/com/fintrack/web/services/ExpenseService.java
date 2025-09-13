package com.fintrack.web.service;

import io.javalin.http.Context;
import java.util.*;
import java.sql.Connection;

import com.fintrack.entity.*;
import com.fintrack.repository.*;
import com.fintrack.db.DatabaseManager;

public class ExpenseService {
	private static ExpenseDao expenseDao = new ExpenseDao(new DatabaseManager().getConnection());

	public static void setDatabaseConnection(Connection connection) {
		expenseDao = new ExpenseDao(connection);
	}

	public static void addUserExpense(Context context) {
		try {
			int id = Integer.parseInt(context.pathParam("id"));
			Expense expense = context.bodyAsClass(Expense.class);
			expense.setUserId(id);
			Expense savedExpense = expenseDao.addExpense(expense);
			context.status(200).json(savedExpense);
		} catch (Exception e) {
			context.result(e.getMessage());
		}
	}
}