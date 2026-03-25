package com.example.ltdt5_test2;

public class Transaction {
    private long id;
    private double amount;
    private int type; // 1: Thu, 2: Chi
    private String category;
    private String date;
    private String note;

    public Transaction(long id, double amount, int type, String category, String date, String note) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.date = date;
        this.note = note;
    }

    // Getters
    public long getId() { return id; }
    public double getAmount() { return amount; }
    public int getType() { return type; }
    public String getCategory() { return category; }
    public String getDate() { return date; }
    public String getNote() { return note; }
}