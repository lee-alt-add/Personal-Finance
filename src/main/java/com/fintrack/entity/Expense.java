package com.fintrack.entity;

import java.time.LocalDateTime;
import java.sql.Timestamp;

public class Expense {
    private int id;
    private int userId;
    private double amount;
    private String category;
    private String description;
    private Timestamp date;

    public Expense(int id, int userId, double amount, String category, String description, Timestamp date) {
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

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getId() { return id; }

    public int getUserId() { return userId; }

    public double getAmount() { return amount; }

    public String getCategory() { return category; }

    public String getDescription() { return description; }

    public Timestamp getDate() { return date; }

    @Override
    public String toString() {
        return "{id:%d, userId:%d, amount:%.2f%n, category:%s, description:%s}".formatted(id, userId, amount, category, description);
    }
}