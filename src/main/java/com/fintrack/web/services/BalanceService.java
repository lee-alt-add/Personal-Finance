package com.fintrack.web.services;

import com.fintrack.entity.Balance;
import com.fintrack.entity.MonthlySummary;
import com.fintrack.repository.BalanceDao;
import io.javalin.http.Context;

import java.sql.Connection;

public class BalanceService {
    private BalanceDao balanceDao;

    public BalanceService(Connection connection) {
        this.balanceDao = new BalanceDao(connection);
    }

    public void getUserBalance(Context context) {
        try {
            int userId = Integer.parseInt(context.pathParam("id"));
            Balance balance = balanceDao.getUserBalance(userId);
            context.status(200).json(balance);
        } catch (Exception e) {
            context.status(404).json(new Balance());
        }
    }

    public void getMonthlySummary(Context context) {
        try {
            int userId = Integer.parseInt(context.pathParam("id"));
            MonthlySummary monthlySummary = balanceDao.getMonthlySummary(userId);
            context.status(200).json(monthlySummary);
        } catch (Exception e) {
            context.status(404).json(new Balance());
        }
    }
}
