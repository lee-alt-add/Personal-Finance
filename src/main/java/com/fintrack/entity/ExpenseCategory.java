package com.fintrack.entity;

public class ExpenseCategory {
    private String category;
    private double amount;

    public ExpenseCategory(String category, double amount) {
        this.category = category;
        this.amount = amount;
    }

    public ExpenseCategory() { }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
