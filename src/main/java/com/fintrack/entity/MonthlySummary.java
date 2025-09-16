package com.fintrack.entity;

public class MonthlySummary {
    private double expenses;
    private  double income;
    private double balance;

    public MonthlySummary(double income, double expenses, double balance) {
        this.expenses = expenses;
        this.income = income;
        this.balance = balance;
    }

    public MonthlySummary() { }

    public double getExpenses() {
        return expenses;
    }

    public void setExpenses(double expenses) {
        this.expenses = expenses;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
