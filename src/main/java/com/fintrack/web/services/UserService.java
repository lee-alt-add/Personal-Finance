package com.fintrack.web.service;

import io.javalin.http.Context;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.sql.Connection;

import com.fintrack.entity.*;
import com.fintrack.repository.*;
import com.fintrack.db.DatabaseManager;


public class UserService {
	private UserDao userDao;
	private IncomeDao incomeDao;
	private ExpenseDao expenseDao;

	public UserService(Connection connection) {
		userDao = new UserDao(connection);
		incomeDao = new IncomeDao(connection);
		expenseDao = new ExpenseDao(connection);
	}

	public void saveUser(Context context) {
		try {
			User user = context.bodyAsClass(User.class);
			User userInDb = userDao.save(user);
			context.status(200).json(userInDb);
		} catch (Exception e) {
			context.status(404);
		}
	}

	public void getUser(Context context) {
		try {
			int id = Integer.parseInt(context.pathParam("id"));
			User user = userDao.getUserById(id);
			context.status(200).json(user);
		} catch (Exception e) {
			context.status(404);
		}
	}

	public void findAllUsers(Context context) {
		context.status(200).json(userDao.findAll());
	}

	public void removeUser(Context context) {
		try {
			int id = Integer.parseInt(context.pathParam("id"));
			User user = userDao.removeUserById(id);
			context.status(200).json(user);
		} catch (Exception e) {
			context.status(404);
			context.result(e.getMessage());
		}
	}

	public void getUserTransactions(Context context) {
		try {
			int id = Integer.parseInt(context.pathParam("id"));
			List<Income> income = incomeDao.findIncomeByUserId(id);
			List<Expense> expenses = expenseDao.findAllExpensesByUserId(id);
			List<Object> transactions = Stream.concat(income.stream(), expenses.stream())
												.sorted((a, b) -> a.getDate().compareTo(b.getDate()))
												.collect(Collectors.toList());
			context.status(200).json(transactions);
		} catch (Exception e) {
			context.status(404).result(e.getMessage());
		}
	}
}