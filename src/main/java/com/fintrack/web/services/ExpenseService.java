package com.fintrack.web.service;

import io.javalin.http.Context;
import java.util.*;
import java.sql.Connection;

import com.fintrack.entity.*;
import com.fintrack.repository.*;

public class ExpenseService {
	private ExpenseDao expenseDao;

	public ExpenseService(Connection connection) {
		expenseDao = new ExpenseDao(connection);
	}

	public void addUserExpense(Context context) {
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