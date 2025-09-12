package com.fintrack.dao;

import java.time.LocalDateTime;

public class Expense {
    private int id;
    private int userId;
    private double amount;
    private String category;
    private String description;
    private LocalDateTime date;

    public Expense(int id, int userId, double amount, String category, String description, LocalDateTime date) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public Expense() {}
    
    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getId() { return id; }

    public int getUserId() { return userId; }

    public double getAmount() { return amount; }

    public String getCategory() { return category; }

    public String getDescription() { return description; }

    public LocalDateTime getDate() { return date; }
}