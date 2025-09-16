package com.fintrack.entity;

import java.sql.Timestamp;

public class Transactions {
    private int id;
    private int userId;
    private double amount;
    private String category;
    private String description;
    private String source;
    private Timestamp date;
    private String transactionType; // "income" or "expense"

    public Transactions(int id, int userId, double amount, String category,
                        String description, String source, Timestamp date,
                        String transactionType) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.source = source;
        this.date = date;
        this.transactionType = transactionType;
    }

    public Transactions() { }

    // --- Getters and setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public Timestamp getDate() { return date; }
    public void setDate(Timestamp date) { this.date = date; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
}
