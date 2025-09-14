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
			context.status(404);
			context.result(e.getMessage());
		}
	}

	public void getUserExpenses(Context context) {
		try {
			int id = Integer.parseInt(context.pathParam("id"));
			List<Expense> expenses = expenseDao.findAllExpensesByUserId(id);
			context.status(200).json(expenses);
		} catch (Exception e) {
			context.status(404);
		}
	}

	public void removeExpenseById(Context context) {
		try {
			int id = Integer.parseInt(context.pathParam("id"));
			Expense expense = expenseDao.removeExpenseById(id);
			context.status(200).json(expense);
		} catch (Exception e) {
			context.status(404);
		}
	}
}