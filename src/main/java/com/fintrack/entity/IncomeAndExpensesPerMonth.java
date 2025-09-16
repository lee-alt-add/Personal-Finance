package com.fintrack.entity;

public class IncomeAndExpensesPerMonth {
    private String month;
    private double income;
    private double expenses;

    public IncomeAndExpensesPerMonth(String month, double income, double expenses) {
        this.month = month;
        this.income = income;
        this.expenses = expenses;
    }

    public IncomeAndExpensesPerMonth() { }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpenses() {
        return expenses;
    }

    public void setExpenses(double expenses) {
        this.expenses = expenses;
    }
}
