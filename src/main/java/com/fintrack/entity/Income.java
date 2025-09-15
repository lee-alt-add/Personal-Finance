package com.fintrack.entity;

import java.time.LocalDateTime;
import java.sql.Timestamp;

public class Income implements Timestamped {
    private int id;
    private int userId;
    private double amount;
    private String source;
    private Timestamp date;

    public Income(int id, int userId, double amount, String source, Timestamp date) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.source = source;
        this.date = date;
    }

    public Income(int userId, double amount, String source) {
        this.userId = userId;
        this.amount = amount;
        this.source = source;
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

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getId() { return id; }

    public int getUserId() { return userId; }

    public double getAmount() { return amount; }

    public String getSource() { return source; }

    @Override
    public Timestamp getDate() { return date; }
}