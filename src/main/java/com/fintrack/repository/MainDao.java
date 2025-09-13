package com.fintrack.repository;


import java.sql.Connection;

public class MainDao {
	public UserDao userDao;
	public ExpenseDao expenseDao;
	public IncomeDao incomeDao;

	public MainDao(Connection connection) {
		this.userDao = new UserDao(connection);
		this.expenseDao = new ExpenseDao(connection);
		this.incomeDao = new IncomeDao(connection);
	}

}