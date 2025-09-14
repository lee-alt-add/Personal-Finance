package com.fintrack.web.service;

import io.javalin.http.Context;
import java.util.*;
import java.sql.Connection;

import com.fintrack.entity.*;
import com.fintrack.repository.*;

public class IncomeService {
	private IncomeDao incomeDao;

	public IncomeService(Connection connection) {
		incomeDao = new IncomeDao(connection);
	}

	public void addUserIncome(Context context) {
		try {
			int id = Integer.parseInt(context.pathParam("id"));
			Income income = context.bodyAsClass(Income.class);
			income.setUserId(id);
			Income savedIncome = incomeDao.addIncome(income);
			context.status(200).json(savedIncome);
		} catch (Exception e) {
			context.status(404);
			context.result(e.getMessage());
		}
	}

	public void removeIncomeById(Context context) {
		try {
			int id = Integer.parseInt(context.pathParam("id"));
			Income income = incomeDao.removeIncomeById(id);
			context.status(200).json(income);
		} catch (Exception e) {
			context.status(404);
		}
	}
}