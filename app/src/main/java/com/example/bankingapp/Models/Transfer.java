package com.example.bankingapp.Models;

import androidx.annotation.NonNull;

public class Transfer {
    private long from_account;
    private long to_account;
    private double amount;
    private String date;

    public Transfer() {
    }

    public Transfer(String date, long from_account, long to_account, double amount) {
        this.from_account = from_account;
        this.to_account = to_account;
        this.amount = amount;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getFrom_account() {
        return from_account;
    }

    public void setFrom_account(long from_account) {
        this.from_account = from_account;
    }

    public long getTo_account() {
        return to_account;
    }

    public void setTo_account(long to_account) {
        this.to_account = to_account;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transfers{" +
                "from_account=" + from_account +
                ", to_account=" + to_account +
                ", amount=" + amount +
                ", date='" + date + '\'' +
                '}';
    }
}