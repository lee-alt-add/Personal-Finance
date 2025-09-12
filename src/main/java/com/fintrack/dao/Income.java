package com.fintrack.dao;

import java.time.LocalDateTime;

public class Income {
    private int id;
    private int userId;
    private double amount;
    private String source;
    private LocalDateTime date;

    public Income(int id, int userId, double amount, String source, LocalDateTime date) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.source = source;
        this.date = date;
    }

    public Income() {}

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getId() { return id; }

    public int getUserId() { return userId; }

    public double getAmount() { return amount; }

    public String getSource() { return source; }

    public LocalDateTime getDate() { return date; }
}