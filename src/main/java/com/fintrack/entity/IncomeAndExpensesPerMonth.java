package com.fintrack.entity;

import java.util.*;

public class IncomeAndExpensesPerMonth {
    private List<String> months;
    private List<Double> income;
    private List<Double> expenses;

    public IncomeAndExpensesPerMonth(List<String> months, List<Double> income, List<Double> expenses) {
        this.months = months;
        this.income = income;
        this.expenses = expenses;
    }

    public IncomeAndExpensesPerMonth() { }

    public List<String> getMonth() {
        return months;
    }

    public void setMonth(List<String> months) {
        this.months = months;
    }

    public List<Double> getIncome() {
        return income;
    }

    public void setIncome(List<Double> income) {
        this.income = income;
    }

    public List<Double> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Double> expenses) {
        this.expenses = expenses;
    }
}
