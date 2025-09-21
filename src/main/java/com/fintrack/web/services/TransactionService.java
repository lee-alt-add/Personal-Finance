package com.fintrack.web.services;

import com.fintrack.entity.IncomeAndExpensesPerMonth;
import com.fintrack.entity.Transactions;
import com.fintrack.repository.TransactionsDao;
import io.javalin.http.Context;

import java.sql.Connection;
import java.util.List;

public class TransactionService {

    TransactionsDao transactionsDao;

    public TransactionService(Connection connection) {
        this.transactionsDao = new TransactionsDao(connection);
    }

    public void getUserTransactions(Context context) {
        try {
            int userId = Integer.parseInt(context.pathParam("id"));
            List<Transactions> allTransactions = transactionsDao.getUserTransactionsById(userId);
            context.status(200).json(allTransactions);
        } catch (Exception e) {
            context.status(404);
        }
    }

    public void getUserTrends(Context context) {
        try {
            int userId = Integer.parseInt(context.pathParam("id"));
            IncomeAndExpensesPerMonth trends = transactionsDao.getUserTrends(userId);
            context.status(200).json(trends);
        } catch (Exception e) {
            context.status(404);
        }
    }
}
